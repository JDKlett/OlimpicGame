package com.game.olimpics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class GameLauncher extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//aggiungere la gestione dei differenti stati
		
		
		View launcherView = new View(this);
		launcherView.setBackgroundResource(R.drawable.home_bg);
		launcherView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
			
				try {
					Intent ourIntent = new Intent(v.getContext(), Class.forName("com.game.olimpics.GameActivity"));
					startActivity(ourIntent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				//il flusso non deve essere restituito continuamente
				return false;
			}
			
		 
		});
		
		setContentView(launcherView);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
