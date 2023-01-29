package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/6/4 16:27
 * desc   : 缺省适配器
 * version: 1.0.0
 */
public class NoDataAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public NoDataAdapter(@Nullable List<String> data) {
        super(R.layout.item_no_data, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String data) {
        holder.setText(R.id.tv_no_cntent, data + "");
    }
}
