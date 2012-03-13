package com.gtremix.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.gtremix.R;
import com.gtremix.controllers.GTR_Controller;

/** 
*	
*/

public class GTR_SettingsActivity extends GTR_Activity implements OnClickListener {

	private final String TAG = "GTR_SettingsActivity: ";
	
	private Bundle data;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        GTR_Controller.setCurrentActivity(this);
        
        data = new Bundle();

        //initialize all buttons
    }
    
    public void update()
    {
    	
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
			case 0: break;
		default:break;
		}
		
	}
}