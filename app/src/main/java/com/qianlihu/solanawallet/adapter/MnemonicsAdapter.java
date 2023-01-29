package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 助记词适配器
 * version: 1.0.0
 */
public class MnemonicsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MnemonicsAdapter(@Nullable List<String> data) {
        super(R.layout.item_mnemonics, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int index = helper.getLayoutPosition() + 1;
        helper.setText(R.id.tv_positon, index + "");
        helper.setText(R.id.tv_word, item);
    }
}
