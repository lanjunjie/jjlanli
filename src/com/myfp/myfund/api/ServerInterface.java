package com.myfp.myfund.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import u.aly.ap;

import com.alibaba.fastjson.JSON;
import com.myfp.myfund.api.ApiType.RequestMethod;
import com.myfp.myfund.utils.IOUtils;
import com.myfp.myfund.utils.RndLog;
import com.nostra13.universalimageloader.utils.IoUtils;


/**
 * 
 * 服务器接口
 * 
 * @author wqz
 * 
 */
public final class ServerInterface {

	private static final String TAG = "ServerInterface";
	
	private static final String SERVER_URL = "http://app.myfund.com:8484";
//	private static final String VERSION_URL = "http://223.203.189.179:8080";
	private static final String SMS_URL = "http://sms.myfund.com:8000";
	//private static final String HOLD_URL = "https://apptrade.myfund.com:8383";
	private static final String FUNDDEAL_URL = "https://apptrade.myfund.com:8383";
//	private Gson mGson;
	private static final String CEMETEEY_URL="http://app.myfund.com:8585";
	
	//我的测试域名
	private static final String MY_PROPERTY="http://10.20.32.57:8080";
	//支付 
	private static final String PAY_URL="http://mobiletest.yeepay.com";
	//配资支付接口
	private static final String GET_ANDPAY="http://www.myfund.com";
	
	private static final String GET_TEST="http://apptrade.myfund.com:8000";
	
	//private static final String GET_TEST="http://apptrade.myfund.com:9000";
	
	
	//private static final String GET_TESTTWO="http://10.20.25.196:8080";
	private static final String GET_TESTTWO="http://10.20.25.197:8080";
	
	//private static final String GET_TESTTWO="http://10.20.24.98:8080";
	private static final String GET_OPENCARD="http://10.20.30.6:8080";

	private NetworkHelper mHelper;

	public ServerInterface() {
//		mGson = new Gson();
		mHelper = new NetworkHelper();
	}

