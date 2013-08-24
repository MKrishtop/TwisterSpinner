package com.mikhailkrishtop.twisterspinner;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.content.Context;
import android.view.MotionEvent;

public class MainLayer extends CCLayer {

	static CGSize winsize = CCDirector.sharedDirector().displaySize();

	String[][] res = new String[][] { { "right foot on the", "green" },
			{ "right foot on the", "red" }, { "right foot on the", "yellow" },
			{ "right foot on the", "blue" },

			{ "left foot on the", "green" }, { "left foot on the", "red" },
			{ "left foot on the", "yellow" }, { "left foot on the", "blue" },

			{ "left hand on the", "green" }, { "left hand on the", "red" },
			{ "left hand on the", "yellow" }, { "left hand on the", "blue" },

			{ "right hand on the", "green" }, { "right hand on the", "red" },
			{ "right hand on the", "yellow" }, { "right hand on the", "blue" } };

	CCSprite arrow;
	CCSprite bar;
	Options options;

	boolean isLocked = false;
	float velocity = 540.0f;
	float ang, currAng = 0.0f;
	final CGPoint arrowCenter;

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new MainLayer();

		scene.addChild(layer);

		return scene;
	}

	public MainLayer() {

		MainSingleton.mainLayer = this;
		this.setIsTouchEnabled(true);

		CCSprite wheel = CCSprite.sprite("wheel.png", true);
		wheel.setScale(MainSingleton.scale);
		wheel.setPosition(winsize.width * 0.5f,
				winsize.height - wheel.getHeight() * 0.5f);
		this.addChild(wheel);

		arrow = CCSprite.sprite("arrow3.png", true);
		arrow.setScale(MainSingleton.scale);
		arrowCenter = CGPoint.make(winsize.width * 0.5f, winsize.height
				- arrow.getWidth() * 0.5f);
		arrow.setPosition(arrowCenter);
		this.addChild(arrow);

		options = new Options();

		CCSprite bg = CCSprite.sprite("background2.png", true);
		bg.setScale(MainSingleton.scale);
		bg.setPosition(winsize.width * 0.5f, bg.getHeight() * 0.5f
				- options.options.getHeight());
		this.addChild(bg, -1);

		Context context = CCDirector.sharedDirector().getActivity();
		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.right_foot_green);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.right_foot_red);
		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.right_foot_yellow);
		SoundEngine.sharedEngine()
				.preloadEffect(context, R.raw.right_foot_blue);

		SoundEngine.sharedEngine()
				.preloadEffect(context, R.raw.left_foot_green);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.left_foot_red);
		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.left_foot_yellow);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.left_foot_blue);

		SoundEngine.sharedEngine()
				.preloadEffect(context, R.raw.left_hand_green);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.left_hand_red);
		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.left_hand_yellow);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.left_hand_blue);

		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.right_hand_green);
		SoundEngine.sharedEngine().preloadEffect(context, R.raw.right_hand_red);
		SoundEngine.sharedEngine().preloadEffect(context,
				R.raw.right_hand_yellow);
		SoundEngine.sharedEngine()
				.preloadEffect(context, R.raw.right_hand_blue);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		synchronized (this) {
			if (isLocked) return false;
			float angleToRotate;
			do {
				angleToRotate = (float) Math.random() * 360.0f + 360.0f;
			} while ((int) ((angleToRotate % 360.0f) / 22.5f) % 4 == (int) (currAng / 22.5f) % 4);
			rotate(angleToRotate);
			return true;
		}
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		return true;
	}

	void rotate(float angle) {
		isLocked = true;
		ang = angle;
		this.schedule("rotateScheduler", 1.0f / 60.0f);
		if (bar != null) {
			CCScaleTo scaleUp = CCScaleTo.action(0.1f,
					MainSingleton.scale * 1.1f);
			CCScaleTo scaleDown = CCScaleTo.action(0.4f, 0.01f);
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
					"barIsScaledDown");
			CCSequence actions1 = CCSequence.actions(scaleUp, scaleDown,
					actionMoveDone);

			bar.runAction(actions1);
		}
	}

	public void rotateScheduler(float dt) {
		if (Math.abs(ang) < 30) {
			ang = 0.0f;
			this.unschedule("rotateScheduler");
			resultBar(currAng);
			isLocked = false;
		} else {
			if (ang > 0.0f) {
				rotateClockwise(dt * velocity);
				ang -= dt * velocity;
			} else {
				rotateCounterclockwise(dt * velocity);
				ang += dt * velocity;
			}

		}
	}

	void rotateClockwise(float angle) {
		float next_ang = currAng + angle;
		if (next_ang > 360)
			next_ang = next_ang % 360.0f;
		arrow.setRotation(next_ang);
		currAng = next_ang;
	}

	void rotateCounterclockwise(float angle) {
		float next_ang = currAng - angle;
		if (next_ang < 0)
			next_ang += 360.0f;
		arrow.setRotation(next_ang);
		currAng = next_ang;
	}

	void resultBar(float ang) {

		int num = (int) (ang / 22.5f);
		String text1 = res[num][0];
		String text2 = res[num][1];

		CCLabel label1 = CCLabel.makeLabel(text1, "Monospace", 36);
		CCLabel label2 = CCLabel.makeLabel(text2, "Monospace", 36);

		bar = CCSprite.sprite("bar.png", true);
		bar.setScale(MainSingleton.scale);
		bar.setPosition(winsize.width * 0.5f, bar.getHeight() * 0.5f);

		label1.setAnchorPoint(0.5f, 0.5f);
		label2.setAnchorPoint(0.5f, 0.5f);

		bar.addChild(label1);
		bar.addChild(label2);

		label1.setPosition(bar.getContentSize().width * 0.5f,
				(bar.getContentSize().height + label1.getHeight()) * 0.5f);
		label2.setPosition(bar.getContentSize().width * 0.5f,
				(bar.getContentSize().height - label2.getHeight()) * 0.5f);
		System.out.println("~pos_l1: " + label1.getPosition().toString());
		System.out.println("~pos_l2: " + label2.getPosition().toString());

		bar.setScale(0.01f);

		this.addChild(bar);

		CCScaleTo scaleUp = CCScaleTo.action(0.6f, MainSingleton.scale * 1.1f);
		CCScaleTo scaleDown = CCScaleTo.action(0.15f, MainSingleton.scale);
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "barIsScaledUp");
		CCSequence actions1 = CCSequence.actions(scaleUp, scaleDown,
				actionMoveDone);

		bar.runAction(actions1);

		if (MainSingleton.isSoundOn) {
			Context context = CCDirector.sharedDirector().getActivity();
			switch (num) {
			case 0:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_foot_green);
				break;
			case 1:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_foot_red);
				break;
			case 2:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_foot_yellow);
				break;
			case 3:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_foot_blue);
				break;

			case 4:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_foot_green);
				break;
			case 5:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_foot_red);
				break;
			case 6:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_foot_yellow);
				break;
			case 7:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_foot_blue);
				break;

			case 8:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_hand_green);
				break;
			case 9:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_hand_red);
				break;
			case 10:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_hand_yellow);
				break;
			case 11:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.left_hand_blue);
				break;

			case 12:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_hand_green);
				break;
			case 13:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_hand_red);
				break;
			case 14:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_hand_yellow);
				break;
			case 15:
				SoundEngine.sharedEngine().playEffect(context,
						R.raw.right_hand_blue);
				break;
			}
		}
	}

	public void barIsScaledUp(Object sender) {

	}

	public void barIsScaledDown(Object sender) {
		this.removeChild(bar, true);
	}

	public void onMenuOpen() {
		if (!options.isMoving) {
			CCMoveTo moveUp = CCMoveTo.action(
					0.3f,
					CGPoint.ccp(this.getPosition().x, this.getPosition().y
							+ options.options.getHeight()));
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
					"optionsMovingComplete");
			CCSequence action = CCSequence.actions(moveUp, actionMoveDone);

			this.runAction(action);
			options.isMoving = true;
			MainSingleton.isMenuOpen = true;
			isLocked = true;
			options.shadow.setVisible(true);
		}
	}

	public void onMenuClose() {
		if (!options.isMoving) {
			CCMoveTo moveDown = CCMoveTo.action(
					0.3f,
					CGPoint.ccp(this.getPosition().x, this.getPosition().y
							- options.options.getHeight()));
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this,
					"optionsMovingComplete");
			CCSequence action = CCSequence.actions(moveDown, actionMoveDone);

			this.runAction(action);
			options.isMoving = true;
			MainSingleton.isMenuOpen = false;
			isLocked = false;
		}
	}

	public void optionsMovingComplete(Object sender) {
		options.isMoving = false;
		if (!MainSingleton.isMenuOpen)
			options.shadow.setVisible(false);
	}

	public void enableScheduler() {
		this.schedule("autoRotate", MainSingleton.sec);
	}

	public void disableScheduler() {
		this.unschedule("autoRotate");
	}

	public void autoRotate(float dt) {
		float rand = (float) Math.random();
		if (!isLocked) {
			rotate(rand * 360.0f + 360.0f);
		}
	}
}
