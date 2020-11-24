package com.bsyun.ido.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bsyun.ido.R;
import com.bsyun.ido.adapter.MessageAdapter;
import com.bsyun.ido.entity.DataBeans;
import com.bsyun.ido.utils.BSLinearLayoutManager;
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

public class HomeFragment extends Fragment {
    private static final String TAG = "DHD";
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MessageReceiver messageReceiver;
    private NetworkReceiver mNetworkReceiver;
    private TextView net_error;
    private boolean isNetworkAvalable = true;
    List<DataBeans> messgeData = new ArrayList<>();
    int count = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        net_error = root.findViewById(R.id.net_error);
        mRecyclerView.setLayoutManager(new BSLinearLayoutManager(getContext()));
        mAdapter = new MessageAdapter(R.layout.item_msg, new ArrayList<DataBeans>());
        mRecyclerView.setAdapter(mAdapter);
        //获取消息的广播
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.bsyun.ido.NotifiMsgService");
        getActivity().registerReceiver(messageReceiver, filter);

        //获取网络状态的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mNetworkReceiver = new NetworkReceiver();
        getActivity().registerReceiver(mNetworkReceiver, intentFilter);
        return root;
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 判断网络是否可用
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                net_error.setVisibility(View.GONE);
                isNetworkAvalable = true;
            } else {
                net_error.setVisibility(View.VISIBLE);
                isNetworkAvalable = false;
            }
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            messgeData.clear();
            if (bundle != null) {
                String notificationTitle = CommmonUtil.getDefaultStr(bundle.getString("notificationTitle"), "unknow name");
                String notificationPkg = CommmonUtil.getDefaultStr(bundle.getString("notificationPkg"), "unknow app");
                String notificationText = CommmonUtil.getDefaultStr(bundle.getString("notificationText"), "unknow msg");
                long time = bundle.getLong("notificationTime", DateUtil.getCurTimeLong());
                List<String> list = Arrays.asList(CommmonUtil.pkglist);
                if (list.contains(notificationPkg)) {
                    count++;
                    final DataBeans dataBeans = new DataBeans();
                    dataBeans.setMsg(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(2));
                    dataBeans.setApp(CommmonUtil.getAppName(notificationPkg));
                    dataBeans.setName(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(0));
                    dataBeans.setGroup(CommmonUtil.getFriendGroupName(notificationText,notificationTitle).get(1));
                    dataBeans.setRecv_time(time);
                    messgeData.add(dataBeans);
                    mAdapter.addData(dataBeans);
                    mAdapter.setData(count, dataBeans);
                    Log.e("DHD", "onReceive: " + dataBeans.toString());
//                    postMsg(dataBeans);
                }
            }
        }
    }


    private void postMsg(final DataBeans datas) {
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
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
                net_error.setVisibility(View.VISIBLE);
                net_error.setText("当前消息：" + datas.toString() + "未上传成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        net_error.setVisibility(View.GONE);
                    }
                }, 2000);
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + request.toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(messageReceiver);
        getActivity().unregisterReceiver(mNetworkReceiver);
    }
}
