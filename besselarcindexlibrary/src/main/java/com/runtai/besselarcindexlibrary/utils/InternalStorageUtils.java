package com.runtai.besselarcindexlibrary.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 内部存储文件读写 数据预载
 * @author LY
 *
 */
public class InternalStorageUtils {
	private static final String TAG = "InternalStorageUtils";
	private static ThreadPoolExecutor threadPool;
	private static int BUFFER_SIZE = 4096;

	private static class SingleHolder{
		private static final ThreadPoolExecutor fixedThreadPool=(ThreadPoolExecutor)Executors.newFixedThreadPool(1);
	}

	/**
	 * 读取内部存储数据  优先读取内部存储读不到再从Assets下的读数据
	 * @param context
	 * @param fileName
	 * @param asyncResonseHandler
	 */
	public static void asynReadInternalFile(final Context context,final String fileName,final AsyncResonseHandler asyncResonseHandler){
		threadPool = SingleHolder.fixedThreadPool;
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				String content = getContent(context, fileName);
				asyncResonseHandler.sendResponseMessage(content);
			}
		});
	}

	/**
	 * 存储到手机内部存储
	 * @param context
	 * @param is
	 * @param fileName
	 */
	private static void saveFile(Context context,InputStream is,String fileName){
		if (context != null) {
			try {
				File file = new File(context.getFilesDir().getPath().toString() + File.separator + fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				if (is != null) {
					FileUtils.writeFile(file, is);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}finally{
				if(is!=null){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 获取内部存储数据
	 * @param context
	 * @param fileName
	 * @return
	 */
	private static String getContent(Context context,String fileName){
		String content = null;
		try {
			if(context!=null){
				File file = new File(context.getFilesDir().getPath().toString() + File.separator + fileName);
				if (file != null && file.length() > 0) {
					content = FileUtils.readTextFile(file);
					Log.v(TAG, "read internal storage data success!");
				}
				if(content==null || "".equals(content)){
					content = FileUtils.readTextInputStream(context.getResources().getAssets().open(fileName));
					Log.v(TAG, "read assets data success!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
}
	
