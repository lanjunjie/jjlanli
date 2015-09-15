package com.myfp.myfund.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.R.string;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.utils.SpringProgressView;

public class FeatureDeatailedActivity extends BaseActivity {
	private SpringProgressView progressView;
	private TextView tex_invest_bfb;
	private TextView tex_invest_dildine;
	private TextView tex_invest_quota;
	private TextView tex_invest_rate;
	private TextView tex_invest_phase;
	private TextView tex_invest_rise;
	private TextView tex_invest_arrive;
	private TextView tex_invest_sec;
	private TextView tex_invest_clo;
	private TextView tex_invest_wt;
	private TextView tex_invest_ze;
	private Intent intent;
	private TextView tex_firm_name;
	public static FeatureDeatailedActivity instance = null;
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_feature_deatailed);
		instance=this;
		intent = getIntent();
		RequestParams params=new RequestParams(this);
		params.put("id", getIntent().getStringExtra("Id"));
		execApi(ApiType.GET_SALESPRODUCT, params);
		
	}
	@Override
	protected void initViews() {
		setTitle("特色固收");
		tex_firm_name = (TextView) findViewById(R.id.tex_firm_name);
		tex_invest_bfb = (TextView) findViewById(R.id.tex_invest_bfb);
		tex_invest_dildine = (TextView) findViewById(R.id.tex_invest_dildine);
		tex_invest_quota = (TextView) findViewById(R.id.tex_invest_quota);
		tex_invest_rate = (TextView) findViewById(R.id.tex_invest_rate);
		tex_invest_phase = (TextView) findViewById(R.id.tex_invest_phase);
		tex_invest_rise = (TextView) findViewById(R.id.tex_invest_rise);
		tex_invest_arrive = (TextView) findViewById(R.id.tex_invest_arrive);
		tex_invest_sec = (TextView) findViewById(R.id.tex_invest_sec);
		tex_invest_clo = (TextView) findViewById(R.id.tex_invest_clo);
		tex_invest_wt = (TextView) findViewById(R.id.tex_invest_wt);
		tex_invest_ze= (TextView) findViewById(R.id.tex_invest_ze);
		tex_invest_dildine.setText(intent.getStringExtra("Term")+"天");
		tex_invest_quota.setText(intent.getStringExtra("Sdlowlimit")+"万");
		tex_invest_rate.setText(intent.getStringExtra("Smodel"));
		tex_invest_phase.setText(intent.getStringExtra("Sspec"));
		String sdstartdate = intent.getStringExtra("Sdstartdate");
		String startdate = sdstartdate.substring(0, 10);
		tex_invest_rise.setText(startdate);
		String sdoverdate = intent.getStringExtra("Sdoverdate");
		String verdate = sdoverdate.substring(0, 10);
		tex_invest_arrive.setText(verdate);
		tex_invest_sec.setText(intent.getStringExtra("Security"));
		tex_invest_clo.setText(intent.getStringExtra("Dalaceway"));
		findViewAddListener(R.id.but_invest_ton);
		findViewAddListener(R.id.tex_invest_wt);
		tex_firm_name.setText(intent.getStringExtra("Sname"));
		tex_invest_ze.setText(intent.getStringExtra("Productscale")+"万");
		progressView = (SpringProgressView) findViewById(R.id.spring_progress_view);
		progressView.setMaxCount(100.0f);
		progressView.getMaxCount();
		progressView.getCurrentCount();
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.but_invest_ton:
			if (App.getContext().getUserName()==null||App.getContext().getUserName().equals("")) {
				
				Intent intent=new Intent(this, MyMeansActivity.class);
				startActivity(intent);
			}else {
				Intent intent=new Intent(this,PurchaseHardActivity.class);
				intent.putExtra("Sname", getIntent().getStringExtra("Sname"));
				intent.putExtra("Sdlowlimit", getIntent().getStringExtra("Sdlowlimit"));
				intent.putExtra("id", getIntent().getStringExtra("Id"));
				intent.putExtra("form", getIntent().getStringExtra("form"));
				startActivity(intent);
			}
			break;
		case R.id.tex_invest_wt:
			Intent intent1 =new Intent(this,QustionActivity.class);
			intent1.putExtra("type", getIntent().getStringExtra("form"));
			intent1.putExtra("name", "常见问题");
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
	@Override
	public void onReceiveData(ApiType api, String json) {
		if (api==ApiType.GET_SALESPRODUCT) {
			try {
				org.json.JSONArray Array=new org.json.JSONArray(json);
				String LevelCount = Array.getJSONObject(0).getString("LevelCount");
				double pasd = Double.parseDouble(LevelCount);
				double pard = Double.parseDouble(getIntent().getStringExtra("Productscale"));
				
//				double usn=pasd;
				double nsu=pasd;
				String valueOf = String.valueOf(nsu);
				String substring = valueOf.substring(0, valueOf.lastIndexOf("."));
				double paed=Double.parseDouble(substring);
				int parseInt = Integer.parseInt(substring);
				progressView.setCurrentCount(parseInt);
				tex_invest_bfb.setText(substring+"万"+"("+(paed/pard)*100+"%"+")");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}



}
