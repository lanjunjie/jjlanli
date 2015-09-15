package com.myfp.myfund.ui;

import org.json.JSONArray;
import org.json.JSONException;

import android.R.string;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

/**
 * 基金详情
 * 
 * @author Max.Zhao
 * 
 */
public class DetailActivity extends BaseActivity {

	private Button[] arr_bottom;
	private String fundName, fundCode, userName;
	private ImageView img_isSelected;
	private JJZSFragment jjzsFragment;
	private JJGKFragment jjgkFragment;
	private JJYJFragment jjyjFragment;
	private CCFXFragment ccfxFragment;
	private FrameLayout mContainer;
	private String sessionId;
	

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_detail);

		Intent intent = getIntent();
		fundName = intent.getStringExtra("fundName");
		fundCode = intent.getStringExtra("fundCode");
		sessionId = App.getContext().getSessionid();
		System.out.println("sessionId=====================>"+sessionId);
		userName = App.getContext().getUserName();

		jjzsFragment = new JJZSFragment();
		jjgkFragment = new JJGKFragment();
		jjyjFragment = new JJYJFragment();
		ccfxFragment = new CCFXFragment();

		RequestParams params = new RequestParams(this);
		params.put("InputFundValue", fundCode);
		params.put("UserName", userName);
		execApi(ApiType.GET_FUND_DETAIL_INFO, params);
	}

	public String getFundCode() {
		return fundCode;
	}

	public String getFundName() {
		return fundName;
	}
	public String getSessionId(){
		return sessionId;
	}

	@Override
	protected void onResume() {
		super.onResume();
		userName = App.getContext().getUserName();
	}

	@Override
	protected void initViews() {
		initTabView();
		setTitle(fundName + "\r\n" + fundCode);

		mContainer = (FrameLayout) findViewById(R.id.layout_detail_container);

		addFragment(0);
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.setVisibility(View.VISIBLE);
		img_isSelected = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img_isSelected.setImageResource(R.drawable.white_star);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.ll_top_layout_right_view:
			if (TextUtils.isEmpty(userName)) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this, MyMeansActivity.class));
			} else {
				RequestParams params = new RequestParams(this);
				params.put("UserName", userName);
				params.put("FundCode", fundCode.trim());
				params.put("FundName", fundName.trim());
				execApi(ApiType.GET_MY_FUND_CENTER, params);
			}
			break;

		default:
			break;
		}
	}

	// 初始化底部导航条
	private void initTabView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_bottom);
		arr_bottom = new Button[4];
		for (int i = 0; i < arr_bottom.length; i++) {
			Button button = (Button) layout.getChildAt(i);
			arr_bottom[i] = button;
			arr_bottom[i].setEnabled(true);
			arr_bottom[i].setTag(i);
			arr_bottom[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < arr_bottom.length; i++) {
						arr_bottom[i].setEnabled(true);
						arr_bottom[i].setTextColor(Color.BLACK);
					}
					arr_bottom[(Integer) v.getTag()].setEnabled(false);
					arr_bottom[(Integer) v.getTag()].setTextColor(Color.RED);

					addFragment((Integer) v.getTag());
				}

			});
			arr_bottom[0].setEnabled(false);
			arr_bottom[0].setTextColor(Color.RED);
		}
	}
//添加fragment到framelayout mContainer布局中
	private void addFragment(int index) {
		//
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		//
		// switch (index) {
		// case 0:
		// transaction.replace(R.id.layout_detail_container, jjzsFragment,
		// "jjzsFragment");
		// break;
		// case 1:
		// transaction.replace(R.id.layout_detail_container, jjgkFragment,
		// "jjgkFragment");
		// break;
		// case 2:
		// transaction.replace(R.id.layout_detail_container, jjyjFragment,
		// "jjyjFragment");
		// break;
		// case 3:
		// transaction.replace(R.id.layout_detail_container, ccfxFragment,
		// "ccfxFragment");
		// break;
		// default:
		// break;
		// }
		//
		// transaction.commit();

		Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(
				mContainer, index);
		mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
		mFragmentPagerAdapter.finishUpdate(mContainer);

	}
   //四个fragment的适配
	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 1:
				return jjgkFragment;
			case 2:
				return jjyjFragment;
			case 3:
				return ccfxFragment;
			case 0:
			default:
				return jjzsFragment;
			}
		}

		@Override
		public int getCount() {
			return 4;
		}
	};


	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_MY_FUND_CENTER) {
				int returnResult = array.getJSONObject(0)
						.getInt("ReturnResult");
				switch (returnResult) {
				case 0:
					showToast("添加成功!");
					img_isSelected
							.setImageResource(R.drawable.red_star_solid_big);
					break;
				case 1:
					showToast("用户名不能为空!");
					break;
				case 2:
					showToast("取消自选!");
					img_isSelected.setImageResource(R.drawable.white_star);
					break;
				case 3:
					showToast("添加成功!");
					img_isSelected
							.setImageResource(R.drawable.red_star_solid_big);
					break;
				case 4:
					showToast("添加失败!");
					break;
				case 5:
					showToast("系统参数错误!");
					break;

				}
			} else if (api == ApiType.GET_FUND_DETAIL_INFO) {
				String isFlag = array.getJSONObject(0).getString("IsFlag")
						.trim();
				if (isFlag.equals("1")) {
					img_isSelected
							.setImageResource(R.drawable.red_star_solid_big);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
