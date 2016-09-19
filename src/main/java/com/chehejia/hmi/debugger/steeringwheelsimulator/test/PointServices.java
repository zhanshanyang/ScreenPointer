package com.chehejia.hmi.debugger.steeringwheelsimulator.test;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

import com.chehejia.hmi.debugger.steeringwheelsimulator.R;

/**
 *
 */
public class PointServices extends Service {
    final String TAG = "PointServices";

    public static final String SHOW_ACTION = "com.amt.easypoint.show.action";
    public static final String REMOVE_ACTION = "com.amt.easypoint.remove.action";

    private static WindowManager sWindowManager = null;
    private static View mView = null;
    private static LayoutParams sParams = null;

    private static EasyView mEasyView = null;
    private static View mPanel = null;
    private static LayoutParams sPanelParams = null;
    private static int TOUCH_OFFSET = 3;
    private final float ALPHA = 1f;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public static final int MSG_SHOW_POINT = 0x321;
    public static final int MSG_SHOW_PANEL = MSG_SHOW_POINT + 1;
    public static final int MSG_CLOSE_VIEW = MSG_SHOW_PANEL + 1;
    public static final int MSG_CLOSE_PANEL = MSG_CLOSE_VIEW + 1;
    public static final int MSG_UPDATE_VIEW = MSG_CLOSE_PANEL + 1;

    /*
    private void startMyService(boolean show) {
        String action = show ? PointServices.SHOW_ACTION : PointServices.REMOVE_ACTION;
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, PointServices.class));
        intent.setAction(action);
        this.startService(intent);
    }
     */

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SHOW_POINT: { // show point
                    addPointView();
                    break;
                }
                case MSG_UPDATE_VIEW: { // update point view
                    updateView();
                    break;
                }
                case MSG_CLOSE_VIEW: { // close point view
                    break;
                }
                case MSG_SHOW_PANEL: { // show panel
                    addPanelView();
                    break;
                }
                case MSG_CLOSE_PANEL: { // close panel
                    break;
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "PointServices.onCreate() *** ");
        init();
    }

    void init() {
        TOUCH_OFFSET = (int) this.getResources().getDimension(R.dimen.TOUCH_OFFSET);
        sWindowManager = (WindowManager) getApplicationContext()
                .getSystemService(WINDOW_SERVICE);
        // point layout params
        sParams = new LayoutParams();
        sParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        sParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        sParams.format = PixelFormat.RGBA_8888;

        sParams.width = LayoutParams.WRAP_CONTENT;
        sParams.height = LayoutParams.WRAP_CONTENT;
        sParams.alpha = ALPHA;
        int width = this.getResources().getDisplayMetrics().widthPixels;
        sCurrentX = -width / 2;
        sCurrentY = 0;
        sParams.x = (int) sCurrentX;
        sParams.y = (int) sCurrentY;

        // panel layout params
        sPanelParams = new LayoutParams();
        sPanelParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        sPanelParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        sPanelParams.width = LayoutParams.MATCH_PARENT;
        sPanelParams.height = LayoutParams.MATCH_PARENT;
        sPanelParams.alpha = ALPHA;
        sPanelParams.format = PixelFormat.RGBA_8888;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "PointServices.onDestroy() *** ");
    }

    @Override
    public int onStartCommand(Intent it, int flags, int startId) {
        Log.i(TAG, "PointServices.onStartCommand() *** ");
        if (it != null) {

            String action = it.getAction();
            Log.i(TAG, "action=" + action);

            if (SHOW_ACTION.equals(action)) {
                showPoint();
            } else if (REMOVE_ACTION.equals(action)) {
                closePointView();
            }
        }
        return super.onStartCommand(it, flags, startId);
    }

    /**
     * show point view.
     */
    private void showPoint() {
        Log.i(TAG, "PointServices.showPoint() *** ");

        if (mView == null) {
            mEasyView = new EasyView(this, mHandler);
            mView = mEasyView.getPoint();
        }
        if (mView != null) {
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (sHasMoved) {
                        return false;
                    }
                    closePointView();
                    return true;
                }
            });

            mView.setOnTouchListener(mTouchListener);

            if (!mInited) {
                mHandler.sendEmptyMessage(MSG_SHOW_POINT);
            }
            mInited = true;
        }
    }

    /**
     * click point to show panel.
     */
    void addPanelView() {
        if (mPanel == null) {
            mPanel = mEasyView.getPanel();
            sWindowManager.addView(mPanel, sPanelParams);
        }
        mEasyView.changeStatus(false);
    }

    /**
     * add point view to window service.
     */
    private void addPointView() {
        sWindowManager.addView(mView, sParams);
        // mHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
    }

    /**
     * hide point view.
     */
    private void closePointView() {
        if (mView != null) {
            sWindowManager.removeView(mView);
        }

        if (mPanel != null) {
            sWindowManager.removeView(mPanel);
        }
        mView = null;
        mPanel = null;
        mEasyView = null;
        mInited = false;
        Toast.makeText(getApplicationContext(), "bye!", Toast.LENGTH_SHORT).show();
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            float x = e.getRawX();
            float y = e.getRawY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    Log.i(TAG, "onTouch ACTION_DOWN x[" + x + "], y[" + y + "]");
                    sOldX = x;
                    sOldY = y;

                    sHasMoved = false;
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    Log.i(TAG, "onTouch ACTION_MOVE x[" + x + "], y[" + y + "]");

                    sMoveX = x - sOldX;
                    sMoveY = y - sOldY;

                    if (Math.abs(sMoveX) > TOUCH_OFFSET || Math.abs(sMoveY) > TOUCH_OFFSET) {
                        sHasMoved = true;
                    }

                    sOldX = x;
                    sOldY = y;

                    updateView();
                    break;
                }
                case MotionEvent.ACTION_OUTSIDE: {
                    Log.i(TAG, "onTouch ACTION_OUTSIDE x[" + x + "], y[" + y + "]");
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    Log.i(TAG, "onTouch ACTION_UP x[" + x + "], y[" + y + "]");
                    break;
                }
                default: {
                    Log.i(TAG, "onTouch x[" + x + "], y[" + y + "]");
                }
            }

            return false;
        }
    };

    static boolean mInited = false;

    static float sCurrentX = 0;
    static float sCurrentY = 0;

    static float sMoveX = 0;
    static float sMoveY = 0;

    static float sOldX = 0;
    static float sOldY = 0;

    public static boolean sHasMoved = false;

    private void updateView() {
        if (mView == null) {
            return;
        }

        sCurrentX += sMoveX;
        sCurrentY += sMoveY;
        sParams.x = (int) sCurrentX;
        sParams.y = (int) sCurrentY;

        Log.i(TAG, "sParams.x=" + sParams.x + ", sParams.y=" + sParams.y);

        sWindowManager.updateViewLayout(mView, sParams);
    }
}

