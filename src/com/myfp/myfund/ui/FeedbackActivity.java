package com.myfp.myfund.ui;


import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.myfp.myfund.App;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RequestParams;

public class FeedbackActivity extends BaseActivity {

	private EditText et_comment,et_mobile;
	private String userName, comment;
	
	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_feedback);
	}

	@Override
	protected void initViews() {
		setTitle("意见反馈");
		
		et_comment = (EditText) findViewById(R.id.editText_feedback_content);
		et_mobile = (EditText) findViewById(R.id.editText_feedback_mobile);
		findViewAddListener(R.id.bt_feedback_submit);

	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_feedback_submit:
			comment = et_comment.getText().toString();
			String mobile = et_mobile.getText().toString();
			if (TextUtils.isEmpty(comment)) {
				showToast("评论内容不能为空");
				return;
			}
//			userName = App.getContext().getUserName();
//			if (userName == null) {
//				showToast("请先登录");
//				startActivity(LoginActivity.class);
//				return;
//			}

			RequestParams params = new RequestParams(this);
			params.put("Mobile", mobile);
			params.put("FundReview", comment);
			execApi(ApiType.GET_CUS_SUGGEST, params);
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		if (json == null) {
			showToast("请求失败!");
			return;
		}
		try {
			JSONArray array = new JSONArray(json);
			if (api == ApiType.GET_CUS_SUGGEST) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("提交成功!");
					finish();
					break;
				case 1:
					showToast("手机号码错误!");
					break;
				case 2:
					showToast("意见内容为空!");
					break;
				case 3:
					showToast("系统参数错误!");
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
