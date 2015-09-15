package com.myfp.myfund.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.MonthQuality;
import com.myfp.myfund.utils.ImageCacheManager;

public class PrivateFundDetailActivity extends BaseActivity {

	private MonthQuality mQuality;
	private TextView tv_isSaled, tv_fundCode, tv_expectedEarnings, tv_proType,
			tv_timeLimit, tv_minInvestment, tv_proDetail;
	private Button bt_orderOL;
	private NetworkImageView img_banner;
	private ImageLoader imageLoader;
	private RequestQueue requestQueue;
	private String baseUrl = "http://www.myfund.com";
	private String userName;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_privatefund);
		
		userName = App.getContext().getUserName();
		
		Intent intent = getIntent();
		mQuality = (MonthQuality) intent.getSerializableExtra("MonthQuality");
		
		requestQueue = Volley.newRequestQueue(this);
		imageLoader = new ImageLoader(requestQueue,
				ImageCacheManager.getInstance());
	}

	@Override
	protected void initViews() {
		setTitle(mQuality.getFundName());
		
		findViewAddListener(R.id.bt_privateFund_orderOL);
		findViewAddListener(R.id.bt_privateFund_orderTel);
		findViewAddListener(R.id.tv_privateFund_seeAll);

		tv_isSaled = (TextView) findViewById(R.id.tv_privateFund_saled);
		tv_fundCode = (TextView) findViewById(R.id.tv_privateFund_fundCode);
		tv_expectedEarnings = (TextView) findViewById(R.id.tv_privateFund_expectedEarnings);
		tv_proType = (TextView) findViewById(R.id.tv_privateFund_proType);
		tv_timeLimit = (TextView) findViewById(R.id.tv_privateFund_timeLimit);
		tv_minInvestment = (TextView) findViewById(R.id.tv_privateFund_minInvestment);
		tv_proDetail = (TextView) findViewById(R.id.tv_privateFund_proDetail);
		img_banner = (NetworkImageView) findViewById(R.id.imgView_privateFund_banner);
		bt_orderOL = (Button)findViewById(R.id.bt_privateFund_orderOL);
		
		tv_isSaled.setText(mQuality.getIsOnSale());
		tv_fundCode.setText(mQuality.getFundCode());
		if (mQuality.getExpectedEarnings().contains("%")) {
			tv_expectedEarnings.setText(mQuality.getExpectedEarnings());
		}else {
			tv_expectedEarnings.setText(mQuality.getExpectedEarnings()+"%");
		}
		tv_proType.setText(mQuality.getProType());
		tv_timeLimit.setText(mQuality.getTimeLimit());
		tv_minInvestment.setText(mQuality.getMinInvestment());
		tv_proDetail.setText(mQuality.getProDetail());
		
//		img_banner.setDefaultImageResId(R.drawable.ic_launcher);
//		img_banner.setErrorImageResId(R.drawable.ic_launcher);
		img_banner.setImageUrl(baseUrl+mQuality.getBannerLogo().substring(1), imageLoader);
		if (!"在售".equals(mQuality.getIsOnSale())) {
			bt_orderOL.setEnabled(false);
			bt_orderOL.setBackgroundColor(Color.GRAY);
		}
	}
	
	@Override
	protected void onResume() {
		userName = App.getContext().getUserName();
		super.onResume();
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_privateFund_orderOL:
			if (userName==null) {
				showToast("请先登录!");
				startActivity(MyMeansActivity.class);
				return;
			}
			Intent intent = new Intent(this, OrderOnlineActivity.class);
			intent.putExtra("MonthQuality", mQuality);
			startActivity(intent);
			break;
		case R.id.bt_privateFund_orderTel:
			// 电话预约
			Intent orderTel = new Intent();
			orderTel.setAction(Intent.ACTION_DIAL);
			orderTel.setData(Uri.parse("tel:400-888-6661"));
			startActivity(orderTel);
			break;
		case R.id.tv_privateFund_seeAll:
			// 查看全部
			Intent detail = new Intent(this, ShowWebActivity.class);
			detail.putExtra("Url", mQuality.getProDetail2());
			startActivity(detail);
			break;
		default:
			break;
		}
	}

}
