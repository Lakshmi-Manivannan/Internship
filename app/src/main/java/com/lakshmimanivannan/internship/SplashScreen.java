package com.lakshmimanivannan.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation top,bottom;
    ImageView logo;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        top = (Animation) AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = (Animation) AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        logo = (ImageView) findViewById(R.id.logo);
        name = (TextView) findViewById(R.id.name);


        logo.setAnimation(top);
        name.setAnimation(bottom);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread{
        public void run(){
            try {
                sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    }
}