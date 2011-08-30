package mikhailkrishtop.twisterSpinner;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.types.CGSize;

public class MainSingleton {

	public static MainLayer mainLayer = null;
	
	public static CCSprite background;
	
	public static boolean isMenuOpen = false;
	
	static CGSize winsize = CCDirector.sharedDirector().displaySize();
	static float scale = 1.0f;
	static float scaleX = 1.0f; 
	static float scaleY = 1.0f; 
	
	public static Integer sec = 30;
	public static boolean isSoundOn = true;
	public static boolean isRepeatOn = false;
	
	static void init() {
		
		selectImages();
		
	}
	
	static void selectImages() {
		
		float min = (winsize.width < winsize.height) ? winsize.width : winsize.height;
		
		if (min < 600.0f)
		{
			CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("480dp/" + "spinner.plist");
			scale = min / 480.0f;
			scaleX = scale;
			scaleY = scale;
		}
		else 
		{
			CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("720dp/" + "spinner.plist");
			scale = min / 720.0f;
			scaleX = scale;
			scaleY = scale;
		}
	}
}
