package com.myfp.myfund.api;

import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.utils.RndLog;



public class NetworkDataService {
	private static final String TAG = "NetworkServiceImpl";
	private ServerAsyncRequester mRequester;
	private static NetworkDataService networkDataService;

	/**
	 * Must new a network service implement on UI thread.
	 */
	NetworkDataService() {
		mRequester = new ServerAsyncRequester(new ServerInterface());
	}

	public static NetworkDataService getNetworkDataService() {

		if (networkDataService == null) {
			networkDataService = new NetworkDataService();
		}
		return networkDataService;

	}

	public void callServerInterface(ApiType api, RequestParams params,
			 OnDataReceivedListener listener) {
		RndLog.d(TAG, "callServerInterface. api=[" + api + "]");
		mRequester.request(api, params, listener);
	}

}
