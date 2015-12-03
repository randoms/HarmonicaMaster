package me.randoms.harmonicmaster;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.randoms.harmonicmaster.views.MyGLSurfaceView;

public class TestGlActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gl);
        //MyGLSurfaceView mGLView = new MyGLSurfaceView(this, );
        //setContentView(mGLView);
    }
}
