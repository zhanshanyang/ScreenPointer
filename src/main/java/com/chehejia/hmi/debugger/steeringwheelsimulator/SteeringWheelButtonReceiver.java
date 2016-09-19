
package com.chehejia.hmi.debugger.steeringwheelsimulator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLIN;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINAC;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINBack;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINCall;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINDirection;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINHome;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINMap;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINMessage;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINMusics;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINNext;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINPre;
import com.chehejia.hmi.debugger.steeringwheelsimulator.key.KeyCodeLINVoice;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chj1300 on 2016/6/22.
 * <p/>
 * 该广播接收者，接收的是键盘上的按键发出的事件广播
 * <p/>
 * action:com.chehejia.hmi.a01.action.BUTTON_ACTION.
 */
public class SteeringWheelButtonReceiver extends BroadcastReceiver {
    /**
     * 事件action.
     */
    public static final String LIN_BUTTON_ACTION = "com.chehejia.hmi.a01.action.BUTTON_ACTION";
//    private static final int LONG_PRESS_DELAY = 1000;
//     TODO: 2016/6/22 double click button event
//    private static long mLastClickTime = 0;

    /**
     * 缓存event对象.
     */
    private Map<Integer, KeyCodeLIN> keyCodeLINMap;

    /**
     * 本省intent.
     *
     * @param keycode code
     * @return intent
     */
    public static Intent makeIntent(int keycode) {
        Intent intent = new Intent();
        intent.setAction(LIN_BUTTON_ACTION);
        Bundle extras = new Bundle();
        extras.putInt("keycode", keycode);
        intent.putExtras(extras);
        return intent;
    }

    /**
     * @param context 上下文
     * @param intent intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (LIN_BUTTON_ACTION.equals(intentAction)) {
            if (keyCodeLINMap == null) {
                keyCodeLINMap = new HashMap<>();
            }
            Bundle extras = intent.getExtras();
            int keycode = extras.getInt("keycode");
//            long eventtime = System.currentTimeMillis();
            KeyCodeLIN keyCodeLIN;
            if (keyCodeLINMap.containsKey(keycode)) {
                keyCodeLIN = keyCodeLINMap.get(keycode);
            } else {
                keyCodeLIN = getKeyCodeLIN(keycode);
                if (keyCodeLIN != null) {
                    keyCodeLINMap.put(keycode, keyCodeLIN);
                }
            }

            if (keyCodeLIN != null) {
                // TODO: 2016/6/22 发送广播
                EventBus.getDefault().post(keyCodeLIN);
                if (isOrderedBroadcast()) {
                    abortBroadcast();
                }
            }
        }
    }

    /**
     * 匹配时间对象.
     *
     * @param keycode code
     * @return event对象
     */
    private KeyCodeLIN getKeyCodeLIN(int keycode) {
        KeyCodeLIN keyCodeLIN;
        switch (keycode) {
            case KEYCODE_LIN_LEFT:
            case KEYCODE_LIN_UP:
            case KEYCODE_LIN_RIGHT:
            case KEYCODE_LIN_DOWN:
            case KEYCODE_LIN_OK:
                keyCodeLIN = new KeyCodeLINDirection(keycode);
                break;
            case KEYCODE_LIN_PRE:
                keyCodeLIN = new KeyCodeLINPre();
                break;
            case KEYCODE_LIN_NEXT:
                keyCodeLIN = new KeyCodeLINNext();
                break;
            case KEYCODE_LIN_CALL:
                keyCodeLIN = new KeyCodeLINCall();
                break;
            case KEYCODE_LIN_VOICE:
                keyCodeLIN = new KeyCodeLINVoice();
                break;
            case KEYCODE_LIN_AC:
                keyCodeLIN = new KeyCodeLINAC();
                break;
            case KEYCODE_LIN_HOME:
                keyCodeLIN = new KeyCodeLINHome();
                break;
            case KEYCODE_LIN_BACK:
                keyCodeLIN = new KeyCodeLINBack();
                break;
            case KEYCODE_LIN_MAP:
                keyCodeLIN = new KeyCodeLINMap();
                break;
            case KEYCODE_LIN_MESSAGE:
                keyCodeLIN = new KeyCodeLINMessage();
                break;
            case KEYCODE_LIN_MUSICS:
                keyCodeLIN = new KeyCodeLINMusics();
                break;
            default:
                keyCodeLIN = null;
                break;
        }
        return keyCodeLIN;
    }

    /**
     * 向左.
     */
    public static final int KEYCODE_LIN_LEFT = 1;
    /**
     * 向上.
     */
    public static final int KEYCODE_LIN_UP = 2;
    /**
     * 向右.
     */
    public static final int KEYCODE_LIN_RIGHT = 3;
    /**
     * 向下.
     */
    public static final int KEYCODE_LIN_DOWN = 4;
    /**
     * ok确定.
     */
    public static final int KEYCODE_LIN_OK = 5;
    /**
     * 上一个.
     */
    public static final int KEYCODE_LIN_PRE = 6;
    /**
     * 下一个.
     */
    public static final int KEYCODE_LIN_NEXT = 7;
    /**
     * 打电话.
     */
    public static final int KEYCODE_LIN_CALL = 8;
    /**
     * 语音.
     */
    public static final int KEYCODE_LIN_VOICE = 9;
    /**
     * 空调设置按钮.
     */
    public static final int KEYCODE_LIN_AC = 10;
    /**
     * 主界面按钮.
     */
    public static final int KEYCODE_LIN_HOME = 11;
    /**
     * 返回按钮.
     */
    public static final int KEYCODE_LIN_BACK = 12;
    /**
     * 地图按钮.
     */
    public static final int KEYCODE_LIN_MAP = 13;
    /**
     * 消息按钮.
     */
    public static final int KEYCODE_LIN_MESSAGE = 14;
    /**
     * 音乐切换按钮.
     */
    public static final int KEYCODE_LIN_MUSICS = 15;

}
