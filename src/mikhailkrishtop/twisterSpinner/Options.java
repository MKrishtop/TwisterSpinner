package mikhailkrishtop.twisterSpinner;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.menus.CCMenuItemToggle;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.protocols.CCTouchDelegateProtocol;

import android.view.MotionEvent;
import org.cocos2d.types.ccColor3B;

public class Options extends CCNode implements CCTouchDelegateProtocol{
	
	public CCSprite options;
	CCSprite interval;
    CCSprite shadow;
	CCMenu menu1;
	CCMenu menu2;
	CCLabel secLabel;
	
	public boolean isSound = true;
	public boolean isRepeate = false;
    public boolean isMoving = false;
	
	public Options() {



		options = CCSprite.sprite("options.png", true);
		options.setScale(MainSingleton.scale);
		options.setPosition(MainSingleton.winsize.width * 0.5f, - options.getHeight() * 0.5f);
		MainSingleton.mainLayer.addChild(options,3);

        shadow = CCSprite.sprite("shadow.png", true);
        shadow.setScaleX(MainSingleton.scale);
        shadow.setScaleY(MainSingleton.winsize.height + options.getHeight() * 2.0f);
        shadow.setPosition(MainSingleton.winsize.width * 0.5f,MainSingleton.winsize.height * 0.5f);
        shadow.setVisible(false);
        MainSingleton.mainLayer.addChild(shadow,2);

		CCSprite tmp;
		CCSprite tmp2;
		
		tmp = CCSprite.sprite("sound_on.png", true);
		tmp2 = CCSprite.sprite("sound_off.png", true);
		CCMenuItemSprite sound_on = CCMenuItemSprite.item(tmp, tmp, this, "ass");
		CCMenuItemSprite sound_off = CCMenuItemSprite.item(tmp2, tmp2, this, "ass");
		CCMenuItemToggle sound = CCMenuItemToggle.item(this, "menuSoundTouched", sound_on, sound_off);
		
		tmp = CCSprite.sprite("repeat_on.png", true);
		tmp2 = CCSprite.sprite("repeat_off.png", true);
		CCMenuItemSprite repeat_on = CCMenuItemSprite.item(tmp, tmp, this, "ass");
		CCMenuItemSprite repeat_off = CCMenuItemSprite.item(tmp2, tmp2, this, "ass");
		CCMenuItemToggle repeat = CCMenuItemToggle.item(this, "menuRepeatTouched", repeat_off, repeat_on);
		
		menu1 = CCMenu.menu(sound, repeat);
		menu1.setPosition(0.05f * options.getContentSize().width 
				+ (sound.getContentSize().width + repeat.getContentSize().width) * 0.5f,
				options.getContentSize().height * 0.5f);
		menu1.alignItemsHorizontally(1.1f);
		options.addChild(menu1);
		
		tmp = CCSprite.sprite("up_arrow.png", true);
		CCMenuItemSprite up_arrow = CCMenuItemSprite.item(tmp, tmp, this, "menuUpArrowTouched");
		
		tmp = CCSprite.sprite("down_arrow.png", true);
		CCMenuItemSprite down_arrow = CCMenuItemSprite.item(tmp, tmp, this, "menuDownArrowTouched");
		
		menu2 = CCMenu.menu(up_arrow, down_arrow);
		menu2.setPosition(options.getContentSize().width - 0.05f * options.getContentSize().width 
				- up_arrow.getContentSize().width * 0.5f,
				options.getContentSize().height * 0.5f);
		menu2.alignItemsVertically(1.1f);
		options.addChild(menu2);
		
		interval = CCSprite.sprite("interval.png", true);
		interval.setPosition(options.getContentSize().width - 0.05f * options.getContentSize().width 
				- up_arrow.getContentSize().width
				- interval.getContentSize().width * 0.5f - 5.0f,
				options.getContentSize().height * 0.5f);
		options.addChild(interval);
		
		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, 1 , true);
		
		replotSec();
	}
	
	public void menuUpArrowTouched(Object sender) {
		if (MainSingleton.sec < 999)
			MainSingleton.sec++;
		
		if (MainSingleton.isRepeatOn) 
		{
			MainSingleton.mainLayer.disableScheduler();
			MainSingleton.mainLayer.enableScheduler();
		}
		
		replotSec();
		System.out.println("~~up_arr");
	}
	
	public void menuDownArrowTouched(Object sender) {
		if (MainSingleton.sec > 5)
			MainSingleton.sec--;
		
		if (MainSingleton.isRepeatOn) 
		{
			MainSingleton.mainLayer.disableScheduler();
			MainSingleton.mainLayer.enableScheduler();
		}
		
		replotSec();
		System.out.println("~~down_arr");
	}
	
	public void menuSoundTouched(Object sender) {
		MainSingleton.isSoundOn = !MainSingleton.isSoundOn;
	}
	
	public void menuRepeatTouched(Object sender) {
		MainSingleton.isRepeatOn = !MainSingleton.isRepeatOn;
		if (MainSingleton.isRepeatOn) MainSingleton.mainLayer.enableScheduler();
		else MainSingleton.mainLayer.disableScheduler();
	}

	public void replotSec() {
		
		if (secLabel != null)
			options.removeChild(secLabel, true);
		
		secLabel = CCLabel.makeLabel(MainSingleton.sec.toString(), "Monospace", 60.0f);
        secLabel.setColor(ccColor3B.ccc3(0, 0, 0));
        secLabel.setOpacity(150);
		secLabel.setAnchorPoint(1.0f, 0.5f);
		secLabel.setPosition(interval.getPosition().x + interval.getContentSize().width * 0.5f - 5.0f,interval.getPosition().y);
		options.addChild(secLabel);
	}
	
	public boolean ccTouchesBegan(MotionEvent event) {
		return false;
	}
	
	public boolean ccTouchesMoved(MotionEvent event) {
		return false;
	}
	
	public boolean ccTouchesEnded(MotionEvent event) {
		return false;
	}
	
	public boolean ccTouchesCancelled(MotionEvent event) {
		return ccTouchesEnded(event);
	}
	
}
