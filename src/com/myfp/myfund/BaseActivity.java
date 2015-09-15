package com.myfp.myfund;


import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.ErrorResult;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.ResponseResult;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.ui.FundNewsActivity;
import com.myfp.myfund.ui.MyMeansActivity;
import com.myfp.myfund.ui.MyPropertyActivity;
import com.myfp.myfund.ui.MyfundHomeActivity;

/**
 * Activity基类
 * @author Bruce.Wang
 *
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener,OnDataReceivedListener{
	
	public ProgressDialog progressDialog;
	
	public LinearLayout ll_top_layout_left_view;
	public LinearLayout top_layout_center_view;
	public LinearLayout ll_top_layout_right_view;
	public LinearLayout top_layout_right_view_two;
	public ImageView iv_mainactivity_top_left;
	public ImageView iv_mainactivity_top_center_img;
	public ImageView iv_mainactivity_top_right;
	public ImageView iv_top_layout_right_view_two;
	public TextView tv_title;
	private View headerView;
	private App app;
	public final static int REQUEST_LOGIN = 1001;

	public String TAG = getClass().getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = App.getContext();
		app.unDestroyActivityList.add(this);
		setFullScreen(false);
		setContentView();
		initHeaderView(); 
		initViews();
	}
	
	/**
	 * 设置内容显示
	 */
	protected abstract void setContentView();

	/**
	 * 初始化控件
	 */
	protected abstract void initViews();

	/**
	 * 子类实现响应监听
	 */
	protected abstract void onViewClick(View v);
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//头部左键
		case R.id.ll_top_layout_left_view:
			//不是主页面
			if (!"".equals(getClass())) {
				if (getClass()==FundNewsActivity.class
						||getClass()==MyPropertyActivity.class
						) {
					iv_mainactivity_top_left.setEnabled(false);
				}else {
					if(getClass()!=MyfundHomeActivity.class){
						finish();
					}
				}
			}
			break;
		}
		onViewClick(v);
	}
	
	
	/**
	 * 设置标题
	 */
	public void setTitle(String title) {
		if (title == null) {
			title = "";
		}
		headerView.setVisibility(View.VISIBLE);
		tv_title.setText(title);
	}
	
	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		headerView = (RelativeLayout)findViewById(R.id.ll_main_top_layout);
		// -----------
		// /TOP
		
		ll_top_layout_left_view = (LinearLayout) findViewById(R.id.ll_top_layout_left_view);
		top_layout_center_view = (LinearLayout) findViewById(R.id.top_layout_center_view);
		ll_top_layout_right_view = (LinearLayout) findViewById(R.id.ll_top_layout_right_view);

		iv_mainactivity_top_left = (ImageView) findViewById(R.id.iv_mainactivity_top_left);
		iv_mainactivity_top_center_img = (ImageView) findViewById(R.id.iv_mainactivity_top_center_img);
		iv_mainactivity_top_right = (ImageView) findViewById(R.id.iv_mainactivity_top_right);

		
		ll_top_layout_left_view.setOnClickListener(this);
		top_layout_center_view.setOnClickListener(this);
		ll_top_layout_right_view.setOnClickListener(this);
		
		tv_title = (TextView) findViewById(R.id.tv_mainactivity_top_center);
		
		switchViewShow();
		
	}
	/**
	 * 根据当前显示页面控制头部控件显示
	 */
	private void switchViewShow() {
		Class<? extends BaseActivity> clazz = this.getClass();
		if (clazz!=null) {
			if (clazz==FundNewsActivity.class||clazz==MyMeansActivity.class
					||clazz==MyPropertyActivity.class
					) {
				iv_mainactivity_top_left.setBackgroundResource(R.drawable.header_backa);	
			}
			if(clazz == MyfundHomeActivity.class){
				//主页面
				iv_mainactivity_top_left.setBackgroundResource(R.drawable.sm_toggle);
			}	
		}
	}

	/**
	 * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
	 * 
	 * @param fullScreen
	 */
	public void setFullScreen(boolean fullScreen) {
		if (fullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

	}
	
	/**
	 * 找到控件并添加监听
	 * @param id
	 * @return
	 */
	public View findViewAddListener(int id){
		View view = findViewById(id);
		view.setOnClickListener(this);
		return view;
	}
	/**
	 * 找到控件并添加监听
	 * @param id
	 * @return
	 */
	public View findViewAddListener(View parent,int id){
		View view = parent.findViewById(id);
		view.setOnClickListener(this);
		return view;
	}
	
	Toast toast;
	/**
	 * 短时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToast(String info) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
		toast.setText(info);
		toast.show();
	}
	
	/**
	 * 长时间显示Toast
	 * 
	 * @param info
	 *            显示的内容
	 */
	public void showToastLong(String info) {
		Toast.makeText(this, info, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 显示正在加载的进度条
	 * 
	 */
	public void showProgressDialog() {
		showProgressDialog("加载中...");
	}
	
	public void showProgressDialog(String msg) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		progressDialog = new ProgressDialog(BaseActivity.this);
		progressDialog.setMessage(msg);
		//progressDialog.setCanceledOnTouchOutside(true);
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		try {
			progressDialog.show();
		} catch (BadTokenException exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * 取消对话框显示
	 */
	public void disMissDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	public void startActivity(Class<? extends Activity> clazz) {
		startActivity(new Intent(this, clazz));
	}
	
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		App.getContext().unDestroyActivityList.remove(this);
	}
	
	@Override
	public void onReceiveData(ApiType api,String json) {
		
	}
	
	public void execApi(ApiType api,RequestParams params){
		RndDataApi.executeNetworkApi(api, params, this);
	}
	public void execApi(ApiType api,RequestParams params,OnDataReceivedListener listener){
		RndDataApi.executeNetworkApi(api, params,listener);
	}
	
	public  ResponseResult parseJson(String json,
			Class<? extends ResponseResult> clazz) throws JSONException {
		
		if(json!=null){
			//成功
				ResponseResult res = JSON.parseObject(json, clazz);
				return res;
		}else{
			Log.i(TAG, "------"+"解析错误！");
			ErrorResult err = JSON.parseObject(json, ErrorResult.class);
			return err;
		}
	}

	
}
