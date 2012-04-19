package com.gtremix.views;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gtremix.R;
import com.gtremix.controllers.GTR_Controller;
import com.gtremix.models.GTR_Model;
import com.gtremix.models.M;

/** 
*	
*/

public class GTR_BrowserActivity extends GTR_Activity implements OnClickListener {

	//for debugging
	private final String TAG = "GTR_BrowserActivity: ";
	
	//holds important application data
	private Bundle data;
	
	//list that displays all the file and folder names on the screen
	private LinearLayout list;
	
	//used to navigate file browser
	private Stack<String> back_stack;
	
	private Button back, main, home;
	
	private int what;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        
        //initializations
        data = new Bundle();
        list = (LinearLayout)findViewById(R.id.list);
        
        //what we are using the browser for
        what = getIntent().getExtras().getInt(M.WHAT);
        if(what == M.ADD_SONG) {
        	data.putString(M.KEY_PATH, GTR_Model.env_dir);
        }
        else if(what == M.LOAD_SEQUENCE) {
        	data.putString(M.KEY_PATH, GTR_Model.env_dir + File.separator + "remix_app/seq");
        }
        sendMessage(M.MESSAGE_OPEN_PATH, data);
        
        back_stack = new Stack<String>();
        
        back = (Button)findViewById(R.id.back); back.setOnClickListener(this);
        home = (Button)findViewById(R.id.home); home.setOnClickListener(this);
        main = (Button)findViewById(R.id.main);	main.setOnClickListener(this);
        
        if(what != M.ADD_SONG){
        	back.setVisibility(View.GONE);
        	home.setVisibility(View.GONE);
        	main.setVisibility(View.GONE);
        }
    }
    
    public void update()
    {
    	Log.d(TAG, "Updating BrowserActivity");
    	if(data.getString(M.KEY_PATH) != null) {
	    	// get path and media
	    	String path = data.getString(M.KEY_PATH);
	    	TextView ls = (TextView)findViewById(R.id.title);
	    	if(what == M.ADD_SONG) 
	    		ls.setText("File Browser: " + path);
	    	else if(what == M.LOAD_SEQUENCE) 
	    		ls.setText("Browse Sequence Files");
	    				
	    	String[] items = data.getStringArray(M.KEY_ITEMS);
	    	if(items == null) {
	    		return;
	    	}
	    	
	    	//remove any previous Views from the list
	    	list.removeAllViews();
	    	
			//finally create and add the new views
	    	//Log.d(TAG, items.length + " items");
	    	for(String s : items) {
	    		if(s.charAt(0) != '.' ) { //doesn't show hidden files and folders
		    		Button b = new Button(this);
		    		b.setText(s);
		    		GTR_Controller.format(b);
		    		b.setOnClickListener(this);
		    		list.addView(b);
	    		}
	    	}
    	}
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.back: 
			Log.d(TAG, "Back button pressed");
			try {goBackDir(); break;}
			catch (EmptyStackException e) {}
		case R.id.home:
			Log.d(TAG, "Home button pressed");
			changeDir(GTR_Model.env_dir); 
			break;
		case R.id.main:
			Log.d(TAG, "Main menu button pressed");
			Intent intent = new Intent(this, GTR_MainActivity.class);
			startActivity(intent);
			break;
		default:
			Button b = (Button)v;
			String old_path = data.getString(M.KEY_PATH);
			String new_path = old_path + File.separator + b.getText();
			changeDir(new_path);
			break;
		}
		
	}
	
	private void changeDir(String path){
		File f = new File(path);
		try{
			if( f.isDirectory() ) {
				back_stack.push(data.getString(M.KEY_PATH));
				data.putString(M.KEY_PATH, path);
			}
			else if(f.isFile()){
				openFile(path);
			}
			else{
				Log.e(TAG, "An error occurred, invalid file name: " + f.getAbsolutePath());
				System.exit(1);
			}
		}
		catch(SecurityException e) {
			Log.e(TAG, "Exception occurred");
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		sendMessage(M.MESSAGE_OPEN_PATH, data);
	}
	
	private void goBackDir() throws EmptyStackException{
		try{
			String path = back_stack.pop();
			File f = new File(path);
			if( f.isDirectory() ) {
				data.putString(M.KEY_PATH, path);
			}
			else if(f.isFile()){
				openFile(path);
			}
			else{
				Log.e(TAG, "An error occurred, '" + path + "' is not a valid file name.");
				System.exit(1);
			}
		}
		catch(SecurityException e) {
			Log.e(TAG, "Exception occurred");
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		sendMessage(M.MESSAGE_OPEN_PATH, data);
	}
	
	private void openFile(String path){
		ArrayList<String> items = new ArrayList<String>();
		if(what == M.ADD_SONG){
			if(isMusicFile(path)){
				data.putString(M.KEY_FILE, path);
				sendMessage(M.ADD_SONG, data);
				Intent intent = new Intent(this, GTR_PlaylistActivity.class);
				startActivity(intent);
			}
			return;
		}
		else if(what == M.LOAD_SEQUENCE){
			if(isSeqFile(path)){
				data.putString(M.KEY_FILE, path);
				sendMessage(M.LOAD_SEQUENCE, data);
				Intent intent = new Intent(this, GTR_SequencerActivity.class);
				startActivity(intent);
			}
		}
		
	}
	
	/**
	 * Simple test to determine if a file is a music file by looking at its extension
	 * 
	 * @param filename - The filename to check 
	 * @return True if it is a music file, false otherwise
	 */
	private static boolean isMusicFile(String filename) {
		int len = filename.length();
		if(filename.substring(len-4, len).equals(".mp3"))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Simple test to determine if a file is a sequence file by looking at its extension
	 * 
	 * @param filename - The filename to check 
	 * @return True if it is a sequence file, false otherwise
	 */
	private static boolean isSeqFile(String filename) {
		int len = filename.length();
		if(filename.substring(len-4, len).equals(".seq"))
		{
			return true;
		}
		return false;
	}
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	            event.startTracking();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }

	    public boolean onKeyUp(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking() && !event.isCanceled()) {
	        	try{
	            	goBackDir();
	            	return true;
	            }
	        	catch(EmptyStackException e){}
	        }
	        return super.onKeyUp(keyCode, event);
	    }
}