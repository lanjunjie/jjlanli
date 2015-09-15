package com.myfp.myfund.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

public class BitmapUtils {

	
	
	private static final String TAG = "BitmapUtils";

	public static int estimateSampleSize(String filePath, int destWidth,
			int destHeight) {
		return estimateSampleSize(filePath, destWidth, destHeight, 0);
	}

	
	public static int estimateSampleSize(String filePath, int destWidth,
			int destHeight, int orientation) {
		if (filePath == null) {
			return 0;
		}

		if (destWidth <= 0 || destHeight <= 0) {
			return 0;
		}

		final int tw = destWidth;
		final int th = destHeight;
		int sw = 0;
		int sh = 0;

		final Options opts = new Options();
		opts.inJustDecodeBounds = true;

		try {
			BitmapFactory.decodeFile(filePath, opts);
		} catch (OutOfMemoryError e) {
			sw = 0;
			sh = 0;
		}

		/*
		 * Logger.debug("bitmap = [%-3d x %-3d], thumb = [%-3d x %-3d]",
		 * opts.outWidth, opts.outHeight, tw, th);
		 */
		sw = opts.outWidth;
		sh = opts.outHeight;

		if (orientation == 90 || orientation == 270) {
			sw = opts.outHeight;
			sh = opts.outWidth;
		}

		return Math.min(sw / tw, sh / th);
	}

	public static int estimateScaledHeight(String filePath, int destWidth) {
		if (filePath == null) {
			return 0;
		}

		final int tw = destWidth;
		int sw = 0;
		int sh = 0;

		final Options opts = new Options();
		opts.inJustDecodeBounds = true;

		try {
			BitmapFactory.decodeFile(filePath, opts);
		} catch (OutOfMemoryError e) {
			sw = 0;
			sh = 0;
		}

		/*
		 * Logger.debug("bitmap = [%-3d x %-3d], thumb = [%-3d x %-3d]",
		 * opts.outWidth, opts.outHeight, tw, th);
		 */
		sw = opts.outWidth;
		sh = opts.outHeight;

		if (sw == 0) {
			return 0;
		}

		return sh * tw / sw;
	}

	public static Bitmap rotateBitmap(Bitmap source, int degrees) {
		if (degrees != 0 && source != null) {
			Matrix m = new Matrix();

			m.setRotate(degrees, (float) source.getWidth() / 2,
					(float) source.getHeight() / 2);

			try {
				Bitmap rbitmap = Bitmap.createBitmap(source, 0, 0,
						source.getWidth(), source.getHeight(), m, true);
				if (source != rbitmap) {
					source.recycle();
					source = rbitmap;
				}
			} catch (OutOfMemoryError ex) {
			}
		}

		return source;
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, int destWidth,
			int destHeight) {
		if (bitmap == null) {
			return null;
		}

		if (destWidth <= 0 || destHeight <= 0) {
			return bitmap;
		}

		Bitmap newBitmap = bitmap;

		final int owidth = bitmap.getWidth();
		final int oheight = bitmap.getHeight();
		final int nwidth = destWidth;
		final int nheight = destHeight;

		try {
			if (owidth > nwidth) {
				if (oheight > nheight) {
					float scaleWidth = ((float) nwidth / owidth);
					float scaleHeight = ((float) nheight / oheight);
					if (scaleWidth > scaleHeight) {
						Bitmap tempBitmap = createScaledBitmap(bitmap,
								scaleWidth, owidth, oheight);
						if (tempBitmap != null) {
							newBitmap = createClippedBitmap(tempBitmap, 0,
									(tempBitmap.getHeight() - nheight) / 2,
									nwidth, nheight);
						}
					} else {
						Bitmap tempBitmap = createScaledBitmap(bitmap,
								scaleHeight, owidth, oheight);
						if (tempBitmap != null) {
							newBitmap = createClippedBitmap(tempBitmap,
									(tempBitmap.getWidth() - nwidth) / 2, 0,
									nwidth, nheight);
						}
					}

				} else {
					newBitmap = createClippedBitmap(bitmap,
							(bitmap.getWidth() - nwidth) / 2, 0, nwidth,
							oheight);
				}

			} else if (owidth <= nwidth) {
				if (oheight > nheight) {
					newBitmap = createClippedBitmap(bitmap, 0,
							(bitmap.getHeight() - nheight) / 2, owidth, nheight);
				}
			}
		} catch (OutOfMemoryError e) {

			newBitmap = bitmap;
		}

		return newBitmap;
	}

	private static Bitmap createScaledBitmap(Bitmap bitmap, float scale,
			int width, int height) {
		if (bitmap == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);

		return scaledBitmap;
	}

	private static Bitmap createClippedBitmap(Bitmap bitmap, int x, int y,
			int width, int height) {
		if (bitmap == null) {
			return null;
		}

		Bitmap newBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);

		return newBitmap;
	}

	public static boolean saveBitmap(Bitmap bitmap, String filename) {
		if (filename == null) {
			return false;
		}

		File file = new File(filename);

		return saveBitmap(bitmap, file);
	}

	public static boolean saveBitmap(Bitmap bitmap, File file) {
		if (bitmap == null || file == null) {
			return false;
		}

		boolean success = false;
		try {
			FileOutputStream out = new FileOutputStream(file);

			bitmap.compress(Bitmap.CompressFormat.JPEG,
					70, out);

			out.flush();
			out.close();

			success = true;
		} catch (IOException e) {

			success = false;
		}

		return success;
	}

	/**
	 * 是否可以压缩
	 * 
	 * @author Saifei
	 * @param picPath
	 * @return
	 */
	public static boolean canCompress(String picPath) {
		Options ops = new Options();
		ops.inJustDecodeBounds = true;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(picPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BitmapFactory.decodeStream(fis, null, ops);
		if (ops.outHeight > 960 || ops.outWidth > 720) {
			return true;
		}
		return false;
	}

	public static Bitmap scaleBitmap(String localPath, int maxWidth,
			int maxHeight, int minwidth, int minHeight) {

		final Options opts = new Options();
		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(localPath, opts);

		/*
		 * Logger.debug("bitmap = [%-3d x %-3d], thumb = [%-3d x %-3d]",
		 * opts.outWidth, opts.outHeight, tw, th);
		 */
		int orgwidth = opts.outWidth;
		int orgheight = opts.outHeight;

		int width = orgwidth;
		int height = orgheight;
		if (orgwidth > maxWidth) {
			float scaleWidth = ((float) maxWidth / orgwidth);

			width = maxWidth;
			height = (int) (orgheight * scaleWidth);

			if (height < minHeight) {

				height = minHeight;
			} else if (height > maxHeight) {
				height = maxHeight;
			}

		} else if (orgheight > maxHeight) {
			float scaleHeight = (float) (maxHeight / orgheight);
			height = maxHeight;
			width = (int) (orgwidth * scaleHeight);
			if (width < minwidth) {
				width = minwidth;
			} else if (width > maxWidth) {

				width = maxWidth;
			}
		}

		int sample = estimateSampleSize(localPath, width, height);

		opts.inSampleSize = sample;
		opts.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(localPath, opts);

		// DisplayImageOptions.Builder builder = new
		// DisplayImageOptions.Builder();
		// builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).considerExifParams(true)
		// .bitmapConfig(Config.RGB_565);
		// String uri = UriUtil.CheckUri(localPath);
		//
		// ImageSize imagesize = new ImageSize(maxWidth, maxHeight);
		// Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri,
		// imagesize,
		// builder.build());

		return bitmap;

	}
}
