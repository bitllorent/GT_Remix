package com.gtremix.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gtremix.R;
import com.gtremix.controllers.GTR_Controller;
import com.gtremix.models.M;

/** 
*	
*/

public class GTR_SequencerActivity extends GTR_Activity implements OnClickListener {

	private final String TAG = "GTR_SequencerActivity: ";
	
	private Bundle data;
	
	private boolean playing = false, looping = false;
	
	private Button play, loop;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequencer);
        
        data = new Bundle();

        //initialize all buttons
        Button effect1 = (Button)findViewById(R.id.effect1); effect1.setOnClickListener(this);
        Button effect2 = (Button)findViewById(R.id.effect2); effect2.setOnClickListener(this);
        Button effect3 = (Button)findViewById(R.id.effect3); effect3.setOnClickListener(this);
        Button effect4 = (Button)findViewById(R.id.effect4); effect4.setOnClickListener(this);
        Button effect5 = (Button)findViewById(R.id.effect5); effect5.setOnClickListener(this);
        Button effect6 = (Button)findViewById(R.id.effect6); effect6.setOnClickListener(this);
        Button effect7 = (Button)findViewById(R.id.effect7); effect7.setOnClickListener(this);
        Button effect8 = (Button)findViewById(R.id.effect8); effect8.setOnClickListener(this);
        Button effect9 = (Button)findViewById(R.id.effect9); effect9.setOnClickListener(this);
        
        Button rewind = (Button)findViewById(R.id.rewind); 	rewind.setOnClickListener(this);
        Button skip = (Button)findViewById(R.id.skip);		skip.setOnClickListener(this);
        loop = (Button)findViewById(R.id.loop);				loop.setOnClickListener(this);
        play = (Button)findViewById(R.id.play);				play.setOnClickListener(this);
    }
    
    public void update()
    {
    	
    }
    
    public void play() {
    	sendMessage(M.PLAY, data);
    }
    
    public void pause() {
    	sendMessage(M.PAUSE, data);
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.effect1: Log.d(TAG, "Applying effect 1"); break;
			case R.id.effect2: Log.d(TAG, "Applying effect 2"); break;
			case R.id.effect3: Log.d(TAG, "Applying effect 3"); break;
			case R.id.effect4: Log.d(TAG, "Applying effect 4"); break;
			case R.id.effect5: Log.d(TAG, "Applying effect 5"); break;
			case R.id.effect6: Log.d(TAG, "Applying effect 6"); break;
			case R.id.effect7: Log.d(TAG, "Applying effect 7"); break;
			case R.id.effect8: Log.d(TAG, "Applying effect 8"); break;
			case R.id.effect9: Log.d(TAG, "Applying effect 9"); break;
			case R.id.play: 
				if(playing) {
					playing = false;
					play.setText("Play");
					pause();
					Log.d(TAG, "Pausing playback");
				}
				else {
					playing = true;
					play.setText("Pause");
					play();
					Log.d(TAG, "Resuming playback");
				}
				break;
			
			case R.id.skip: Log.d(TAG, "Skipping song"); break;
			case R.id.rewind: Log.d(TAG, "Rewinding song"); break;
			case R.id.loop:  
				if(looping) {
					looping = false;
					loop.setText("Loop");
					Log.d(TAG, "Ending Loop");
				}
				else {
					looping = true;
					loop.setText("End Loop");
					Log.d(TAG, "Looping");
				}
				break;
			default:break;
		}
		
	}
}