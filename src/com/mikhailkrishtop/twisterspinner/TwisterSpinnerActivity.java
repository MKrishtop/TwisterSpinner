package com.mikhailkrishtop.twisterspinner;

import android.media.AudioManager;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class TwisterSpinnerActivity extends Activity {
	
	protected CCGLSurfaceView _ccsv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        _ccsv = new CCGLSurfaceView(this);
        
        setContentView(_ccsv);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	MainSingleton.isMenuOpen = false;
    	SoundEngine.sharedEngine().releasAllSounds();
    	CCDirector.sharedDirector().end();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	CCDirector.sharedDirector().attachInView(_ccsv);
    	CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
    	CCDirector.sharedDirector().setDisplayFPS(false);
    	CCDirector.sharedDirector().setAnimationInterval(1.0f/60);
    	
    	MainSingleton.init();
    	
    	CCScene scene = MainLayer.scene();
    	CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    public void onBackPressed() {
        if (MainSingleton.isMenuOpen)
        {
            MainSingleton.mainLayer.onMenuClose();
        }
        else
        {
   		    super.onBackPressed();
    	    this.finish();
        }
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	
    	if (keyCode == KeyEvent.KEYCODE_MENU)
    	{
    		if (MainSingleton.isMenuOpen)
    			MainSingleton.mainLayer.onMenuClose();
    		else
    			MainSingleton.mainLayer.onMenuOpen();
    		return true;
    	}

    	return super.onKeyUp(keyCode, event);
    }
}