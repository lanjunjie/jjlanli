package com.myfp.myfund.api;

import android.os.AsyncTask;
import android.util.Log;

import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.OnDataReceivedListener.OnDataReceivedListener2;
import com.myfp.myfund.utils.RndLog;


final class ServerAsyncRequester {
	private static final String TAG = "ServerAsyncRequester";
	private ServerInterface mServerInterface;

	/***********************************************
	 * Must new a ServerAsyncRequester on UI thread.
	 * 
	 * @param serverInterface
	 */
	ServerAsyncRequester(ServerInterface serverInterface) {
		mServerInterface = serverInterface;

	}

	private class AsyncRequester extends
			AsyncTask<Object, Void, String> {
		OnDataReceivedListener mResponseListener;
		RequestParams reqParams;
		private ApiType api;
		
		@Override
		protected String doInBackground(Object... params) {
			api = (ApiType) params[0];
			reqParams = (RequestParams) params[1];
			mResponseListener = (OnDataReceivedListener) params[2];
			RndLog.d(TAG, "doInBackground");
			try {
				return mServerInterface.request(api, reqParams);
			} catch (final NetworkException e) {
				RndLog.e(TAG, e.toString());
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			RndLog.d("api", "listener :"+mResponseListener.getClass().getName());
			 Log.e(TAG, "response  dd: "+result);
			if(mResponseListener instanceof OnDataReceivedListener2){
				OnDataReceivedListener2 listener = (OnDataReceivedListener2) mResponseListener;
				listener.onReceiveData(api, reqParams, result);
			}else{
				mResponseListener.onReceiveData(api,result);
			}
		}
	}

	public void request(ApiType api, RequestParams params,
			 OnDataReceivedListener listener) {
		RndLog.d(TAG, "request. api=" + api);
		 Log.e(TAG, "request  dd: "+params);
		new AsyncRequester().execute(api, params, listener);
	}

}