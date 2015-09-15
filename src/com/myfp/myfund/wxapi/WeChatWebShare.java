package com.myfp.myfund.wxapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

public class WeChatWebShare {

	private static final String WX_APP_ID = "wx9d6082dfe3902d2d";
	private IWXAPI wxApi = null;

	private Bitmap thumb;

	public WeChatWebShare(Context ctx) {
		// 实例化
		wxApi = WXAPIFactory.createWXAPI(ctx, WX_APP_ID);

		wxApi.registerApp(WX_APP_ID);
	}

	/**
	 * 微信分享
	 * 
	 * @param ctx
	 *            ( Activity)
	 * @param url
	 * 
	 * @param flag
	 *            (0:分享到微信好友，1：分享到微信朋友圈)
	 */
	public void wechatShare(String url, String imgPath, String thumbUrl,
			String title, String descripton, int flag) {

		try {
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = url;
			WXMediaMessage msg = new WXMediaMessage(webpage);
			msg.title = title;
			msg.description = descripton;

			// 这里替换一张自己工程里的图片资源
			if (imgPath != null) {
				thumb = BitmapFactory.decodeFile(imgPath);
				msg.setThumbImage(thumb);
			} else if (thumbUrl != null) {
	             Bitmap bmp = BitmapFactory.decodeStream(new URL(thumbUrl).openStream());
	             thumb = Bitmap.createScaledBitmap(bmp, 150,
	 					150, true);
	 			bmp.recycle();
	 			msg.thumbData = bmpToByteArray(thumb, true);

			}

			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis());
			req.message = msg;
			req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
					: SendMessageToWX.Req.WXSceneTimeline;
			wxApi.sendReq(req);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;

		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;

			conn = (HttpURLConnection) myFileUrl.openConnection();

			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
