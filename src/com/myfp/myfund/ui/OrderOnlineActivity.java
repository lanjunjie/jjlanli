package com.myfp.myfund.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.api.beans.MonthQuality;

public class OrderOnlineActivity extends BaseActivity {

	private MonthQuality mQuality;
	private TextView tv_isSaled, tv_fundCode, tv_expectedEarnings, tv_proType,
			tv_timeLimit, tv_minInvestment, tv_fundName, tv_time,tv_orderTime;
	private EditText et_invest, et_mobile;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_order_ol);

		Intent intent = getIntent();
		mQuality = (MonthQuality) intent.getSerializableExtra("MonthQuality");
	}

	@Override
	protected void initViews() {
		setTitle("在线预约");

		tv_fundName = (TextView) findViewById(R.id.tv_orderOL_fundName);
		tv_isSaled = (TextView) findViewById(R.id.tv_orderOL_saled);
		tv_fundCode = (TextView) findViewById(R.id.tv_orderOL_fundCode);
		tv_expectedEarnings = (TextView) findViewById(R.id.tv_orderOL_expectedEarnings);
		tv_proType = (TextView) findViewById(R.id.tv_orderOL_proType);
		tv_timeLimit = (TextView) findViewById(R.id.tv_orderOL_timeLimit);
		tv_minInvestment = (TextView) findViewById(R.id.tv_orderOL_minInvestment);
		et_invest = (EditText) findViewById(R.id.et_orderOL_investMoney);
		tv_time = (TextView) findViewAddListener(R.id.et_orderOL_signTime);
		et_mobile = (EditText) findViewById(R.id.et_orderOL_mobile);
		tv_orderTime = (TextView) findViewById(R.id.tv_orderOL_orderTime);
		
		tv_fundName.setText(mQuality.getFundName());
		tv_isSaled.setText(mQuality.getIsOnSale());
		tv_fundCode.setText(mQuality.getFundCode());
		tv_expectedEarnings.setText(mQuality.getExpectedEarnings());
		tv_proType.setText(mQuality.getProType());
		tv_timeLimit.setText(mQuality.getTimeLimit());
		tv_minInvestment.setText(mQuality.getMinInvestment());
		tv_orderTime.setText(mQuality.getSignTimeStart()+"至"+mQuality.getSignTimeEnd());

		findViewAddListener(R.id.bt_orderOL_order);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_orderOL_order:
			String userName = App.getContext().getUserName();
			if (userName == null) {
				showToast("请先登录");
				startActivity(MyMeansActivity.class);
				return;
			}
			if (et_mobile.getText().length() != 11) {
				showToast("手机号码位数错误!");
				return;
			}
			RequestParams params = new RequestParams(this);
			params.put("UserName", userName);
			params.put("FundCode", mQuality.getFundCode());
			params.put("FundName", mQuality.getFundName());
			params.put("InvestMoney", et_invest.getText().toString());
			params.put("SignTime", tv_time.getText().toString());
			params.put("Mobile", et_mobile.getText().toString());
			execApi(ApiType.GET_ONLINE_BOOK, params);
			break;
		case R.id.et_orderOL_signTime:
			showDialog(1);
		default:
			break;
		}
	}

	DatePickerDialog.OnDateSetListener onDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (isBefore(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, mQuality.getSignTimeStart()) || isBefore(mQuality.getSignTimeEnd(), year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)) {
				showToast("请选择正确的预约时间");
				return;
			}
			tv_time.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
			t.setToNow(); // 取得系统时间。
			int year = t.year;
			int month = t.month;
			int date = t.monthDay;
			return new DatePickerDialog(this, onDateSetListener, year, month, date);

		}
		return null;
		
	};
	
	public boolean isBefore(String s1,String s2){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(s1);
            Date dt2 = df.parse(s2);
            if (dt1.getTime() < dt2.getTime()) {
                return true;
            } 

        } catch (Exception exception) {
            exception.printStackTrace();
        }
		return false;
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_ONLINE_BOOK) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("预约成功!");
					finish();
					break;
				case 1:
					showToast("金额为空!");
					break;
				case 2:
					showToast("时间为空!");
					break;
				case 3:
					showToast("手机号码为空!");
					break;
				case 4:
					showToast("手机号码错误!");
					break;
				case 5:
					showToast("未登录!");
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
