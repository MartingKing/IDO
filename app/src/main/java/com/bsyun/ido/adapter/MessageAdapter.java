package com.bsyun.ido.adapter;

import com.bsyun.ido.R;
import com.bsyun.ido.entity.DataBeans;
import com.bsyun.ido.utils.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageAdapter extends BaseQuickAdapter<DataBeans, BaseViewHolder> implements LoadMoreModule {


    public MessageAdapter(int layoutResId, @Nullable List<DataBeans> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NotNull final BaseViewHolder baseViewHolder, final DataBeans dataBeans) {

        baseViewHolder.setText(R.id.tv_friendname,"好友："+dataBeans.getName());
        baseViewHolder.setText(R.id.tv_groupname,"群名："+dataBeans.getGroup());
        baseViewHolder.setText(R.id.tv_msg,"消息："+dataBeans.getMsg());
        baseViewHolder.setText(R.id.tv_time, DateUtil.getDateToString(dataBeans.getRecv_time()));
        baseViewHolder.setText(R.id.tv_pkg,"应用："+dataBeans.getApp());
    }

}
