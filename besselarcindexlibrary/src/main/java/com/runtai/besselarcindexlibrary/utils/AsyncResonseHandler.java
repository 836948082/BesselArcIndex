package com.runtai.besselarcindexlibrary.utils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class AsyncResonseHandler {
	protected static final int SUCCESS_MESSAGE = 0;
	private Handler handler;
	
	public AsyncResonseHandler(){
		if(Looper.myLooper() != null) {
            handler = new Handler(){
                public void handleMessage(Message msg){
                	AsyncResonseHandler.this.handleMessage(msg);
                }
            };
        };
	}
	
	protected Message obtainMessage(int responseMessage, Object response) {
        Message msg = null;
        if(handler != null){
            msg = this.handler.obtainMessage(responseMessage, response);
        }else{
            msg = new Message();
            msg.what = responseMessage;
            msg.obj = response;
        }
        return msg;
	}
	
	
	 protected void sendMessage(Message msg) {
        if(handler != null){
            handler.sendMessage(msg);
        } else {
            handleMessage(msg);
        }
	 }
	
	protected void handleMessage(Message msg){
		onSuccess((String) msg.obj);
    }
	
	void sendResponseMessage(String content){
		sendMessage(obtainMessage(SUCCESS_MESSAGE, content));
	}
	
	protected void onSuccess(String content){
		
	}
	
}
