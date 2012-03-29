package com.gtremix.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	private static final String TAG 		= "GTR_Model: ";
	
	public static String env_dir 			= Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private static final String app_path	= env_dir + "/remix_app";
	private static final File app_dir		= new File(app_path);
	private static final File seq_dir		= new File(app_path + "/seq");
	private static final File ini_file 		= new File(app_path + "/gtr.txt");
	
	private static ArrayList<String> playlist = new ArrayList<String>();
	
	private static void startUp(Bundle b) {
		//make sure we have an ini file to read from
		affirmIniFile();	
	}
	
	public static ArrayList<String> getPlaylist(){
		return new ArrayList<String>(playlist);
	}
	
	public static void addSong(String s){
		playlist.add(s);
	}
	
	private static void affirmIniFile() {
		BufferedReader reader;
		
		//try to open a FileReader 
		try {
			reader = getBufferedReader(ini_file);
		} 
		//if the file does not exist yet, create it
		catch(FileNotFoundException fnfe) {
			try {
				Log.d(TAG, "Trying to create new file: " + ini_file.getAbsolutePath());
				app_dir.mkdirs();
				seq_dir.mkdirs();
				ini_file.createNewFile();
				BufferedWriter writer = getBufferedWriter(ini_file);
				String eol = "\n";
				writer.write(";GT Remix INI file" + eol);
				writer.write(";Please do not edit!" + eol);
				writer.write("media_path=" + eol);
				writer.close();
				reader = getBufferedReader(ini_file);
			}
			//if something goes wrong
			catch(IOException ioe) {
				Log.e(TAG, "Error occured while creating the file");
				Log.e(TAG, ioe.getMessage());
				ioe.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	/**
	 * Gets all the filenames in the passed directory and stores them in the passed 
	 * Bundle for delivery to the Controller.
	 * 
	 * @param b - the Bundle the ArrayList will be stored in
	 * @param dir - the directory to search for files
	 */
	private static void getList(Bundle b, File dir) {
		Log.d(TAG, "Searching in " + dir.getAbsolutePath());
		// get the names of all files in the media directory
		String[] list = dir.list();
		
		//put them in our Bundle so that the View can access them
		b.putStringArray(M.KEY_ITEMS, list);
	}
	
	/**
	 * Reads the ini file and returns the value associated with the keyword name
	 * 
	 * @param name - the name of the value to search for
	 * @return The value associated with name
	 */
	private static String parseIniFile(String name) {
		String line;
		try{
			BufferedReader reader = getBufferedReader(ini_file);
			while((line = reader.readLine()) != null) {
				if(!isComment(line)) {	
					int p = line.indexOf("=");
					int len = line.length();
					String line_name = line.substring(0, p);
					String line_value = line.substring(p+1, len);
					if(line_name.equals(name)) {
						Log.d(TAG, "Read " + line_value + " for " + line_name);
						return line_value;
					}
				}
			}
		}
		catch(Exception e) {
			Log.e(TAG, "Error ocurred parsing .INI file");
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		return "";
	}
	
	/**
	 * Updates the .ini file with new data to reflect any changes made to it
	 * 
	 * @param name - The name of the value that should be changed.
	 * @param value - The value to change it to.
	 */
	private static void writeToIniFile(String name, String value) {
		try {
			BufferedReader reader = getBufferedReader(ini_file);
			BufferedWriter writer = getBufferedWriter(ini_file);
			
			String eol = "\n";
			String fileString = "";
			String line;
			while((line = reader.readLine()) != null) {
				if(!isComment(line)) {
					int p = line.indexOf("=");
					String line_name = line.substring(0, p);
					if(line_name.equals(name)) {
						String new_line = line_name + "=" + value + eol;
						fileString += new_line;
					}
					else 
						fileString += line;
				}
				else {
					fileString += line;
				}
			}
			writer.write(fileString);
			writer.close();
		}
		catch (Exception e) {
			Log.e(TAG, "Exception thrown");
			e.printStackTrace();
		}
	}

	private static boolean isComment(String s) {
		int i = 0;
		while(Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.charAt(i) == ';';
	}

	private static void sendMessage(int what) {
		Message msg = Message.obtain(GTR_Controller.messageHandler, what);
		msg.sendToTarget();
	}
	
	private static void sendMessage(int what, Object o) {
		Message msg = Message.obtain(GTR_Controller.messageHandler, what, o);
		msg.sendToTarget();
	}
	
	/**
	 * Handles all the overhead for creating a BufferedWriter. Used to keep
	 * code looking clean and keep the creation of file writers consistent.
	 * 
	 * @param filename - String containing the path to the file
	 * @return A BufferedWriter for that file
	 */
	private static BufferedWriter getBufferedWriter(File file) 
		throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		return writer;
	}
	
	/**
	 * Handles all the overhead for creating a BufferedReader. Used to keep
	 * code looking clean and keep the creation of file writers consistent.
	 * 
	 * @param filename - String containing the path to the file
	 * @return A BufferedWriter for that file
	 */
	private static BufferedReader getBufferedReader(File file) 
		throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		return reader;
	}
	
	/**
	 * The  Model's message Handler. 
	 */
	public static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			Log.d(TAG, "Message received");
			Bundle b;
			switch(msg.what){
			case M.MESSAGE_START_UP: // The app has just started and must retrieve a stored media path or tell the UI there isn't one.
				startUp((Bundle) msg.obj);
				break;
			case M.MESSAGE_OPEN_PATH: //We are attempting to open a folder on the file system for the file browser
				b = (Bundle)msg.obj;
				String path = b.getString(M.KEY_PATH);
				getList(b, new File(path));
				GTR_Model.sendMessage(M.MESSAGE_UPDATE, b);
				Log.d(TAG, "MESSAGE_UPDATE sent.");
				break;
			case M.ADD_SONG:
				b = (Bundle)msg.obj;
				playlist.addAll(b.getStringArrayList(M.KEY_ITEMS));
				GTR_Model.sendMessage(M.MESSAGE_UPDATE, b);
				Log.d(TAG, "MESSAGE_UPDATE sent.");
				break;
			default:break;  
			}
		}
	};
}
