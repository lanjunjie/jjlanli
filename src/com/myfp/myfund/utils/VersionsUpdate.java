package com.myfp.myfund.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;


import com.alibaba.fastjson.JSON;
import com.myfp.myfund.BaseActivity;
import com.myfp.myfund.OnDataReceivedListener;
import com.myfp.myfund.api.ApiType;
import com.myfp.myfund.api.RndDataApi;
import com.myfp.myfund.api.beans.GetVersionUpdate;
import com.myfp.myfund.ui.SettingActivity;

public class VersionsUpdate implements OnDataReceivedListener {

	private String url;
	private boolean isMain;
	public BaseActivity activity;
	private ProgressDialog pd; // 进度条对话框
	private String version,first,second,third;
	private List<GetVersionUpdate> update;
	public static BaseActivity initActivity=null;

	public VersionsUpdate(boolean isMain,BaseActivity activity) {
		this.isMain = isMain;
		this.activity = activity;
	}

	public void getVersion() {
		if (activity instanceof SettingActivity) {
			activity.showProgressDialog("正在检查");
		}
		RndDataApi.executeNetworkApi(ApiType.GET_VERSIONS, null, this);
	}

	public void getAPK() {
		RndDataApi.executeNetworkApi(ApiType.GET_APK, null, this);
	}

	@Override
	public void onReceiveData(ApiType api, String json) {
		try {
			if (api == ApiType.GET_VERSIONS) {
				if (json == null) {
					if (!isMain) {
						activity.disMissDialog();
						Builder builder = new Builder(activity);
						builder.setMessage("当前版本已经是最新版");
						builder.setTitle("提示");
						builder.setPositiveButton("确定", new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
						builder.create().show();
					}
					return;
				}
				activity.disMissDialog();
				
				//ByteArrayInputStream inputStream=new ByteArrayInputStream(json.getBytes("GBK"));
				//System.out.println("inputStream-=-=============>"+inputStream);
				//BufferedReader ioReader=new BufferedReader(new InputStreamReader(inputStream));
				//String readLine = ioReader.readLine();
				//String ns=new String(readLine.getBytes("UTF-8"),"GBK");
				//System.out.println("ns=-=-=-=-=-=-=-=-=-=-=>"+ns);
				//System.out.println("readLine============------------>"+readLine);
				//update = JSON.parseArray(readLine, GetVersionUpdate.class);
				//System.out.println("update================>"+update);
					
			
				update = JSON.parseArray(json, GetVersionUpdate.class);
								
				version = update.get(0).getAppMyfundVersion();
				first = update.get(0).getFirst();
				second = update.get(0).getSecond();
				third = update.get(0).getThird();
				
				String vson = getVersionName();

				if (!version.equals(vson)) {
					Builder builder = new Builder(activity);
					if(second.length()==0){
						builder.setMessage("有最新版本:"+version+",更新内容是："+"\n"+first+";"+"\n"+"是否下载最新版本？");
					}else if(third.length()==0){
						builder.setMessage("有最新版本:"+version+",更新内容是："+"\n"+first+";"+"\n"+second+";"+"\n"+"是否下载最新版本？");
					}else{
						builder.setMessage("有最新版本:"+version+",更新内容是："+"\n"+first+";"+"\n"+second+";"+"\n"+third+";"+"\n"+"是否下载最新版本？");
					}
					
					builder.setTitle("提示");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									getAPK();
									dialog.dismiss();
									// 设置你的操作事项
								}
							});
					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									// 设置你的操作事项
								}
							});
					builder.setCancelable(false);
					builder.create().show();
				} else {
					if (!isMain) {
						Builder builder = new Builder(activity);
						builder.setMessage("当前版本已经是最新版");
						builder.setTitle("提示");
						builder.setPositiveButton("确定", new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
						builder.create().show();
					}
				}
			} else if (api == ApiType.GET_APK) {
				if (json == null) {
					return;
				}
				JSONObject obj = new JSONObject(json);
				url = obj.getString("appMyfundApk");
				downLoadApk();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取当前程序的版本号
	 */
	public String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = activity.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				activity.getPackageName(), 0);
		return packInfo.versionName;
	}

	// 安装apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");// 编者按：此处Android应为android，否则造成安装不了
		activity.startActivity(intent);
	}

	/*
	 * 02 * 从服务器中下载APK 03
	 */
	protected void downLoadApk() {

		pd = new ProgressDialog(activity);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("正在下载更新");
		pd.setProgressNumberFormat("%1d KB/%2d KB");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = getFileFromServer(url, pd);
					sleep(1000);
					if (pd.isShowing()) {
						installApk(file);
					}
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public  File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 60 * 1000);
			// 获取到文件的大小
			pd.setMax(conn.getContentLength()/1000);
			InputStream is = conn.getInputStream();
			String ph = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/myfund";
			File file = new File(ph + File.separator + "myfund"+version+".apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1 && pd.isShowing()) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total/1000);
			}
			if (!pd.isShowing()) {
				file.delete();
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}
}