	public String request(final ApiType api, final RequestParams params)
			throws NetworkException {

		String url = SERVER_URL;
	
//		if( api == ApiType.GET_VERSIONS || api == ApiType.GET_APK){
//			url = VERSION_URL;
//		}
		if (api == ApiType.GET_CHECK_CODE || api == ApiType.GET_VERSIONS || api == ApiType.GET_APK
				||api==ApiType.GET_CHECK_CODETWO||api==ApiType.FUNDSWITH_CAPITAL
				||api==ApiType.GET_ADDFUTURES||api==ApiType.GET_SHOWDETAILS||api==ApiType.GET_FEATUREPRMHDL
				||api==ApiType.GET_FEATUREPRMHYC) {
			url = SMS_URL;
		}else if (api == ApiType.GET_DEALLOGIN || api == ApiType.GET_DEALINFOR
				|| api == ApiType.GET_DEALBUY || api == ApiType.GET_DEALREDEEM
				|| api == ApiType.GET_CANCELLATION
				|| api == ApiType.GET_DEALSEARCH ||  api == ApiType.GET_HOLD_DETAIL||
				api == ApiType.GET_DEALREDEEM_LIST ||api == ApiType.GET_ONLINEBANKINFOR
				||api == ApiType.GET_REMITBANKINFOR ||api == ApiType.GET_ORDER ||
				api == ApiType.GET_BANKPAY || api == ApiType.GET_CHANGEFORFUND ||
				api == ApiType.GET_FUNDTOTALINFOR || api == ApiType.GET_CHANGEFUND
				|| api == ApiType.GET_CANCELLATIONLIST || api == ApiType.GET_ENTURSTSEARCH
				|| api == ApiType.GET_HISTORYSEARCH || api == ApiType.GET_DTSEARCH
				|| api == ApiType.GET_DTAGREEMENT || api == ApiType.GET_DTPAY
				|| api == ApiType.GET_DTMANAGE || api == ApiType.GET_DTSTOP
				|| api == ApiType.GET_CHECKINFO || api == ApiType.GET_RANDOMBUM
				|| api == ApiType.GET_SENDMSG || api == ApiType.GET_RESETPASS
				|| api == ApiType.GET_ACCOUNTINFOR || api == ApiType.GET_QRYSMALLMONEY
				|| api == ApiType.GET_CHKSMALLMONEY || api == ApiType.GET_RISKQUESTION
				|| api == ApiType.GET_RATEFEE || api == ApiType.GET_RISKSUBMIT
				|| api == ApiType.GET_OPENACCOUNTSTATUS || api == ApiType.GET_OPENACCOUNTBANKS
				|| api == ApiType.GET_OPENACCOUNTADDRESS || api == ApiType.GET_OPENACCOUNT
				|| api == ApiType.GET_SMALLMONEY||api==ApiType.GET_MYPROPERTYMEMBER||
				api==ApiType.GET_MYPROPERTY||api==ApiType.GET_MEBERJUDGE
				||api==ApiType.GET_MEMBERINFORMATION||api==ApiType.GET_PURCHASEDETAILS
				||api==ApiType.GET_WITHDREWDEPOSIT
				||api==ApiType.GET_ACCOUNTINFO
				||api==ApiType.GET_CLIENT_MESSAGE||api==ApiType.GET_INQUIRY_FUND
				||api==ApiType.GET_CHANGE_PASSWORD||api==ApiType.GET_SET_FUND||api==ApiType.GET_SHOWDETAILSTWO
				) {
			url = FUNDDEAL_URL;
		}
		if (api==ApiType.GET_FEATURE||api==ApiType.GET_CEMETEEY||api==ApiType.GET_PLACEMENT
				|| api == ApiType.GET_FUNDINFOR
				|| api == ApiType.GET_HISTORYRATE
				|| api == ApiType.GET_EQUITYCHART
				|| api == ApiType.GET_FUNDMANAGER
				|| api == ApiType.GET_CURRENTPRODUCT
				|| api == ApiType.GET_CODE
				|| api == ApiType.GET_FUNDCOMPANY
				|| api == ApiType.GET_SELLINGPRODUCTS
				|| api == ApiType.GET_PRODUCTDETAIL
				|| api==ApiType.GET_SAVENAMETESTINFO
				|| api==ApiType.GET_TODETERMINETHEMEBEER
				|| api==ApiType.GET_JUDGEFNDS
				|| api==ApiType.GET_HOUSEKEEPERFLUX
				|| api==ApiType.GET_ANAPPOINTMENT
				|| api==ApiType.GET_PLACEMENTTWO
				|| api==ApiType.GET_PLACEMENTTWOT
				|| api==ApiType.GET_MYFUNDBANNER
				|| api==ApiType.GET_BANKTRANSFER
				) {
			 url=CEMETEEY_URL;
		}
		if (api==ApiType.GET_ANDPAY) {
			url=GET_ANDPAY;
		}
		if (api==ApiType.GET_OPENACCOUNT2||api==ApiType.GET_SMALLMONEY2||api==ApiType.GET_STEPVERIFICATION||api==ApiType.GET_DEALSEARCHTWO
				||api==ApiType.GET_HOLD_DETAILTWO||api==ApiType.GET_DEALREDEEM_LISTTWO||api==ApiType.GET_RISKSUBMITTWO
				||api==ApiType.GET_INQUIRY_FUNDTWO||api==ApiType.GET_ENTURSTSEARCHTWO||api==ApiType.GET_HISTORYSEARCHTWO
				||api==ApiType.GET_RISKQUESTIONTWO||api==ApiType.GET_DEALLOGINTWO||api==ApiType.GET_QRYSMALLMONEYTWO
				||api==ApiType.GET_DEALSEARCHONETWO||api==ApiType.GET_DEALREDEEMTWO||api==ApiType.GET_DEALREDEEM_LISTTWO
				||api==ApiType.GET_CHANGEFUNDTWO||api==ApiType.GET_CANCELLATIONLISTTWO||api==ApiType.GET_CANCELLATIONTWO
				||api==ApiType.GET_ONLINEBANKINFORTWO||api==ApiType.GET_REMITBANKINFORTWO||api==ApiType.GET_ORDERTWO
				||api==ApiType.GET_BANKPAYTWO||api==ApiType.GET_SET_FUNDTWO||api==ApiType.GET_DTMANAGETWO
				||api==ApiType.GET_DTAGREEMENTTWO||api==ApiType.GET_DTPAYTWO||api==ApiType.GET_DTSTOPTWO||api==ApiType.GET_CHANGEFORFUNDTWO
				||api==ApiType.GET_FUNDTOTALINFORTWO||api==ApiType.GET_DTSEARCHTWO||api==ApiType.UPDATE_MOBILEID||api==ApiType.GET_MYPROPERTYNEW
				||api==ApiType.GET_SHOWDETAILSTWONEW||api==ApiType.GET_MEMBERINFORMATIONNEW||api==ApiType.GET_MEBERJUDGENEW||api==ApiType.GET_PURCHASEDETAILSNEW
				||api==ApiType.GET_BANKCARDMESSAGENEW||api==ApiType.GET_JUDGEWITHDRAWALDEGREENEW||api==ApiType.GET_WITHDRaAWCASHBACKRECORDNEW
				||api==ApiType.GET_FINDMOBILENO||api==ApiType.GET_BUTLERTONG||api==ApiType.GET_JUDGEWITHDRAWALDEGREE||api==ApiType.GET_WITHDRaAWCASHBACKRECORD
				||api==ApiType.GET_BANKCARDMESSAGE||api==ApiType.GET_WITHDREWDEPOSITNEW||api==ApiType.GET_OPENCARD||api==ApiType.GET_SMSOPENCARD) {
			url=GET_TEST;
			//url=GET_TESTTWO;
			//url=GET_OPENCARD;
		}
	
		RndLog.d(TAG, "SERVER:"+url);
		HttpResponse response = getResponseByApi(url, api, params);

		if (response != null) {// response == null�?可能是无网络引起

			HttpEntity entity = response.getEntity();
			StatusLine status = response.getStatusLine();
			if (HttpStatus.SC_OK == status.getStatusCode()) {
				if (entity != null) {
					try {
						String json = "";

						InputStream is = entity.getContent();
						Header contentEncoding = response
								.getFirstHeader("Content-Encoding");

						if (contentEncoding != null
								&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
							RndLog.d(TAG, "https response has be gziped!");
							is = new GZIPInputStream(
									new BufferedInputStream(is));

						}
						 if (url.contains("https")) {
							json = IOUtils.toString(is, "GB2312");
						}
						 else if(url.contains("http://apptrade.myfund.com:8000")) {
							json = IOUtils.toString(is, "GB2312");
						}
						else {
							json = IOUtils.toString(is, "UTF-8");
						}
					
						
				/*		if (url.contains("https")) {

							json = IOUtils.toString(is, "GB2312");
						}
						else if (url.equals("http://sms.myfund.com:8000")) {
							json = IOUtils.toString(is, "GB2312");
						} 
						 else if (url.equals("http://10.20.24.168:8080")) {
							json = IOUtils.toString(is, "GB2312");
						}
						else{
							json = IOUtils.toString(is, "UTF-8");
 					    }
						*/
				/*		if (url.contains("https")) {

							json = IOUtils.toString(is, "GB2312");
						}else if (url.contains("https")) {
							json = IOUtils.toString(is, "UTF-8");
 					    } 
					*/
						RndLog.d(TAG, "request. json.length = " + json);
						return json;
					} catch (Exception e) {
//						BzLog.e(TAG, e.getMessage(), e);
						e.printStackTrace();
						throw new NetworkException(e);
					} finally {
						try {
							entity.consumeContent();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return null;
			} else {
				int statusCode = status.getStatusCode();
				RndLog.e(TAG, "HTTP CODE :"+statusCode);
				throw new NetworkException(status.getStatusCode(),
						status.getReasonPhrase());
			}
		}else{
			//执行过程中产生异常
			return null;
		}
	}

	/**
	 * 修改为根据result类型返回结果
	 * @param json
	 * @param clazz
	 * @return
	 * @throws JSONException 
	 */
	private ResponseResult parseJson(String json,
			Class<? extends ResponseResult> clazz) throws JSONException {

		JSONObject obj = new JSONObject(json);
		String status = obj.getString("code");
		//从data中取结果的前提是 1.成功   2.Class是ResponseResult的子类
		if("1".equals(status)){
			//成功
				ResponseResult res = JSON.parseObject(json, clazz);
				return res;
		}else{
			ErrorResult err = JSON.parseObject(json, ErrorResult.class);
			return err;
		}
	}

	/**
	 * 通过参数返回http响应
	 * @param url
	 * @param api
	 * @param params
	 * @return
	 */
	private HttpResponse getResponseByApi(String url, ApiType api,
			RequestParams params) {
		
		//appendSign(api,params);
		
		try {
			//TODO 判断api类型
			if(api.getRequestMethod() == RequestMethod.POST){
				return mHelper.postFile(url + api.getOpt(), params);
			}else if(api.getRequestMethod() == RequestMethod.FILE){
				return mHelper.postFile(url + api.getOpt(), params);
			}else{
				return mHelper.performGet(url + api.getOpt(), params);
//				return mHelper.performPost(url + api.getOpt(), params);
			}
		} catch (NetworkException e) {
			RndLog.e(TAG, e.getMessage());
			e.printStackTrace();
			return null;
		}

	}


	public static Class<? extends ResponseResult> getJsonClassByApi(ApiType api) {
		//TODO 返回api对应的CLASS
		return api.getClazz();
	}

}
