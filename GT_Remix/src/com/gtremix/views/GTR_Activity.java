package com.gtremix.views;

import android.app.Activity;
import android.os.Message;

import com.gtremix.controllers.GTR_Controller;

/**
 * Our implementaion of the Android's Activity class. 
 * Has some extra methods that need to be called without directly knowing
 * the exact class. This allows us to have multiple classes representing
 * multiple views.
 * 
 * @author CJ
 *
 */
public abstract class GTR_Activity extends Activity {
	
	public void update(){}
	
	protected void sendMessage(int what, Object data) {
		Message msg = Message.obtain(GTR_Controller.messageHandler, what, data);
        msg.sendToTarget();
	}
	
	@Override
    public void onResume(){
    	super.onResume();
    	GTR_Controller.setCurrentActivity(this);
    }
}
