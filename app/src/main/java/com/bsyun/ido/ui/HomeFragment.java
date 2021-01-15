package com.bsyun.ido.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bsyun.ido.R;
import com.bsyun.ido.adapter.MessageAdapter;
import com.bsyun.ido.entity.DataBeans;
import com.bsyun.ido.utils.BSLinearLayoutManager;
import com.bsyun.ido.utils.CommmonUtil;
import com.bsyun.ido.utils.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
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
        filter.addAction(CommmonUtil.ACTION_NOTIFY);
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
                    Log.e(TAG, "onReceive: " + dataBeans.toString());
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(messageReceiver);
        getActivity().unregisterReceiver(mNetworkReceiver);
    }
}
