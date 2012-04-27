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
	
	public static Sequence getSequence() {
		return currentSequence;
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
	
	private static Sequence currentSequence = new Sequence();
	
	public static void applyEffect(int index, float param) {
		currentSequence.applyEffect(index, param);
	}
	
	public static ArrayList<String> getPlaylist(){
		return new ArrayList<String>(playlist);
	}
	
	public static void addSong(String s){
		playlist.add(s);
	}
	
	public static void playSong(int index) {
		nowPlaying = playlist.get(index);
		playing = true;
		//play(nowPlaying);
		Log.d(TAG, "Now playing: " + nowPlaying);
	}
	
	public static void playSong(String s) {
		nowPlaying = s;
		playing = true;
		//play(nowPlaying);
		Log.d(TAG, "Now playing: " + nowPlaying);
	}
	
	public static void play() {
		//play(nowPlaying);
		playing = true;
	}
	
	public static void pause() {
		//pause();
		playing = false;
	}
	
	public static void nextSong() {
		int index = playlist.indexOf(nowPlaying);
		index++;
		if(index < playlist.size()) {
			nowPlaying = playlist.get(index);
			Log.d(TAG, "Playing song: " + nowPlaying);
			//play(nowPlaying);
		}
		else if(looping){
			index = 0;
			nowPlaying = playlist.get(index);
			Log.d(TAG, "Playing song: " + nowPlaying);
			//play(nowPlaying);
		}
		else {
			//stop();
		}
	}
	
	public static void prevSong() {
		int index = playlist.indexOf(nowPlaying);
		index--;
		if(index >= 0) {
			nowPlaying = playlist.get(index);
			//play(nowPlaying);
		}
		else {
			index = 0;
			nowPlaying = playlist.get(index);
			//play(nowPlaying);
		}
		Log.d(TAG, "Playing song: " + nowPlaying);
	}
	
	public static boolean playing(){
		return playing;
	}
	
	public static boolean looping(){
		return looping;
	}
	
	public static void setLoop(boolean b) {
		looping = b;
		currentActivity.update();
	}
	
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) 	{
			Log.d(TAG, "Message recieved");
			Message new_msg;
			Bundle b;
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
					b = (Bundle)msg.obj;
					playlist.add(b.getString(M.KEY_FILE));
					break;
				case M.LOAD_SEQUENCE:
					b = (Bundle)msg.obj;
					currentSequence = new Sequence(b.getString(M.KEY_FILE));
					break;
				case M.PLAY:
					Log.d(TAG, "Message PLAY recieved");
					play();
					currentActivity.update();
					break;
				case M.PAUSE:
					Log.d(TAG, "Message PAUSE recieved");
					pause();
					currentActivity.update();
					break;
				case M.NEXT_SONG:
					Log.d(TAG, "Message NEXT_SONG recieved");
					nextSong();
					break;
				case M.PREV_SONG:
					Log.d(TAG, "Message PREV_SONG recieved");
					prevSong();
					break;
				case M.SAVE_SEQUENCE:
					Log.d(TAG, "Saving current sequence");
					b = (Bundle)msg.obj;
					currentSequence.save(b.getString(M.KEY_PATH));
				default:break;
			}
		}
	};
}
