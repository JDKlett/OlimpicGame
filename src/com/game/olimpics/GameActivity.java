package com.game.olimpics;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GameActivity extends Activity {

	RunnerView runner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//activity e' una sottoclasse di context
		setContentView(R.layout.olimpic_layout);
		//il binding viene effettuato a runtime dal sistema
		//mi basta dichiararlo in entrambi ed il gioco e` fatto
		runner = (RunnerView) findViewById(R.id.runner);
		//runner = new RunnerView(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		runner.pause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		runner.resume();
	}

}
