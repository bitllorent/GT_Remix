package com.gtremix.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gtremix.models.GTR_Model;

import android.util.Log;

public class Sequence {
	public final int NUM_PARAM = 5;
	public int[] effect_params;
	
	public Sequence(){
		effect_params = new int[NUM_PARAM];
		
	}
	
	public Sequence(String filename){
		effect_params = new int[NUM_PARAM];
		File file = new File(filename);
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = reader.readLine()) != null){
				int pivot = line.indexOf(' ');
				int index = Integer.parseInt(line.substring(0, pivot));
				int param = Integer.parseInt(line.substring(pivot+1));
				effect_params[index] = param;
			}
			reader.close();
		}
		catch(IOException e){handleException(e);}
		catch(ArrayIndexOutOfBoundsException e){handleException(e);}
	}
	
	public void save(String filename){
		Log.d("GTR_Sequence: ", "Saving File " + filename);
		File file = new File(GTR_Model.app_path + "seq/" + filename);
		Log.d("GTR_Sequence: ", "Path " + file.getAbsolutePath());
		BufferedWriter writer;
		try{
			if(!file.isFile()){
				file.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(file));
			String data = "";
			for(int i=0;i<NUM_PARAM;i++){
				data += "" + i + " " + effect_params[i] + "\n";  
			}
			writer.write(data);
			writer.close();
		}
		catch(IOException e){handleException(e);}
	}
	
	public void applyEffect(int index, float param){
		//effect_params[index] = param;
		
		//TODO: delete this when sliders are implemented
		if(effect_params[index] == 0)
			effect_params[index] = 1;
		else
			effect_params[index] = 0;
	}
	
	private void handleException(Exception e){
		System.err.println("GTR_Sequence: Exception occurred");
		System.err.println("GTR_Sequence: " + e.getMessage());
		e.printStackTrace();
		System.exit(1);
	}

}
