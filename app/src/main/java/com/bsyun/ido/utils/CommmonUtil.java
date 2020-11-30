package com.bsyun.ido.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CommmonUtil {

    public static final String app_mode_key = "is_night_mode";
    public static final String qq = "com.tencent.mobileqq";
    public static final String weihat = "com.tencent.mm";
    public static final String dingding = "com.alibaba.android.rimet";
    public static final String feishu = "com.ss.android.lark";
    public static final String knock = "com.aimi.knock";
    public static final String xiaomi = "com.ss.android.lark.kami";
    public static final String ACTION_NOTIFY = "com.ido.notification";

    public static final String base_url = "http://172.18.20.117:8000";
    public static final String msg_url = "/msg/im_add";
    public static final String[] pkglist = {qq, weihat, dingding, feishu, knock, xiaomi};

    public static String getDefaultStr(String s, String v) {
        return TextUtils.isEmpty(s) ? v : s;
    }

    public static String getAppName(String pkg) {
        String app = "";
        switch (pkg) {
            case CommmonUtil.qq:
                app = "QQ";
                break;
            case CommmonUtil.dingding:
                app = "钉钉";
                break;
            case CommmonUtil.feishu:
                app = "飞书";
                break;
            case CommmonUtil.knock:
                app = "knock";
                break;
            case CommmonUtil.weihat:
                app = "微信";
                break;
            case CommmonUtil.xiaomi:
                app = "小米办公";
                break;
        }
        return app;
    }

    /**
     * 群聊Q
     * 2020-11-03 15:20:02.941 29577-29577/com.bsyun.ido I/DHD: notificationTitle 测试 (12条新消息)
     * 2020-11-03 15:20:02.941 29577-29577/com.bsyun.ido I/DHD: notificationText 湘农有品: 222
     * 2020-11-03 15:20:02.941 29577-29577/com.bsyun.ido I/DHD: notificationPkg com.tencent.mobileqq
     * 2020-11-03 15:20:02.942 29577-29577/com.bsyun.ido I/DHD: getPostTime 2020-11-03 15:20:02
     * <p>
     * <p>
     * <p>
     * 单聊qq
     * 2020-11-03 15:20:35.964 29577-29577/com.bsyun.ido I/DHD: notificationTitle 湘农有品 (12条新消息)
     * 2020-11-03 15:20:35.964 29577-29577/com.bsyun.ido I/DHD: notificationText 12121
     * 2020-11-03 15:20:35.964 29577-29577/com.bsyun.ido I/DHD: notificationPkg com.tencent.mobileqq
     * 2020-11-03 15:20:35.965 29577-29577/com.bsyun.ido I/DHD: getPostTime 2020-11-03 15:20:35
     * <p>
     * <p>
     * 单聊微信
     * 2020-11-03 15:21:06.076 29577-29577/com.bsyun.ido I/DHD: notificationTitle 美猴王
     * 2020-11-03 15:21:06.076 29577-29577/com.bsyun.ido I/DHD: notificationText [6条]美猴王: 2222
     * 2020-11-03 15:21:06.076 29577-29577/com.bsyun.ido I/DHD: notificationPkg com.tencent.mm
     * 2020-11-03 15:21:06.079 29577-29577/com.bsyun.ido I/DHD: getPostTime 2020-11-03 15:21:06
     * <p>
     * <p>
     * 群聊微信
     * 2020-11-03 15:21:24.195 29577-29577/com.bsyun.ido I/DHD: notificationTitle 测试
     * 2020-11-03 15:21:24.195 29577-29577/com.bsyun.ido I/DHD: notificationText [4条]美猴王: 898989
     * 2020-11-03 15:21:24.195 29577-29577/com.bsyun.ido I/DHD: notificationPkg com.tencent.mm
     * 2020-11-03 15:21:24.198 29577-29577/com.bsyun.ido I/DHD: getPostTime 2020-11-03 15:21:24
     */
    public static List<String> getFriendGroupName(String notificationText, String notificationTitle) {
        List<String> names = new ArrayList<>();
        String friendName = "";
        String groupName = "";
        String msg = "";
        if (notificationText != null) {
            if (notificationText.contains(":")) {
                String[] split = notificationText.split(":");
                msg = split[1];
                friendName = split[0];
                if (friendName.contains("]")) {
                    friendName = friendName.substring(friendName.indexOf("]") + 1);
                }
                if (!notificationTitle.contains(friendName)) {
                    groupName = notificationTitle;
                    if (groupName.contains("新消息")) {
//                    Log.e("DHD", "getFriendGroupName: "+groupName.indexOf("("));
                        groupName = groupName.substring(0, groupName.indexOf("("));
                    }
                }
            } else {
                friendName = notificationTitle;
                msg = notificationText;
                if (friendName.contains("新消息")) {
                    friendName = friendName.substring(0, friendName.indexOf("("));
                }
            }
            names.add(0, friendName);
            names.add(1, groupName);
            names.add(2, msg);
        } else {
            names.add(0, notificationTitle);
            names.add(1, notificationTitle);
            names.add(2, notificationText);
        }
        return names;
    }

}
