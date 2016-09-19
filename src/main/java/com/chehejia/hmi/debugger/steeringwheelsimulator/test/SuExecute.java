package com.chehejia.hmi.debugger.steeringwheelsimulator.test;

import android.util.Log;

import java.io.DataOutputStream;

/**
 *
 */
public class SuExecute {
    private static final String TAG = "SuExecute";

    public static void execCmd(String cmd, long wait) {
        Thread exe = new SuCmdThread(cmd);
        exe.start();
        long old = System.currentTimeMillis();
        try {
            if (wait > 0) {
                exe.join(wait);
            } else if (wait == -1) {
                exe.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time = (System.currentTimeMillis() - old) / 1000;
        Log.i(TAG, "exec cmd time : " + time + "S");
    }

    public static void execCmd(String cmd) {
        Process process = null;
        DataOutputStream os = null;
//        DataInputStream is = null;
        try {
            Log.i(TAG, "enter execCmd: ");

            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd);
            //input enter
            if (!cmd.endsWith("\n")) {
                os.writeBytes("\n");
            }
            os.flush();
//            os.writeBytes("exit\n");
//            os.flush();
            process.waitFor();
            int iRet = process.exitValue();
            Log.i(TAG, "execCmd: " + cmd + ", result: " + iRet);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

//                if (is != null) {
//                    is.close();
//                }
                process.destroy();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * execute cmd in work thread.
     *
     * @author archermind
     */
    static class SuCmdThread extends Thread {
        String mCmd = "";

        public SuCmdThread(String cmd) {
            mCmd = cmd;
        }

        @Override
        public void run() {
            execCmd(mCmd);
        }
    }
}
