package me.randoms.harmonicmaster;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class TestActivity extends Activity{
	private View actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_v1);
	    // create our manager instance after the content view is set
	    SystemBarTintManager tintManager = new SystemBarTintManager(this);
	    // enable status bar tint
	    tintManager.setStatusBarTintEnabled(true);
	    // enable navigation bar tint
	    tintManager.setNavigationBarTintEnabled(true);
	    tintManager.setTintColor(getResources().getColor(R.color.base_darker));
	    
	    actionBar = findViewById(R.id.actionbar);
	    
	    Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    int width = size.x;
	    
	    float actionBarHeight = (float)width/4*3;
	    ViewGroup.LayoutParams mParams = actionBar.getLayoutParams();
	    mParams.height = (int)actionBarHeight;
	    actionBar.setLayoutParams(mParams);
	    
	}
	
	
}
