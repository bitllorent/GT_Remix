package com.gtremix.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gtremix.R;
import com.gtremix.models.M;

/** 
*	
*/

public class GTR_MainActivity extends GTR_Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private final String TAG = "GTR_MainActivity: ";
	
	private Bundle data;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Activity starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Send message to controller to initialize everything
        data = new Bundle();
        sendMessage(M.MESSAGE_START_UP, data);

        //initialize all buttons
        Button browserButton = (Button)findViewById(R.id.browse);
        browserButton.setOnClickListener(this);

        Button queueButton = (Button)findViewById(R.id.playlist);
        queueButton.setOnClickListener(this);

        Button aboutButton = (Button)findViewById(R.id.about);
        aboutButton.setOnClickListener(this);

        Button sequencerButton = (Button)findViewById(R.id.sequencer);
        sequencerButton.setOnClickListener(this);

        //Button loadButton = (Button)findViewById(R.id.load);
        //loadButton.setOnClickListener(this);

        //Button settingsButton = (Button)findViewById(R.id.settings);
        //settingsButton.setOnClickListener(this); 
        
    }
    
    public void update()
    {
    	
    }

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
		case R.id.browse:
			//TODO: startActivityForResult
			intent = new Intent(this, GTR_BrowserActivity.class);
			intent.putExtra(M.WHAT, M.ADD_SONG);
			startActivity(intent);
			break;
		case R.id.playlist:
			intent = new Intent(this, GTR_PlaylistActivity.class);
			startActivity(intent);
			break;
		case R.id.about:
			intent = new Intent(this, GTR_AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.sequencer:
			intent = new Intent(this, GTR_SequencerActivity.class);
			startActivity(intent);
			break;
		/*case R.id.load:
			//TODO: startActivityForResult
			intent = new Intent(this, GTR_BrowserActivity.class);
			intent.putExtra(M.WHAT, M.LOAD_SEQUENCE);
			startActivity(intent);
			break;
		case R.id.settings:
			intent = new Intent(this, GTR_SettingsActivity.class);
			startActivity(intent);
			break;*/
		default:break;
		}
		
	}
}