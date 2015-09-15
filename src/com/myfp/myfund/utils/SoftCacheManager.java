package com.myfp.myfund.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class SoftCacheManager {
	private static SoftCacheManager softCacheManager;
	static SoftCacheManager getSoftInstance(){
		if(softCacheManager==null){
			softCacheManager=new SoftCacheManager();
		}
		return softCacheManager;
	}
	private Map<String, SoftReference<Bitmap>>  map=new HashMap<String, SoftReference<Bitmap>>();
	//��ͼƬ����������
	public void putBitmap(String url,Bitmap bitmap){
		map.put(url, new SoftReference<Bitmap>(bitmap));
	}
	//��ͼƬ����������ȡ��
	public Bitmap getBitmap(String url){
		Bitmap bitmap=null;
		if (map.get(url)!=null) {
			SoftReference<Bitmap> softReference=map.get(url);
			 bitmap=softReference.get();
		}
		return bitmap;
	}
	// ��ͼƬ�����������Ƴ�
	public void removeBitmap(String url){
		map.remove(url);
	}
}	
