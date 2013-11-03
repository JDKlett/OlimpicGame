package com.game.olimpics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


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
		
		try {
			Intent ourIntent = new Intent(this, Class.forName("com.game.olimpics.GameActivity"));
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
