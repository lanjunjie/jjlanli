package com.myfp.myfund.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.R;

public class BankIdActivity extends BaseActivity{
	EditText et_bankid_banknum;
	@Override
	protected void setContentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_bankid);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		setTitle("银行卡号");
		et_bankid_banknum = (EditText)findViewById(R.id.et_bankid_banknum);
		findViewAddListener(R.id.bt_bankid_confirm);
		
	}

	@Override
	protected void onViewClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_bankid_confirm:
			if(et_bankid_banknum.getText().toString().length() == 0){
				showToast("请填写银行卡号");
				return;
			}else{
				if(et_bankid_banknum.getText().toString().contains(" ")){
					showToast("银行卡号格式不正确");
					return;
				}else{
					if(TextUtils.isDigitsOnly(et_bankid_banknum.getText().toString())){
						Bundle bundle = new Bundle();
						bundle.putString("BankId", et_bankid_banknum.getText().toString());
						Intent intent = new Intent(
								BankIdActivity.this,
								BindingBankActivity.class);
						intent.putExtras(bundle);
						setResult(1, intent);
						finish();
					}else{
						showToast("银行卡号格式不正确");
						return;
					}
					
				}
			}
			
			
			break;

		default:
			break;
		}
		
	}

}
