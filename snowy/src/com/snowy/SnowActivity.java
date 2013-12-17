package com.snowy;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

public class SnowActivity extends Activity {
	private static FrameLayout fr;
	public static int width;
	public static int height;
	private static Context context;
	private static Queue<Snow> snowQ;

	/**
	 * 변수모음
	 */
	// 최대 눈 개수
	private static int maxSnowCount = 100;
	//애니메이션 딜레이 시간
	static int delayTime = 50;
	//눈이 추가되는 간격
	static int interval = 500;
	//눈 최소 크기
	static int snowMinSize = 2;
	//눈 최대 크기
	static int snowMaxSize = 4;
	//눈 최소 속도
	static int snowMinSpeed = 1;
	//눈 최대 속도
	static int snowMaxSpeed = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snow);

		fr = (FrameLayout) findViewById(R.id.snow_layout);

		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		SnowActivity.context = getApplicationContext();

		snowQ = new ArrayBlockingQueue<Snow>(maxSnowCount);

		// 애니메이션 시작
		this.setRepeat();
	}

	// 반복실행..
	public void setRepeat() {
		// 애니메이션
		aniHdler = new AniHandler();
		aniHdler.sendMessageDelayed(new Message(), delayTime);

		// 눈 추가
		hdlr = new SnowHandler();
		hdlr.sendMessageDelayed(new Message(), interval);
	}

	/**
	 * 눈생성
	 */
	static Handler hdlr;

	static class SnowHandler extends Handler {
		public void handleMessage(Message msg) {
			makeSnow();
			hdlr.sendMessageDelayed(new Message(), interval);
		}
	}

	// 반복되며 실행..
	public static void makeSnow() {

		int startPosition = (int) (Math.random() * width);
		int size = (int) (Math.random() * snowMaxSize) + snowMinSize;
		int speed = (int) (Math.random() * snowMaxSpeed) + snowMinSpeed;

		Snow snow = new Snow(context, startPosition, size, speed);

		fr.addView(snow);
		if (!snowQ.offer(snow)) {
			Snow removedSnow = snowQ.poll();
			fr.removeView(removedSnow);
		}
	}

	/**
	 * 애니메이션 관련 변수
	 */
	static Handler aniHdler;

	static class AniHandler extends Handler {
		public void handleMessage(Message msg) {
			doRepeatedly();
			aniHdler.sendMessageDelayed(new Message(), delayTime);
		}
	}

	// 반복되며 실행..
	public static void doRepeatedly() {
		for(int i=0; i<fr.getChildCount(); i++){
			View v = fr.getChildAt(i);
			v.invalidate();
		}
	}

}
