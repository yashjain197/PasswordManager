package com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.passwordmanager.passwordmanager.ui.AddPasswordScreen.AddPasswordData;
import com.passwordmanager.passwordmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /** Setting click listener on fab button **/
        binding.addPassword.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddPasswordData.class));
        });

        gestureDetector= new GestureDetector(this,new GestureListener());


    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {


        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Toast.makeText(MainActivity.this, "Fling", Toast.LENGTH_SHORT).show();
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onSwipeRight() {

    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {

    }

    public void onSwipeBottom() {
    }


}