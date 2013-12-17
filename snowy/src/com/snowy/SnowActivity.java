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
	 * ��������
	 */
	// �ִ� �� ����
	private static int maxSnowCount = 100;
	//�ִϸ��̼� ������ �ð�
	static int delayTime = 50;
	//���� �߰��Ǵ� ����
	static int interval = 500;
	//�� �ּ� ũ��
	static int snowMinSize = 2;
	//�� �ִ� ũ��
	static int snowMaxSize = 4;
	//�� �ּ� �ӵ�
	static int snowMinSpeed = 1;
	//�� �ִ� �ӵ�
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

		// �ִϸ��̼� ����
		this.setRepeat();
	}

	// �ݺ�����..
	public void setRepeat() {
		// �ִϸ��̼�
		aniHdler = new AniHandler();
		aniHdler.sendMessageDelayed(new Message(), delayTime);

		// �� �߰�
		hdlr = new SnowHandler();
		hdlr.sendMessageDelayed(new Message(), interval);
	}

	/**
	 * ������
	 */
	static Handler hdlr;

	static class SnowHandler extends Handler {
		public void handleMessage(Message msg) {
			makeSnow();
			hdlr.sendMessageDelayed(new Message(), interval);
		}
	}

	// �ݺ��Ǹ� ����..
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
	 * �ִϸ��̼� ���� ����
	 */
	static Handler aniHdler;

	static class AniHandler extends Handler {
		public void handleMessage(Message msg) {
			doRepeatedly();
			aniHdler.sendMessageDelayed(new Message(), delayTime);
		}
	}

	// �ݺ��Ǹ� ����..
	public static void doRepeatedly() {
		for(int i=0; i<fr.getChildCount(); i++){
			View v = fr.getChildAt(i);
			v.invalidate();
		}
	}

}
