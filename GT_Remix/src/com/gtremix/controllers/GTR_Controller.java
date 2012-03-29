package com.gtremix.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gtremix.models.GTR_Model;
import com.gtremix.models.M;
import com.gtremix.views.GTR_Activity;

public class GTR_Controller {
	
	public static final String TAG = "GTR_Controller: ";
	private static GTR_Activity currentActivity;	
	
	public static GTR_Activity getCurrentActivity() {
		return currentActivity;
	}
	
	public static void setCurrentActivity(GTR_Activity a) {
		currentActivity = a;
	}
	
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) 	{
			Log.d(TAG, "Message recieved");
			Message new_msg;
			switch(msg.what){
				case M.MESSAGE_START_UP:
					new_msg = Message.obtain(GTR_Model.messageHandler, msg.what, msg.obj); 
		        	new_msg.sendToTarget();
		        	Log.d(TAG, "MESSAGE_START_UP sent");
					break;
				case M.MESSAGE_NO_MEDIA_PATH:
					Log.d(TAG, "No media path found");
					break;
				case M.MESSAGE_UPDATE:
					currentActivity.update();
					Log.d(TAG, "Updating Activity");
					break;
				case M.MESSAGE_OPEN_PATH:
					Log.d(TAG, "MESSAGE_OPEN_PATH sent");
					new_msg = Message.obtain(GTR_Model.messageHandler, M.MESSAGE_OPEN_PATH, msg.obj);
					new_msg.sendToTarget();
					break;
				case M.ADD_SONG:
					Log.d(TAG, "ADD_SONG sent");
					new_msg = Message.obtain(GTR_Model.messageHandler, M.ADD_SONG, msg.obj);
					new_msg.sendToTarget();
				default:break;
			}
		}
	};
}
