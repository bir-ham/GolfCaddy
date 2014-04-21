package com.example.golfcaddy.activities;

import com.example.golfcaddy.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	TextView textAbout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		// get the intent
		Intent i = getIntent();
		String about = i.getStringExtra("aboutText");
		
		// get the the text view and update it
		textAbout = (TextView) findViewById(R.id.textAbout);
		textAbout.setText(about);
	}
}
