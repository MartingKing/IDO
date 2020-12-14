package com.bsyun.ido.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bsyun.ido.R;

public class GalleryFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        TextView textView = root.findViewById(R.id.text_gallery);
        textView.setText("消息数据上传默认是开启的，如果想关闭，就点一下关闭就行了，关闭后只是后台数据不上传，但是消息还是在接收；如果需要重新开启，点击一次或者多次开启都行，开启后收到的消息开始上传服务器");
        return root;
    }
}