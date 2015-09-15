package com.myfp.myfund.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageCacheManager implements ImageCache{
    List<String> list=new ArrayList<String>();
	private static ImageCacheManager imageCacheManager;
	private SoftCacheManager softCacheManager=SoftCacheManager.getSoftInstance();
	public static ImageCacheManager getInstance(){
		if (imageCacheManager==null) {
			imageCacheManager=new ImageCacheManager();
		}
		
		return imageCacheManager;
	}
	//���һ��lrucache
	LruCache<String, Bitmap> lruCache =new LruCache<String, Bitmap>(4*1024*1024){

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			if (evicted) {
				//�ϵ���������
				//oldValue.recycle();
				//oldValue=null;
				//���뵽�?����
				softCacheManager.putBitmap(key, oldValue);
			}
		}

		@Override
		protected int sizeOf(String key, Bitmap value) {
			return value.getRowBytes()*value.getHeight();
		}
		
	};
	@Override
	public Bitmap getBitmap(String url) {
		if (lruCache.get(url)==null) {
			if (softCacheManager.getBitmap(url)!=null) {
				lruCache.put(url,softCacheManager.getBitmap(url));
				softCacheManager.removeBitmap(url);
			}
		}
		return lruCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		lruCache.put(url, bitmap);
	}

}
