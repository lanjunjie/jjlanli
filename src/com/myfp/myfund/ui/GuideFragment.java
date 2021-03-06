package com.myfp.myfund.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myfp.myfund.R;

/**
 * 启动页的Fragment
 * @author Max.Zhao
 *
 */
public class GuideFragment extends Fragment {
	
	private int index;
	private TextView tv_enter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guide, null, false);
		tv_enter = (TextView) view.findViewById(R.id.textView_guide_enter);
		switch (index) {
		case 0:
			view.setBackgroundResource(R.drawable.guide_1);
			break;
		case 1:
			view.setBackgroundResource(R.drawable.guide_2);
			break;
		case 2:
			//第三页的时候 绑定点击事件
			view.setBackgroundResource(R.drawable.guide_3);
			tv_enter.setVisibility(View.VISIBLE);
			tv_enter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(getActivity(),MyActivityGroup.class));
					getActivity().finish();
				}
			});
			break;
		default:
			break;
		}
		return view;
	}

}
