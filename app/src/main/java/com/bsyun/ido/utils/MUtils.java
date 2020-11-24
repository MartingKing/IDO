package com.bsyun.ido.utils;

/**
 * Created by yaojian on 2019/1/23 17:19
 */
public class MUtils {

    private static long lastClickTime;

    /**
     * 判断事件出发时间间隔是否超过预定值
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(400);
    }

    /**
     * 判断事件出发时间间隔是否超过预定值
     */
    public static boolean isFastDoubleClick(int delay) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delay) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
