package com.gtremix.views;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class GTR_PlaylistActivity extends GTR_Activity implements OnClickListener {

	private final String TAG = "GTR_PlaylistActivity: ";
	
	private Bundle data;
	
	private LinearLayout list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        
        GTR_Controller.setCurrentActivity(this);
        
        data = new Bundle();

        //initialize all buttons
        update();
    }
    
    public void update()
    {
    	//remove any previous Views from the list
    	list.removeAllViews();
    	
		//finally create and add the new views
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    	lp.setMargins(0, 0, 0, 1);
    	for(String s : GTR_Model.getPlaylist()) {
    		Button b = new Button(this);
    		b.setText(s);
    		b.setTextSize(20);
    		b.setTextColor(Color.WHITE);
    		b.setBackgroundColor(0xff444444);
    		b.setGravity(Gravity.LEFT);
    		b.setLayoutParams(lp);
    		b.setOnClickListener(this);
    		list.addView(b);
    	}
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
			case 0:
				//play the selected song
				break;
		default:break;
		}
		
	}
}