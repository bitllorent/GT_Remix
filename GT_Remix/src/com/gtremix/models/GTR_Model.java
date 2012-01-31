package com.gtremix.models;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gtremix.controllers.GTR_Controller;


/** 
*	
*/
public class GTR_Model {
	
	private static final String TAG = "GTR_Model: ";
	
	private static String mainDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	private static File mediaDir;

	private static void getMediaPath(Bundle b) {
		// TODO:implement initMediaPath that asks user for a directory and saves it to .ini file
		
		//search .ini file for saved directory, if not found, ask user to provide one
		String mediaPath = "";
		if(!(mediaPath.equals(""))) {
			// TODO: send message to Controller asking for user input
			return;
		}

		mediaPath = "Music";
		mediaDir = new File(mainDir + File.separator + mediaPath);
		b.putString("PATH", mediaDir.getName());
		getMedia(b);
	}
	
	private static void getMedia(Bundle b) {
		Log.d(TAG, "Searching in " + mediaDir.getName());
		// get the names of all files in the media directory
		String[] files = mediaDir.list();
		ArrayList<String> music_files = new ArrayList<String>();
		
		// check all files, if any music files are found, add them to our list
		int l = files.length;
		for(int i=0; i < l;i++) {
			if(isMusicFile(files[i])) {
				music_files.add(files[i]);
				Log.d(TAG, "Music file found");
			}
		}
		
		//finally put them in our Bundle so that the View can access them
		b.putStringArrayList("MEDIA", music_files);
		Log.d(TAG, "Data updated");
	}
	
	private static boolean isMusicFile(String filename) {
		Log.d(TAG, "Checking " + filename);
		int l = filename.length();
		if(filename.substring(l-4, l).equals(".mp3"))
		{
			Log.d(TAG, "It is a music file");
			return true;
		}
		return false;
	}
	
	private static void update() {
		Message msg = Message.obtain(GTR_Controller.messageHandler, M.MESSAGE_UPDATE);
		msg.sendToTarget();
	}
	
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			Log.d(TAG, "Message received");
			switch(msg.what){
			case M.MESSAGE_INIT_MEDIA_PATH: // The app has just started and must retrieve a stored media path or ask the user for a new one
				getMediaPath((Bundle) msg.obj);
				update(); // send a message indicating that data has changed and that the UI must update to reflect the changes
			default:break;  
			}
		}
	};
}
