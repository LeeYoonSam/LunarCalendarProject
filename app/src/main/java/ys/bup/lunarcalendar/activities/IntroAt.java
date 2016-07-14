package ys.bup.lunarcalendar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ys.bup.lunarcalendar.R;

/**
 * Created by ys on 2016. 7. 13..
 */
public class IntroAt extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_intro);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveMainAt();
            }
        }, 2000);
    }

    public void moveMainAt()
    {
        Intent i = new Intent(IntroAt.this, LunarMainAt.class);
        startActivity(i);

        finish();
    }
}
