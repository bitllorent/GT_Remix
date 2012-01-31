package com.gtremix.controllers;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gtremix.models.GTR_Model;
import com.gtremix.models.M;
import com.gtremix.views.GTR_Activity;

public class GTR_Controller {
	
	public static final String TAG = "GTR_Controller: ";
	private static GTR_Activity currentActivity;	
	
	public static void setCurrentActivity(GTR_Activity activity) {
		currentActivity = activity;
	}
	
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) 	{
			Log.d(TAG, "Message recieved");
			switch(msg.what){
				case M.MESSAGE_INIT_MEDIA_PATH:
					Message new_msg = Message.obtain(GTR_Model.messageHandler, msg.what, msg.obj); 
		        	new_msg.sendToTarget();
		        	Log.d(TAG, "Message sent");
					break;
				case M.MESSAGE_UPDATE:
					currentActivity.update();
				default:break;
			}
		}
	};
}
