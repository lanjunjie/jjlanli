package com.myfp.myfund.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.beans.BankTypeResult;

public class BindingBankActivity extends BaseActivity {
	TextView tv_bindingbank_banktype, tv_bindingbank_bankidnum,
			tv_bindingbank_province, tv_bindingbank_city,
			tv_bindingbank_bankaccountaddress;
	private String channelid,paycenterid;
	Bundle bundle;
	public static BindingBankActivity instance = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_bindingbank);
		instance = this;
		bundle = getIntent().getExtras();
	}

	@Override
	protected void initViews() {
		setTitle("银行卡绑定");
		tv_bindingbank_banktype = (TextView) findViewById(R.id.tv_bindingbank_banktype);
		tv_bindingbank_bankidnum = (TextView) findViewById(R.id.tv_bindingbank_bankidnum);
		tv_bindingbank_province = (TextView) findViewById(R.id.tv_bindingbank_province);
		tv_bindingbank_city = (TextView) findViewById(R.id.tv_bindingbank_city);
		tv_bindingbank_bankaccountaddress = (TextView) findViewById(R.id.tv_bindingbank_bankaccountadress);
		findViewAddListener(R.id.layout_bindingbank_banktype);
		findViewAddListener(R.id.layout_bindingbank_bankidnum);
		findViewAddListener(R.id.layout_bindingbank_province);
		findViewAddListener(R.id.layout_bindingbank_city);
		findViewAddListener(R.id.layout_bindingbank_bankaccountaddress);
		findViewAddListener(R.id.bt_writeifor_nextstep);
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 选择银行卡类型
		case R.id.layout_bindingbank_banktype:
			Intent intent = new Intent(BindingBankActivity.this,
					BankTypeActivity.class);
			startActivityForResult(intent, 0);

			break;
		// 选择银行卡卡号
		case R.id.layout_bindingbank_bankidnum:
			if (tv_bindingbank_banktype.getText().length() == 0) {
				showToast("请选择银行卡类型");
				return;
			} else {
				Intent intent1 = new Intent(BindingBankActivity.this,
						BankIdActivity.class);
				startActivityForResult(intent1, 1);
			}

			break;
		// 选择省份
		case R.id.layout_bindingbank_province:

			if (tv_bindingbank_banktype.getText().length() == 0
					|| tv_bindingbank_bankidnum.getText().length() == 0) {
				showToast("请填写银行卡类型或银行卡号");
				return;
			} else {
				Intent intent2 = new Intent(BindingBankActivity.this,
						ProvinceSelectedActivity.class);
				startActivityForResult(intent2, 2);
			}
			break;
		// 选择城市
		case R.id.layout_bindingbank_city:
			if (tv_bindingbank_province.getText().length() == 0) {
				showToast("请选择省份");
				return;
			} else {
				Intent intent3 = new Intent(BindingBankActivity.this,
						CitySelectedActivity.class);
				intent3.putExtra("Province", tv_bindingbank_province.getText()
						.toString());
				startActivityForResult(intent3, 3);
			}

			break;
		// 选择银行卡开户网点
		case R.id.layout_bindingbank_bankaccountaddress:
			if (tv_bindingbank_city.getText().length() == 0) {
				showToast("请选择省市");
				return;
			} else {
				Intent intent4 = new Intent(BindingBankActivity.this,
						AccountAddressActivity.class);
				intent4.putExtra("City", tv_bindingbank_city.getText()
						.toString());
				intent4.putExtra("ChannelId", channelid);
				startActivityForResult(intent4, 4);
			}

			break;
		// 下一步
		case R.id.bt_writeifor_nextstep:
			if (tv_bindingbank_bankaccountaddress.getText().length() == 0) {
				showToast("请填写完整信息");
				return;
			} else {
				Intent intent5 = new Intent(BindingBankActivity.this,
						BankSmallMoneyActivity.class);
				bundle.putString("BankType", tv_bindingbank_banktype.getText().toString());
				bundle.putString("BankCardId", tv_bindingbank_bankidnum.getText().toString());
				bundle.putString("Province", tv_bindingbank_province.getText().toString());
				bundle.putString("City", tv_bindingbank_city.getText().toString());
				bundle.putString("AccountAddress", tv_bindingbank_bankaccountaddress.getText().toString());
				bundle.putString("ChannelId",channelid);
				bundle.putString("PayCenterId",paycenterid);
				intent5.putExtras(bundle);
				startActivity(intent5);
				
			}

			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		// 银行卡类型
		case 0:
			if (data != null) {
				Bundle bundle = data.getExtras();
				BankTypeResult res;
				res = (BankTypeResult) bundle.getSerializable("BankTypeResult");
				tv_bindingbank_banktype.setText(res.getChannelname());
				channelid = res.getChannelid();
				paycenterid = res.getPaycenterid();
			}

			break;
		// 银行卡卡号
		case 1:
			if (data != null) {
				Bundle bundle = data.getExtras();
				tv_bindingbank_bankidnum.setText(bundle.getString("BankId"));
			}

			break;
		// 省份
		case 2:
			if (data != null) {
				Bundle bundle = data.getExtras();
				tv_bindingbank_province.setText(bundle.getString("Pname"));
				tv_bindingbank_city.setText(bundle.getString("Cname"));
			}

			break;
		case 3:
			if (data != null) {
				Bundle bundle = data.getExtras();
				tv_bindingbank_city.setText(bundle.getString("Cname"));
			}
		case 4:
			if (data != null) {
				Bundle bundle = data.getExtras();
				tv_bindingbank_bankaccountaddress.setText(bundle
						.getString("AccountAddress"));
			}

			break;

		default:
			break;
		}
	}

}
