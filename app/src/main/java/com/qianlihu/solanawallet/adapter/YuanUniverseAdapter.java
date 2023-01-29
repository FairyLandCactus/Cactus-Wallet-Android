package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   :
 * version: 1.0.0
 */
public class YuanUniverseAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public YuanUniverseAdapter(@Nullable List<String> data) {
        super(R.layout.item_yuan_universe, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_name, item);
    }
}
