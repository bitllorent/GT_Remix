package com.gtremix.views;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gtremix.R;
import com.gtremix.controllers.GTR_Controller;
import com.gtremix.models.M;

/** 
*	
*/

public class GTR_MainActivity extends Activity implements GTR_Activity {
    /** Called when the activity is first created. */
	private final String TAG = "GTR_MainActivity: ";
	
	private Bundle data;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GTR_Controller.setCurrentActivity(this);
        
        // Initialize media data
        data = new Bundle();
        Message msg = Message.obtain(GTR_Controller.messageHandler, M.MESSAGE_INIT_MEDIA_PATH, data);
        msg.sendToTarget();
        
    }
    
    public void update()
    {
    	// get path and media
    	String mediaPath = data.getString("PATH");
    	TextView dir = (TextView)findViewById(R.id.dir);
    	dir.setText(mediaPath);
    			
    	ArrayList<String> mediaFiles = data.getStringArrayList("MEDIA");
    	LinearLayout mediaList = (LinearLayout)findViewById(R.id.medialist);
    	
    	// first remove any previous Views from the list
    	mediaList.removeAllViews();
    	
    	for(String s : mediaFiles) {
    		Button b = new Button(this);
    		b.setText(s);
    		b.setTextColor(Color.WHITE);
    		b.setBackgroundColor(0xff444444);
    		mediaList.addView(b);
    	}
    }
}