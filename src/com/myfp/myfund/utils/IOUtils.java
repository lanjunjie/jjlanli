package com.myfp.myfund.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.os.Environment;
import android.text.TextUtils;

public class IOUtils {

	private static final int STREAM_BUFFER_SIZE = 8192;

	public static final String APP_NAME = "meifa";

	private static final File WHISPER_EXT_DIR = new File(
			Environment.getExternalStorageDirectory() + "/" + APP_NAME);

	public static final File WHISPER_EXT_DIR_FOT_AUDIO = new File(
			WHISPER_EXT_DIR, "/.record");

	private static final File WHISPER_EXT_DIR_FOT_CAMERA = new File(
			WHISPER_EXT_DIR, "/.camera");

	private static final File WHISPER_EXT_DIR_USER_HEAD_ICON = new File(
			WHISPER_EXT_DIR, "/.userhead/middle");

	private static final File WHISPER_EXT_DIR_USER_BIG_HEAD_ICON = new File(
			WHISPER_EXT_DIR, "/.userhead/big");
	private static final File WHISPER_EXT_DIR_USER_SMALL_HEAD_ICON = new File(
			WHISPER_EXT_DIR, "/.userhead/small");

	private static final File WHISPER_EXT_DIR_PIC_CACHE = new File(
			WHISPER_EXT_DIR, ".imageloaderCache");

	private static final File BZ_PRIVETE_CACHE = new File(WHISPER_EXT_DIR,
			".privatespace");

	private static final File BZ_SAVE = new File(WHISPER_EXT_DIR, "bz");

	/**
	 * 判断SD卡是否存�?
	 * @return
	 */
	public static final boolean isSDCardExists() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static File getPrivateCacheDir() {

		if (!BZ_PRIVETE_CACHE.exists()) {
			BZ_PRIVETE_CACHE.mkdirs();
		}

		return BZ_PRIVETE_CACHE;
	}

	public static File getUserHeadIconDir() {

		if (!WHISPER_EXT_DIR_USER_HEAD_ICON.exists()) {
			WHISPER_EXT_DIR_USER_HEAD_ICON.mkdirs();
		}
		return WHISPER_EXT_DIR_USER_HEAD_ICON;

	}

	public static File getUserBigHeadIconDir() {
		if (!WHISPER_EXT_DIR_USER_BIG_HEAD_ICON.exists()) {
			WHISPER_EXT_DIR_USER_BIG_HEAD_ICON.mkdirs();
		}
		return WHISPER_EXT_DIR_USER_BIG_HEAD_ICON;
	}

	public static File getImageLoaderCacheDir() {
		if (!WHISPER_EXT_DIR_PIC_CACHE.exists()) {
			WHISPER_EXT_DIR_PIC_CACHE.mkdirs();
		}
		return WHISPER_EXT_DIR_PIC_CACHE;
	}

	public static File getUserSmallHeadIconDir() {
		if (!WHISPER_EXT_DIR_USER_SMALL_HEAD_ICON.exists()) {
			WHISPER_EXT_DIR_USER_SMALL_HEAD_ICON.mkdirs();
		}
		return WHISPER_EXT_DIR_USER_SMALL_HEAD_ICON;
	}

	/**
	 * 文件保存路径
	 * @return
	 */
	public static File getMFSaveDir() {
		if (!BZ_SAVE.exists()) {
			BZ_SAVE.mkdirs();
		}
		return BZ_SAVE;
	}


	// TODO 可以添加回调
	/*public static void savaImageLocalStorage(final String uri) {
		Application.getInstance().runInBackground(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean result = false;
				File temFile = new File(getBzSaveDir(), System
						.currentTimeMillis() + ".jpg");
				try {
					Bitmap bitmap = ImageLoader.getInstance()
							.loadImageSync(uri);
					BzUtil.saveBitmap(bitmap, temFile.getAbsolutePath());
					result = true;
				} catch (Exception e) {
					if (temFile.exists()) {
						temFile.delete();
					}
				}
				final boolean r = result;
				Application.getInstance().runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (r) {
						}
					}
				});
			}

		});

//		return result;

	}*/


