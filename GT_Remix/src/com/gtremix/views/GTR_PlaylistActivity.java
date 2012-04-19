package com.gtremix.views;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gtremix.R;
import com.gtremix.controllers.GTR_Controller;

/** 
*	
*/

public class GTR_PlaylistActivity extends GTR_Activity implements OnClickListener {

	private final String TAG = "GTR_PlaylistActivity: ";
	
	private Bundle data;
	
	private LinearLayout list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        
        data = new Bundle();      
        list = (LinearLayout)findViewById(R.id.list);
        
        Button main = (Button)findViewById(R.id.main);
        main.setOnClickListener(this);
    }
    
    public void update()
    {
    	Log.d(TAG, "Updating PlaylistActivity");
    	//remove any previous Views from the list
    	list.removeAllViews();
    	
    	ArrayList<String> songs = GTR_Controller.getPlaylist();
    	
		//finally create and add the new views
    	for(String s : songs) {
    		Button b = new Button(this);
    		b.setText(s);
    		GTR_Controller.format(b);
    		b.setOnClickListener(this);
    		list.addView(b);
    	}
    }

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.main:
				intent = new Intent(this, GTR_MainActivity.class);
				startActivity(intent);
				break;
			default:
				Button b = (Button)v;
				GTR_Controller.playSong((String)b.getText());
				intent = new Intent(this, GTR_SequencerActivity.class);
				startActivity(intent);
				break;
		}
		
	}
}