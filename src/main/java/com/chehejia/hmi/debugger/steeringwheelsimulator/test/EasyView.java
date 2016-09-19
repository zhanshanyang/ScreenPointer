package com.chehejia.hmi.debugger.steeringwheelsimulator.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;


import com.chehejia.hmi.debugger.steeringwheelsimulator.R;
import com.chehejia.hmi.debugger.steeringwheelsimulator.SteeringWheelButtonReceiver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 展示的界面.
 */
public class EasyView {
    private static final String TAG = "EasyView";

    private Context mContext;
    private Handler mHandler;
    //    private Intent mHomeIntent;
    private View mSubView;
    private View mPanel;
    private View mPoint;

    public EasyView(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        initAnimation();
        initPoint();
    }

    View getPoint() {
        return mPoint;
    }

    View getPanel() {
        return mPanel;
    }

    void initPoint() {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN, null);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        LayoutInflater in = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = in.inflate(R.layout.point, null);
        mPoint = view.findViewById(R.id.btn_point);
        mPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!PointServices.sHasMoved) {
                    if (mPanel == null) {
                        initPanel();
                    }
                    mHandler.sendEmptyMessage(PointServices.MSG_SHOW_PANEL);
                }
            }
        });
    }

    void initPanel() {
        LayoutInflater in = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // panel view
        mPanel = in.inflate(R.layout.point_panel, null);
        mSubView = mPanel.findViewById(R.id.lyt_panel);
        mPanel.findViewById(R.id.btn_left_ok).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_left).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_up).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_right).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_down).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_settings).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_pre).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_call).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_next).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_voice).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_ac).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_home).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_left_back).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_map).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_message).setOnClickListener(mClickListener);
        mPanel.findViewById(R.id.btn_right_music).setOnClickListener(mClickListener);

        mPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        View view = mPanel.findViewById(R.id.lyt_panel);
                        int x = (int) view.getX();
                        int y = (int) view.getY();
                        int w = view.getWidth();
                        int h = view.getHeight();
                        final Rect rect = new Rect(x, y, x + w, y + h);
                        if (!rect.contains((int) event.getX(), (int) event.getY())) {
                            changeStatus(true);
                        }
                        break;
                    }
                }
                return false;
            }
        });
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int keycode = 0;
            int vid = v.getId();
            if (R.id.btn_settings == vid) {
                changeStatus(true);
            } else {

                Map<Integer, Integer> map = new HashMap<>();
                map.put(R.id.btn_left_ok, SteeringWheelButtonReceiver.KEYCODE_LIN_OK);
                map.put(R.id.btn_left_left, SteeringWheelButtonReceiver.KEYCODE_LIN_LEFT);
                map.put(R.id.btn_left_up, SteeringWheelButtonReceiver.KEYCODE_LIN_UP);
                map.put(R.id.btn_left_right, SteeringWheelButtonReceiver.KEYCODE_LIN_RIGHT);
                map.put(R.id.btn_left_down, SteeringWheelButtonReceiver.KEYCODE_LIN_DOWN);
                map.put(R.id.btn_left_ac, SteeringWheelButtonReceiver.KEYCODE_LIN_AC);
                map.put(R.id.btn_left_home, SteeringWheelButtonReceiver.KEYCODE_LIN_HOME);
                map.put(R.id.btn_left_back, SteeringWheelButtonReceiver.KEYCODE_LIN_BACK);
                map.put(R.id.btn_right_pre, SteeringWheelButtonReceiver.KEYCODE_LIN_PRE);
                map.put(R.id.btn_right_call, SteeringWheelButtonReceiver.KEYCODE_LIN_CALL);
                map.put(R.id.btn_right_next, SteeringWheelButtonReceiver.KEYCODE_LIN_NEXT);
                map.put(R.id.btn_right_voice, SteeringWheelButtonReceiver.KEYCODE_LIN_VOICE);
                map.put(R.id.btn_right_map, SteeringWheelButtonReceiver.KEYCODE_LIN_MAP);
                map.put(R.id.btn_right_message, SteeringWheelButtonReceiver.KEYCODE_LIN_MESSAGE);
                map.put(R.id.btn_right_music, SteeringWheelButtonReceiver.KEYCODE_LIN_MUSICS);

                if (map.containsKey(vid)) {
                    keycode = map.get(vid);
                }
            }

            if (keycode != 0) {
                Intent intent = SteeringWheelButtonReceiver.makeIntent(keycode);
                mContext.sendBroadcast(intent);
            }
        }
    };

    /**
     * show point or panel.
     *
     * @param showPoint 点
     */
    void changeStatus(boolean showPoint) {
        if (showPoint) {
            mSubView.startAnimation(mHideAnimation);
        } else {
            mPanel.setVisibility(View.VISIBLE);
            mSubView.startAnimation(mShowAnimation);
        }
    }

    private void sendKeyEvent(int keyCode) {
        long now = SystemClock.uptimeMillis();
        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));

        injectKeyEvent(new KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0, 0,
                KeyCharacterMap.VIRTUAL_KEYBOARD, 0, 0, InputDevice.SOURCE_KEYBOARD));
    }

    // execute cmd by shell
    private void execCmd(int ketEvent) {
        String cmd = "input keyevent " + ketEvent;
        SuExecute.execCmd(cmd, 0);
    }

    /**
     * inject a specify keyevent.
     *
     * @param event 事件
     */
    private void injectKeyEvent(KeyEvent event) {
        // test
        // INJECT_INPUT_EVENT_MODE_ASYNC
        // INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT
        // InputManager.getInstance().injectInputEvent(event,
        // InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }

    private AnimationSet mShowAnimation;
    private AnimationSet mHideAnimation;

    void initAnimation() {
        ScaleAnimation saShow = new ScaleAnimation(0.3f, 1f, 0.3f, 1f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ScaleAnimation saHide = new ScaleAnimation(1f, 0.3f, 1f, 0.3f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation aaShow = new AlphaAnimation(0.3f, 1);
        AlphaAnimation aaHide = new AlphaAnimation(1, 0.3f);

        mShowAnimation = new AnimationSet(false);
        mHideAnimation = new AnimationSet(false);

        mShowAnimation.addAnimation(aaShow);
        mShowAnimation.addAnimation(saShow);
        mHideAnimation.addAnimation(aaHide);
        mHideAnimation.addAnimation(saHide);
        mShowAnimation.setDuration(200);
        mHideAnimation.setDuration(200);

        mShowAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPoint.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mHideAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPanel.setVisibility(View.GONE);
                mPoint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
