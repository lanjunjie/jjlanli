package com.myfp.myfund.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;
import com.myfp.myfund.ui.view.CreateBmpFactory;
import com.myfp.myfund.ui.view.HeadImageView;

public class PersonalInfoActivity extends BaseActivity {

	private EditText  et_email;
	private TextView tv_sex, tv_phone;
	private String mobile,userName,name,sex,idCard,email;
	private String displayName;
	private Editor editor;
	private TextView et_identityCard,te_account,et_name;
	private Button baocun;
	private String depositacctName;
	


	
	@Override
	protected void setContentView() {
		setContentView(R.layout.new_activity_person);

		 userName = App.getContext().getUserName();
		 mobile = App.getContext().getMobile();
		 mobile = getIntent().getStringExtra("Mobile");
		 idCard = App.getContext().getIdCard();
		 idCard = getIntent().getStringExtra("IDCard");
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName);
		
		execApi(ApiType.GET_USER_INFO, params);
		
	}

	@Override
	protected void initViews() {
		setTitle("个人资料");
	
		baocun = (Button) findViewById(R.id.button_personal_save1);
		et_identityCard = (TextView) findViewById(R.id.new_et_personal_identityCard1);
		et_email = (EditText) findViewById(R.id.new_et_personal_email1);
		tv_phone=(TextView) findViewById(R.id.new_textView_personal_phone1);
		tv_sex = (TextView) findViewById(R.id.new_personal_sex);
		baocun.setOnClickListener(this);
		tv_sex.setOnClickListener(this);

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.new_personal_sex:
			// 性别
			Intent intent = new Intent(this,SexChoiceActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.button_personal_save1:
			// 保存
			saveInfo();
			break;
/*		case R.id.textView_personal_phone:
			// 手机
			Intent mIntent = new Intent(this,ChangeMobileActivity.class);
			startActivityForResult(mIntent,2);
			break; */
		default:
			break;
		}
	}

	private void saveInfo() {

		sex = tv_sex.getText().toString();
		idCard = et_identityCard.getText().toString();
		email = et_email.getText().toString();
	
		RequestParams params = new RequestParams(this);
		params.put("UserName", userName.trim());
		params.put("Realname", name.trim());
		params.put("Mobile", mobile.trim());
		params.put("Sex", sex.trim());
		params.put("IDCard", idCard.trim());
		params.put("Email", email.trim());
		execApi(ApiType.UPDATE_USER_INFO, params);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == 1) {
			sex = intent.getStringExtra("Sex");
			tv_sex.setText(sex);
		}else if (resultCode == 2) {
			mobile = intent.getStringExtra("Mobile");
			
			tv_phone.setText(mobile);
		}

	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_USER_INFO) {
				JSONObject obj = array.getJSONObject(0);
				idCard = obj.getString("IDCard");
				
				et_identityCard.setText(idCard);
				et_email.setText(obj.getString("Email"));
				tv_sex.setText(obj.getString("Sex"));
				userName=obj.getString("UserName");
				name=obj.getString("DisplayName");
				if (mobile==null) {
				mobile = obj.getString("Mobile");
					
				}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
				if (!TextUtils.isEmpty(mobile)) {
					tv_phone.setText(mobile);
				}else {
					tv_phone.setText("未绑定");
					tv_phone.setTextColor(Color.GRAY);
				}
			} else if (api == ApiType.UPDATE_USER_INFO) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					App.getContext().setIdCard(idCard);
					SharedPreferences sPreferences = getSharedPreferences("Setting",
							MODE_PRIVATE);
					Editor editor = sPreferences.edit();
					editor.putString("IDCard", idCard);
					showToast("修改成功！");
					finish();
					break;
				case 1:
					showToast("手机号码错误！");
					break;
				case 2:
					showToast("身份证格式错误！");
					break;
				case 3:
					showToast("邮箱格式错误！");
					break;
				case 4:
					showToast("姓名必须为中文真实姓名！");
					break;
					
				default:
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
