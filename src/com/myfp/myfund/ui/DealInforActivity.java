package com.myfp.myfund.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.R.id;

public class DealInforActivity extends BaseActivity {
	private String idCard, passWord, encodeIdCard, encodePassWord,
			customrisklevel, countfund, depositacctname, totalfundmarketvalue;
	TextView tv_depositacctname, tv_countfund, tv_totalfundmarketvalue,
			tv_customrisklevel;
	public static DealInforActivity instance = null;
	private long lastBack;
	private SharedPreferences sPreferences;
	private String lean="false";
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_dealinfor);
		instance = this;
		
			Intent intent = getIntent();
			encodeIdCard = intent.getStringExtra("IDCard");
			encodePassWord = intent.getStringExtra("PassWord");
			customrisklevel = intent.getStringExtra("CustomRiskLevel");
			countfund = intent.getStringExtra("CountFund");
			System.out.println("countfund数据为空不============>"+countfund);
			depositacctname = intent.getStringExtra("DepositacctName");
			System.out.println("depositacctname数据是否为空------------》"+depositacctname);
			totalfundmarketvalue = intent.getStringExtra("TotalFundMarketValue");
			lean = intent.getStringExtra("lean");
			System.out.println("lean-=-====>"+lean);
		
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("个人信息");

		tv_depositacctname = (TextView) findViewById(R.id.tv_depositacctname);
		tv_countfund = (TextView) findViewById(R.id.tv_countfund);
		tv_totalfundmarketvalue = (TextView) findViewById(R.id.tv_totalfundmarketvalue);
		tv_customrisklevel = (TextView) findViewById(R.id.tv_customrisklevel);
		
		LinearLayout layout = (LinearLayout) findViewAddListener(R.id.ll_top_layout_right_view);
		layout.getLayoutParams().height=120;
		layout.getLayoutParams().width=180;
		layout.setVisibility(View.VISIBLE);
		ImageView img = (ImageView) findViewById(R.id.iv_mainactivity_top_right);
		img.getLayoutParams().height = 120;
		img.getLayoutParams().width = 180;
		
		img.setImageResource(R.drawable.safequit);
		//img.setVisibility(View.GONE);
