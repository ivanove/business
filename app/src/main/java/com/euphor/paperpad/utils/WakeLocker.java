/**
 * 
 */
package com.euphor.paperpad.utils;


import android.content.Context;
import android.os.PowerManager;
 
/**
 * @author euphordev02
 *
 */
public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;
 
    public static void acquire(Context context) {
        if (wakeLock != null) wakeLock.release();
 
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
        wakeLock.acquire();
    }
 
    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}