	public static void writeTo(InputStream is, OutputStream os)
			throws IOException {

		try {
			byte[] buffer = new byte[STREAM_BUFFER_SIZE];
			int rlen = 0;
			while ((rlen = is.read(buffer, 0, STREAM_BUFFER_SIZE)) != -1) {
				os.write(buffer, 0, rlen);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	// public static void writeTo(InputStream is, OutputStream os, long length,
	// NetworkTask task) throws IOException, CancelException {
	//
	// float percent = 0;
	// long pos = 0;
	//
	// try {
	// byte[] buffer = new byte[STREAM_BUFFER_SIZE];
	// int rlen = 0;
	// while ((rlen = is.read(buffer, 0, STREAM_BUFFER_SIZE)) != -1) {
	// if (task.isCancel) {
	// throw new CancelException("TaskId: " + task.mTaskId
	// + " canceled!");
	// }
	//
	// os.write(buffer, 0, rlen);
	//
	// pos += rlen;
	//
	// if (length > 0) {
	// percent = ((float) pos / (float) length) * 100f;
	// task.doProgress(pos, length, (int) percent);
	// }
	// }
	// } finally {
	// if (is != null) {
	// try {
	// is.close();
	// } catch (IOException e) {
	// }
	// }
	// if (os != null) {
	// try {
	// os.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	// }

	/**
	 * Using this method to get directory object on external storage.
	 */
	public static File getExternalDir() {
		if (!WHISPER_EXT_DIR.exists()) {
			WHISPER_EXT_DIR.mkdirs();
		}
		return WHISPER_EXT_DIR;
	}

	public static File getExternalDirForCamera() {
		if (!WHISPER_EXT_DIR_FOT_CAMERA.exists()) {
			WHISPER_EXT_DIR_FOT_CAMERA.mkdirs();
		}
		return WHISPER_EXT_DIR_FOT_CAMERA;
	}

	public static File getCameraTempFile() {
		if (!WHISPER_EXT_DIR_FOT_CAMERA.exists()) {
			WHISPER_EXT_DIR_FOT_CAMERA.mkdirs();
		}
		return new File(WHISPER_EXT_DIR_FOT_CAMERA, System.currentTimeMillis()+".jpg");
		// return WHISPER_EXT_DIR_FOT_CAMERA+;
	}

	public static void clearExternalFiles() {
		clearDir(WHISPER_EXT_DIR);
	}

	/**
	 * Clear camera files on external storage.
	 */
	public static void clearExternalCameraFiles() {
		clearDir(WHISPER_EXT_DIR_FOT_CAMERA);
	}

	private static void clearDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {

					clearDir(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
	}


	/**
	 * 删除指定语音文件
	 * 
	 * @param fileName
	 */
	public static boolean deleteFile(String fileName) {
		if (!TextUtils.isEmpty(fileName)) {
			File audioFile = new File(fileName);
			if (audioFile.exists()) {
				return audioFile.delete();
			}
		}
		return false;
	}

	public static String getAudioRecordFilePath() {

		if (!WHISPER_EXT_DIR_FOT_AUDIO.exists()) {
			WHISPER_EXT_DIR_FOT_AUDIO.mkdirs();
		}

		return WHISPER_EXT_DIR_FOT_AUDIO + File.separator
				+ System.currentTimeMillis() + ".amr";

	}

	/**
	 * 删除�?��语音
	 */
	public static void clearExternalAudio() {
		clearDir(WHISPER_EXT_DIR_FOT_AUDIO);
	}

	public static void clearExternalCommunity() {
		clearDir(WHISPER_EXT_DIR_FOT_CAMERA);
	}

	public static String getImageFileName(String url) {
		String filename = "";
		String[] temSplit = url.split("/");
		if (temSplit.length > 0) {
			filename = temSplit[temSplit.length - 1];
		}

		return filename;

	}




	public static boolean isLocFileExist(String filePath) {

		File file = new File(filePath);

		if (file != null && file.exists() && file.length() > 0) {

			return true;
		}

		return false;

	}

	public static String toString(InputStream is, String charset) {

		BufferedReader reader = null;
		StringBuilder buffer = new StringBuilder();
		try {

			reader = new BufferedReader(new InputStreamReader(is, charset));

			String line = reader.readLine();

			while (!TextUtils.isEmpty(line)) {

				buffer.append(line);
				line = reader.readLine();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return buffer.toString();
	}

	public static enum ImageCacheType {

		USER_HEAD_SMALL_ICON, USER_HEAD_BIG_ICON, USER_HEAD_ICON, USER_COMMUNITY, SINGLE_CHAT, USER_COMMUNITY_TOP, USER_COMMUNITY_MIDDLE

	}

}