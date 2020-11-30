package com.bsyun.ido;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bsyun.ido.entity.DataBeans;
import com.bsyun.ido.utils.CommmonUtil;
import com.bsyun.ido.utils.DateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("NewApi")
public class NotifiMsgService extends NotificationListenerService {

    private static final String TAG = "DHD";

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        // 获取接收消息APP的包名
        String notificationPkg = sbn.getPackageName();
        // 获取接收消息的抬头
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        // 获取接收消息的内容
        String notificationText = extras.getString(Notification.EXTRA_TEXT);
        long notificationTime = sbn.getPostTime();
        Log.i("DHD", "notificationTitle " + notificationTitle);
        Log.i("DHD", "notificationText " + notificationText);
        Log.i("DHD", "notificationPkg " + notificationPkg);
        Log.i("DHD", "getPostTime " + DateUtil.getDateToString(sbn.getPostTime()));
        Intent intent = new Intent();
        intent.putExtra("notificationTitle", notificationTitle);
        intent.putExtra("notificationPkg", notificationPkg);
        intent.putExtra("notificationText", notificationText);
        intent.putExtra("notificationTime", notificationTime);
        intent.setAction(CommmonUtil.ACTION_NOTIFY);
        sendBroadcast(intent);
        List<String> list = Arrays.asList(CommmonUtil.pkglist);
        if (list.contains(notificationPkg)) {
            final DataBeans dataBeans = new DataBeans();
            dataBeans.setMsg(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(2));
            dataBeans.setApp(CommmonUtil.getAppName(notificationPkg));
            dataBeans.setName(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(0));
            dataBeans.setGroup(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(1));
            dataBeans.setRecv_time(notificationTime);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    postMsg(dataBeans);
                }
            }).start();
        }
    }

    private void postMsg(DataBeans datas) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(6000, TimeUnit.MILLISECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        List<DataBeans> dataBeansList = new ArrayList<>();
        dataBeansList.add(datas);
        String jsonObject = JSON.toJSONString(dataBeansList);
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
        final Request request = new Request.Builder().url(CommmonUtil.base_url + CommmonUtil.msg_url).method("POST", requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + request.toString());
            }
        });
    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