//		TextView text = (TextView)findViewById(R.id.tv_top_layout_right_view);
//		text.setVisibility(View.VISIBLE);

		// 申购
		findViewAddListener(R.id.bt_dealbuy);
		// 赎回
		findViewAddListener(R.id.bt_dealredeem);
		// 撤单
		findViewAddListener(R.id.bt_dealcancellation);
		// 持有详情
		findViewAddListener(R.id.bt_dealholddetail);
		// 转换
		findViewAddListener(R.id.bt_dealchange);
		// 委托查询
		findViewAddListener(R.id.bt_dealentrust);
		// 交易历史查询
		findViewAddListener(R.id.bt_dealhistory);
		// 定投
		findViewAddListener(R.id.bt_dealdt);
		// 定投管理
		findViewAddListener(R.id.bt_dealdtmanage);
		//账户信息
		findViewAddListener(R.id.bt_accountinfor);
		//安全退出
		findViewAddListener(R.id.ll_top_layout_right_view);
		//重新评估
		findViewAddListener(R.id.bt_repeatassess);
		//分红设置
		findViewAddListener(R.id.bt_dividendtext);
		//密码修改
		findViewAddListener(R.id.bt_changepasswordtext);

		tv_depositacctname.setText(depositacctname);
		tv_countfund.setText(countfund+"(支)");
		tv_totalfundmarketvalue.setText(totalfundmarketvalue+"(元)");
		if(customrisklevel!=null) {
			switch (Integer.parseInt(customrisklevel)) {
			case 01:
				tv_customrisklevel.setText("安逸型");
				break;
			case 02:
				tv_customrisklevel.setText("保守型");
				break;
			case 03:
				tv_customrisklevel.setText("稳健型");
				break;
			case 04:
				tv_customrisklevel.setText("积极型");
				break;
			case 05:
				tv_customrisklevel.setText("激进型");
				break;
				
			default:
				break;
			}
			
		}
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bt_dealbuy:
			// 基金购买
			Intent intent = new Intent(DealInforActivity.this,
					DealBuyActivity.class);
			intent.putExtra("IDCard", encodeIdCard);
			intent.putExtra("PassWord", encodePassWord);
			startActivity(intent);
			break;
		case R.id.bt_dealredeem:
			// 基金赎回
			Intent intent1 = new Intent(DealInforActivity.this,
					DealRedeemActivity.class);
			intent1.putExtra("IDCard", encodeIdCard);
			intent1.putExtra("PassWord", encodePassWord);
			startActivity(intent1);
			break;
		case R.id.bt_dealcancellation:
			// 基金撤单
			Intent intent2 = new Intent(DealInforActivity.this,
					DealCancellationActivity.class);
			intent2.putExtra("IDCard", encodeIdCard);
			intent2.putExtra("PassWord", encodePassWord);
			startActivity(intent2);
			break;
		case R.id.bt_dealchange:
			// 基金转换
			Intent intent4 = new Intent(DealInforActivity.this,
					DealChangeActivity.class);
			intent4.putExtra("IDCard", encodeIdCard);
			intent4.putExtra("PassWord", encodePassWord);
			startActivity(intent4);
			break;
		case R.id.bt_dealholddetail:
			// 持有详情
			Intent intent3 = new Intent(DealInforActivity.this,
					HoldDetailActivity.class);
			intent3.putExtra("IDCard", encodeIdCard);
			intent3.putExtra("PassWord", encodePassWord);
			startActivity(intent3);
			break;
		case R.id.bt_dealentrust:
			// 委托查询
			Intent intent5 = new Intent(DealInforActivity.this,
					DealEntrustActivity.class);
			intent5.putExtra("IDCard", encodeIdCard);
			intent5.putExtra("PassWord", encodePassWord);
			startActivity(intent5);
			break;
		case R.id.bt_dealhistory:
			// 交易历史查询
			Intent intent6 = new Intent(DealInforActivity.this,
					DealHistoryActivity.class);
			intent6.putExtra("IDCard", encodeIdCard);
			intent6.putExtra("PassWord", encodePassWord);
			startActivity(intent6);
			break;
		case R.id.bt_dealdt:
			// 定投
			Intent intent7 = new Intent(DealInforActivity.this,
					DealDTActivity.class);
			intent7.putExtra("IDCard", encodeIdCard);
			intent7.putExtra("PassWord", encodePassWord);
			intent7.putExtra("CustomRiskLevel", customrisklevel);
			startActivity(intent7);
			break;
		case R.id.bt_dealdtmanage:
			// 定投管理
			Intent intent8 = new Intent(DealInforActivity.this,
					DTManageActivity.class);
			intent8.putExtra("IDCard", encodeIdCard);
			intent8.putExtra("PassWord", encodePassWord);
			intent8.putExtra("CustomRiskLevel", customrisklevel);
			startActivity(intent8);
			break;
		case R.id.bt_accountinfor:
			// 账户信息
			Intent intent9 = new Intent(DealInforActivity.this,
					AccountInforActivity.class);
			intent9.putExtra("IDCard", encodeIdCard);
			intent9.putExtra("PassWord", encodePassWord);
			intent9.putExtra("CustomRiskLevel", customrisklevel);
			startActivity(intent9);
			break;
		case R.id.ll_top_layout_right_view:
			// 安全退出
			App.getContext().setIdCard(null);
			App.getContext().setEncodePassWord(null);
			SharedPreferences sPreferences = getSharedPreferences("Setting",
					MODE_PRIVATE);
			Editor editor = sPreferences.edit();
			editor.putString("EncodePassWord", null);
			editor.commit();
			
			Intent intent10 = new Intent(DealInforActivity.this,
					MyActivityGroup.class);
			
			startActivity(intent10);
			finish();
			break;
		case R.id.bt_repeatassess:
			// 重新评估
			Intent intent11 = new Intent(DealInforActivity.this,
					RiskAssessmentActivity.class);
			intent11.putExtra("IDCard", encodeIdCard);
			intent11.putExtra("PassWord", encodePassWord);
			intent11.putExtra("CustomRiskLevel", customrisklevel);
			intent11.putExtra("CountFund", countfund);
			intent11.putExtra("DepositacctName", depositacctname);
			intent11.putExtra("TotalFundMarketValue", totalfundmarketvalue);
			intent11.putExtra("Flag","1");
			startActivity(intent11);
//			finish();
			break;
		//设置分红方式
		case R.id.bt_dividendtext:
			Intent intent12=new Intent(DealInforActivity.this, 
					queryAssetsActivity.class);
			intent12.putExtra("IDCard", encodeIdCard);
			intent12.putExtra("PassWord", encodePassWord);
			startActivity(intent12);
//			finish();
			break;
		//修改密码
		case R.id.bt_changepasswordtext:
			Intent intent13=new Intent(DealInforActivity.this, ResetPasswordActivity.class);
			intent13.putExtra("IDCard", encodeIdCard);
			intent13.putExtra("PassWord", encodePassWord);
			startActivity(intent13);
			
			break;
		case R.id.ll_top_layout_left_view:
			if (lean!=null) {
				
				if (lean.equals("true")) {
				finish();
			}
			}else {
				finish();
				MyActivityGroup.instance.finish();
				Intent intent14=new Intent(this, MyActivityGroup.class);
				startActivity(intent14);
			}

		default:
			break;
		}

	}
	
	@Override
	public void onBackPressed() {
		long curTime = System.currentTimeMillis();
		if (curTime - lastBack > 2000) {
			lastBack = curTime;
			showToast("再按一次退出展恒基金网");
		} else {
			String listStr = JSON.toJSONString(App.getContext().userList);
			//Editor editor = sPreferences.edit();
			//editor.putString("UserList", listStr);
			//editor.commit();
			finish();
			
		}
	}

}
