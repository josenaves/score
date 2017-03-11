package com.josenaves.score;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Score App
 * Simple score for your matches.
 * 
 * March, 2014
 * 
 * @author @josenaves
 */
public class ScoreActivity extends Activity implements OnClickListener, OnLongClickListener {

    private TextView scorePlayer1;
    private TextView scorePlayer2;
    
    private int score1;
    private int score2; 
    
    private GestureDetector gestureDetector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // FULL SCREEN For the win !!!
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 

        // No need to use wakelock via systemsServices (nor permission to do this)
        // http://stackoverflow.com/questions/2039555/how-to-get-an-android-wakelock-to-work
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_score);
        
        scorePlayer1 = (TextView) findViewById(R.id.textScore1);
        scorePlayer2 = (TextView) findViewById(R.id.textScore2);

        score1 = 0;
        score2 = 0;
        
        showScores(score1, score2);
        
        scorePlayer1.setOnClickListener(this);
        scorePlayer2.setOnClickListener(this);
        
        scorePlayer1.setOnLongClickListener(this);
        scorePlayer2.setOnLongClickListener(this);
        
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	showScores(score1, score2);
    }
    
    private void showScores(int score1, int score2) {
        scorePlayer1.setText(String.valueOf(score1));
        scorePlayer2.setText(String.valueOf(score2));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textScore1) {
        	if (score1 == 99) {
        		Toast.makeText(this, getString(R.string.score_too_big, String.valueOf(score1)), Toast.LENGTH_SHORT).show();	
        	}
        	else {
        		scorePlayer1.setText(String.valueOf(++score1));
        	}
        }
        else if (view.getId() == R.id.textScore2) {
        	if (score2 == 99) {
        		Toast.makeText(this, getString(R.string.score_too_big, String.valueOf(score2)), Toast.LENGTH_SHORT).show();
        	}
        	else {
        		scorePlayer2.setText(String.valueOf(++score2));	
        	}
        }
    }
    
	@Override
	public boolean onLongClick(View view) {
        if (view.getId() == R.id.textScore1) {
        	if (score1 != 0) {
        		vibrate();
        		Toast.makeText(this, getString(R.string.remove_point, "1"), Toast.LENGTH_SHORT).show();
        		score1--;
        		showScores(score1, score2);
        	}
        }
        else if (view.getId() == R.id.textScore2) {
        	if (score2 != 0) {
        		vibrate();
        		Toast.makeText(this, getString(R.string.remove_point, "2"), Toast.LENGTH_SHORT).show();
        		score2--;
        		showScores(score1, score2);
        	}
        }
		return true;
	}       
    
    private void vibrate() {
    	Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	vibrator.vibrate(120);
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
    	savedInstanceState.putInt("score1", score1);
    	savedInstanceState.putInt("score2", score2);
    }    

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	score1 = savedInstanceState.getInt("score1");
    	score2 = savedInstanceState.getInt("score2");
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (gestureDetector.onTouchEvent(event)) {
    		return true;
    	}
    	return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
    }

    private void onRightSwipe() {
    	Toast.makeText(this, getString(R.string.game_finished), Toast.LENGTH_SHORT).show();
    	
    	score1 = 0;
    	score2 = 0;
    	showScores(score1, score2);
    }

    // Private class for gestures
    private class SwipeGestureDetector extends SimpleOnGestureListener {
      // Swipe properties, you can change it to make the swipe longer or shorter and speed
      private static final int SWIPE_MIN_DISTANCE = 120;
      private static final int SWIPE_MAX_OFF_PATH = 200;
      private static final int SWIPE_THRESHOLD_VELOCITY = 200;

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
          float diffAbs = Math.abs(e1.getY() - e2.getY());
          float diff = e1.getX() - e2.getX();

          if (diffAbs > SWIPE_MAX_OFF_PATH) return false;
          
          // Left swipe
          if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
             ScoreActivity.this.onLeftSwipe();
             
          } 
          else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        	  // Right swipe
        	  ScoreActivity.this.onRightSwipe();
          }
        } 
        catch (Exception e) {
          Log.e("ScoreActivity", "Error on gestures");
        }
        return false;
      }
    }
}
