/**   
* @Title: BaseFragment.java
* @Package com.rndchina.cailifang
* @Description: TODO(��һ�仰�������ļ���ʲô)
* @author liangyao  
* @date 2014-8-6 ����1:10:06
* @version V1.0   
*/


package com.myfp.myfund;

import com.myfp.myfund.api.ApiType;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @ClassName: BaseFragment
 * @Description: TODO(Fragment基类)
 * @author liangyao
 * @date 2014-8-6 ����1:10:06
 * 
 */

public abstract class BaseFragment extends Fragment implements OnClickListener,OnDataReceivedListener{
	/**
	 * 
	 */
	protected abstract void onViewClick(View v);

	@Override
	public void onClick(View v) {
		onViewClick(v);
	}
	
	@Override
	public void onReceiveData(ApiType api, String json) {
		
	}

}
