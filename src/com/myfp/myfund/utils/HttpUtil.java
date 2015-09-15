package com.myfp.myfund.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpUtil {

	public static String sendGet(String url) {
		HttpClient client=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				return EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getSteam(String url) {
		HttpClient client=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				return response.getEntity().getContent();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
