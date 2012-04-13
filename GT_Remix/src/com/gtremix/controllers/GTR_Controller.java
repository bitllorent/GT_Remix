package com.gtremix.controllers;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;

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
		currentActivity.update();
	}
	
	public static void format(Button b) {
		b.setTextColor(Color.WHITE);
		b.setBackgroundColor(0xff444444);
		b.setGravity(Gravity.LEFT);
		b.setTextSize(20);
	}
	
	private static ArrayList<String> playlist = new ArrayList<String>();
	private static String nowPlaying = "";
	private static boolean looping = false;
	private static boolean playing = false;
	
	public static ArrayList<String> getPlaylist(){
		return new ArrayList<String>(playlist);
	}
	
	public static void addSong(String s){
		playlist.add(s);
	}
	
	public static void playSong(int index) {
		Log.d(TAG, "Now playing: " + nowPlaying);
		nowPlaying = playlist.get(index);
		//play(nowPlaying);
	}
	
	public static void playSong(String s) {
		Log.d(TAG, "Now playing: " + nowPlaying);
		nowPlaying = s;
		//play(nowPlaying);
	}
	
	public static void nextSong() {
		int index = playlist.indexOf(nowPlaying);
		index++;
		if(index < playlist.size()) {
			Log.d(TAG, "Playing next song: " + nowPlaying);
			nowPlaying = playlist.get(index);
			//play(nowPlaying);
		}
		else if(looping){
			index = 0;
			nowPlaying = playlist.get(index);
			//play(nowPlaying);
		}
		else {
			//stop();
		}
	}
	
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) 	{
			Log.d(TAG, "Message recieved");
			Message new_msg;
			switch(msg.what){
				case M.MESSAGE_START_UP:
					new_msg = Message.obtain(GTR_Model.messageHandler, msg.what, msg.obj);
					Log.d(TAG, "MESSAGE_START_UP sent");
		        	new_msg.sendToTarget();
					break;
				case M.MESSAGE_NO_MEDIA_PATH:
					Log.d(TAG, "No media path found");
					break;
				case M.MESSAGE_UPDATE:
					Log.d(TAG, "Updating Activity");
					currentActivity.update();
					break;
				case M.MESSAGE_OPEN_PATH:
					Log.d(TAG, "MESSAGE_OPEN_PATH sent");
					new_msg = Message.obtain(GTR_Model.messageHandler, M.MESSAGE_OPEN_PATH, msg.obj);
					new_msg.sendToTarget();
					break;
				case M.ADD_SONG:
					Bundle b = (Bundle)msg.obj;
					playlist.addAll(b.getStringArrayList(M.KEY_ITEMS));
					break;
				default:break;
			}
		}
	};
}
