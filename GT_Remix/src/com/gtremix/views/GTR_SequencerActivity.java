package com.gtremix.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
        /*Button effect6 = (Button)findViewById(R.id.effect6); effect6.setOnClickListener(this);
        Button effect7 = (Button)findViewById(R.id.effect7); effect7.setOnClickListener(this);
        Button effect8 = (Button)findViewById(R.id.effect8); effect8.setOnClickListener(this);
        Button effect9 = (Button)findViewById(R.id.effect9); effect9.setOnClickListener(this);*/
        
        Button rewind = (Button)findViewById(R.id.rewind); 	rewind.setOnClickListener(this);
        Button skip = (Button)findViewById(R.id.skip);		skip.setOnClickListener(this);
        loop = (Button)findViewById(R.id.loop);				loop.setOnClickListener(this);
        play = (Button)findViewById(R.id.play);				play.setOnClickListener(this);
        
        Button back = (Button)findViewById(R.id.back); 		back.setOnClickListener(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	GTR_Controller.setCurrentActivity(this);
    	playing = GTR_Controller.playing();
    	looping = GTR_Controller.looping();
    }
    
    public void update()
    {
    	playing = GTR_Controller.playing();
    	looping = GTR_Controller.looping();
    	if(playing) {
			play.setText("Pause");
		}
		else {
			play.setText("Play");
		}
    	
    	if(looping) {
			loop.setText("End Loop");
		}
		else {
			loop.setText("Loop");
		}
    }
    
    public void play() {
    	sendMessage(M.PLAY, data);
    }
    
    public void pause() {
    	sendMessage(M.PAUSE, data);
    }
    
    public void nextSong(){
    	sendMessage(M.NEXT_SONG, data);
    }
    
    public void prevSong(){
    	sendMessage(M.PREV_SONG, data);
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.effect1: 
				Log.d(TAG, "Applying effect 1");
				GTR_Controller.applyEffect(0, 1.0f);
				break;
			case R.id.effect2: 
				Log.d(TAG, "Applying effect 2");
				GTR_Controller.applyEffect(1, 1.0f);
				break;
			case R.id.effect3: 
				Log.d(TAG, "Applying effect 3");
				GTR_Controller.applyEffect(2, 1.0f);
				break;
			case R.id.effect4: 
				Log.d(TAG, "Applying effect 4");
				GTR_Controller.applyEffect(3, 1.0f);
				break;
			case R.id.effect5: 
				Log.d(TAG, "Applying effect 5");
				GTR_Controller.applyEffect(4, 1.0f);
				break;
			/*case R.id.effect6: 
				Log.d(TAG, "Applying effect 6");
				GTR_Controller.applyEffect(5, 1.0f);
				break;
			case R.id.effect7: 
				Log.d(TAG, "Applying effect 7");
				GTR_Controller.applyEffect(6, 1.0f);
				break;
			case R.id.effect8: 
				Log.d(TAG, "Applying effect 8");
				GTR_Controller.applyEffect(7, 1.0f);
				break;
			case R.id.effect9: 
				Log.d(TAG, "Applying effect 9");
				GTR_Controller.applyEffect(8, 1.0f);
				break;*/
			case R.id.play: 
				if(playing) {
					pause();
					Log.d(TAG, "Pausing playback");
				}
				else {
					play();
					Log.d(TAG, "Resuming playback");
				}
				break;
			
			case R.id.skip: 
				Log.d(TAG, "Skipping song");
				nextSong();
				break;
			case R.id.rewind: 
				Log.d(TAG, "Previous song");
				prevSong();
				break;
			case R.id.loop:  
				if(looping) {
					GTR_Controller.setLoop(false);
					Log.d(TAG, "Ending Loop");
				}
				else {
					GTR_Controller.setLoop(true);
					Log.d(TAG, "Looping");
				}
				break;
			case R.id.back: 
				Intent intent = new Intent(this, GTR_MainActivity.class);
				startActivity(intent);
			default:break;
		}
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inf = getMenuInflater();
    	inf.inflate(R.menu.sequencer_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.save:
            save();
            return true;
        case R.id.load:
            load();
            return true;
        //case R.id.clear:
        	//clear();
        	//return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void save(){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Save Sequence As");
        dialog.setMessage("Sequence file name");
        
        final EditText input = new EditText(this);
        final GTR_SequencerActivity context = this;
        dialog.setView(input)
        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				context.data.putString(M.KEY_PATH, input.getText().toString() + ".seq");
				context.sendMessage(M.SAVE_SEQUENCE, context.data);
				dialog.dismiss();
			}
		})
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		})
        .show();
    }
    
    private void load(){
    	Intent intent = new Intent(this, GTR_BrowserActivity.class);
		intent.putExtra(M.WHAT, M.LOAD_SEQUENCE);
		startActivity(intent);
    }
}