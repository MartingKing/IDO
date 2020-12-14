package com.bsyun.ido;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bsyun.ido.entity.DataBeans;
import com.bsyun.ido.utils.CommmonUtil;

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

    private static final String TAG = "NotifiMsgService";
    private boolean troggle = true;
    private MessageReceiver messageReceiver;
    private DataBeans dataBeans;
    private String notificationPkg;
    private String notificationText;
    private String notificationTitle;
    private long notificationTime;
    private List<DataBeans> dataBeansList = new ArrayList<>();
    private OkHttpClient client;
    private MediaType mediaType;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(6000, TimeUnit.MILLISECONDS)
                .build();
        mediaType = MediaType.parse("application/json;charset=UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }


    // 在收到消息时触发

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;
        notificationPkg = sbn.getPackageName();
        notificationTitle = extras.getString(Notification.EXTRA_TITLE);
        notificationText = extras.getString(Notification.EXTRA_TEXT);
        notificationTime = sbn.getPostTime();
        sedBrodcastToHome(notificationPkg, notificationTitle, notificationText, notificationTime);
        receiveBroadcastFromHome();
        List<String> list = Arrays.asList(CommmonUtil.pkglist);
        if (list.contains(notificationPkg)) {
            dataBeans = new DataBeans();
            dataBeans.setMsg(CommmonUtil.getFriendGroupName(notificationText, notificationTitle).get(2));
            dataBeans.setApp(CommmonUtil.getAppName(notificationPkg));
            dataBeans.setName(CommmonUtil.getFriendGroupName(notificationText, notificationTitle).get(0));
            dataBeans.setGroup(CommmonUtil.getFriendGroupName(notificationText, notificationTitle).get(1));
            dataBeans.setRecv_time(notificationTime);
            Log.e(TAG, "onNotificationPosted: " + troggle);
            if (troggle) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postMsg(dataBeans);
                    }
                }).start();
            }
        }
    }

    private void receiveBroadcastFromHome() {
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommmonUtil.ACTION_START);
        registerReceiver(messageReceiver, filter);
    }

    private void sedBrodcastToHome(String notificationPkg, String notificationTitle, String notificationText, long notificationTime) {
        Intent intent = new Intent();
        intent.putExtra("notificationTitle", notificationTitle);
        intent.putExtra("notificationPkg", notificationPkg);
        intent.putExtra("notificationText", notificationText);
        intent.putExtra("notificationTime", notificationTime);
        intent.setAction(CommmonUtil.ACTION_NOTIFY);
        sendBroadcast(intent);
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                troggle = bundle.getBoolean("troggle");
            }
        }
    }

    private void postMsg(DataBeans datas) {
        dataBeansList.clear();
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
