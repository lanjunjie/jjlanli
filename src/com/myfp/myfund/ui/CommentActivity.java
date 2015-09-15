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

public class CommentActivity extends BaseActivity {

	private EditText et_comment;
	private String fundCode,fundName, userName, comment;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_comment);

		fundCode = getIntent().getStringExtra("FundCode");
		fundName = getIntent().getStringExtra("FundName");
		
	}

	@Override
	protected void initViews() {
		setTitle("发表评论");

		et_comment = (EditText) findViewById(R.id.editText_comment);
		findViewAddListener(R.id.bt_comment_submit);
	}

	@Override
	protected void onViewClick(View v) {
		switch (v.getId()) {
		case R.id.bt_comment_submit:
			comment = et_comment.getText().toString();
			if (TextUtils.isEmpty(comment)) {
				showToast("评论内容不能为空");
				return;
			}
			userName = App.getContext().getUserName();
			if (TextUtils.isEmpty(userName)) {
				showToast("请先登录");
				startActivity(MyMeansActivity.class);
				return;
			}

			RequestParams params = new RequestParams(this);
			params.put("UserName", userName);
			params.put("FundCode", fundCode);
			params.put("FundName", fundName);
			params.put("FundReview", comment);
			execApi(ApiType.GET_REVIEW_UPDATE, params);
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
			if (api == ApiType.GET_REVIEW_UPDATE) {
				int result = array.getJSONObject(0).getInt("ReturnResult");
				switch (result) {
				case 0:
					showToast("提交成功!");
					finish();
					break;
				case 1:
					showToast("评论内容为空!");
					break;
				case 2:
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
