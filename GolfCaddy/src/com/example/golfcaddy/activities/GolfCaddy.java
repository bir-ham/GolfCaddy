package com.example.golfcaddy.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.golfcaddy.R;
import com.example.golfcaddy.database.DBHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class GolfCaddy extends Activity implements OnClickListener {
	
	private String TAG;
	
	private Button buttonPlus, buttonMinus, buttonOk, buttonPrefs, buttonAbout;
	private TextView textScore;
	private ListView scoreCard;
	private Cursor cursor;
	private SimpleCursorAdapter adapter;
	private Integer score=0;
	private SharedPreferences prefs;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private Intent i;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        // Initialize variables
        TAG = getResources().getString(R.string.app_name);
        
        // Inflate UI from XML
        setContentView(R.layout.main);
        
        // Get a hang of UI components
        buttonPlus = (Button)findViewById(R.id.buttonPlus);
        buttonMinus= (Button)findViewById(R.id.buttonMinus);
        buttonOk   = (Button)findViewById(R.id.buttonOk);
//        buttonPrefs= (Button)findViewById(R.id.buttonPrefs);
//        buttonAbout= (Button)findViewById(R.id.buttonAbout);
        textScore  = (TextView)findViewById(R.id.textScore);
        scoreCard  = (ListView)findViewById(R.id.scoreCard);
                
        // Add onClick listeners
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonOk.setOnClickListener(this);
//        buttonPrefs.setOnClickListener(this);
//        buttonAbout.setOnClickListener(this);
        
        // Get preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        textScore.setText(score.toString());

        // Initialize the database
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        // Load the data (essentially executes a SELECT statement)
        cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        startManagingCursor(cursor);

        // Set the list adapter
        String[] from = {DBHelper.COL_STROKES, DBHelper.COL_TIMESTAMP};
        int[] to = {R.id.textScore, R.id.textTimestamp};
        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
        adapter.setViewBinder(VIEW_BINDER);
        scoreCard.setAdapter(adapter);

        Log.d(TAG, "onCreated()");
    }
    
    // Our custom bind to timestamp colon to its view and change data from 
    // timestamp to reative time 
    static final ViewBinder VIEW_BINDER = new ViewBinder() {
	
		@Override
		public boolean setViewValue(View view, Cursor cursor,
				int columnIdex) {
			if (cursor.getColumnIndex(DBHelper.COL_TIMESTAMP) != columnIdex) {
				// We are not processing anything other than timestamp column
				return false;
			} else {
				long timestamp = cursor.getLong(columnIdex);
				CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
				((TextView) view).setText(relativeTime);
				
				return true;
			}			
		}		
	};

	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
    	getMenuInflater().inflate(R.menu.menu, menu);
    	
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.help:
			showDialog(-1);
			return true;
		case R.id.about:
			i = new Intent(this, About.class);
    		i.putExtra("aboutText", "This is the latest version of GolfCaddy\n" +
    				"Developed by Birhanu Hailemariam 2011.");
    		startActivity(i);
			return true;
		case R.id.pref:
			i = new Intent(this, Preferences.class);
    		startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
	@Override
    public void onRestoreInstanceState(Bundle inState) {
        score = (inState!=null) ? inState.getInt("score",0) : 0;
        Log.d(TAG, "onRestoreInstanceState score=" +score);
        textScore.setText(score.toString());
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	outState.putInt("score", score);
    	Log.d(TAG, "onSaveInstanceState score=" +score);
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onClick(View src) {
    	switch(src.getId()) {
    	case R.id.buttonPlus:
    		score++;
    		break;
    	case R.id.buttonMinus:
    		score--;
    		break;
    	case R.id.buttonOk:
    		// Save in DB
    		ContentValues values = new ContentValues();
    		values.put(DBHelper.COL_TEE, "-1");
    		values.put(DBHelper.COL_STROKES, score);
    		values.put(DBHelper.COL_TIMESTAMP, System.currentTimeMillis());
    		db.insert(DBHelper.TABLE_NAME, null, values);
    		Log.d(TAG, "onClick() buttonOk inserted values="+values);

    		cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
    		startManagingCursor(cursor);

    		adapter.changeCursor(cursor);
    		score = 0;
    		break;
//    	case R.id.buttonPrefs:
//    		i = new Intent(this, Preferences.class);
//    		startActivity(i);
//    		break;
//    	case R.id.buttonAbout:
//    		i = new Intent(this, About.class);
//    		i.putExtra("aboutText", "This is the latest version of GolfCaddy\n" +
//    				"Developed by Birhanu Hailemariam 2011.");
//    		startActivity(i);
//    		break;
    	}
    	textScore.setText(score.toString());
    }
    
 
  
}