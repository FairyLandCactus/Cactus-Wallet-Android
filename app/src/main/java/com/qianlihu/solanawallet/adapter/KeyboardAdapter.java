package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.TokenTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/22 11:11
 * desc   :  密码键盘适配器
 * version: 1.0.0
 */
public class KeyboardAdapter extends BaseQuickAdapter<TokenTypeBean, BaseViewHolder> {
    public KeyboardAdapter(@Nullable List<TokenTypeBean> data) {
        super(R.layout.item_add_token_select_main_chain, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TokenTypeBean item) {
        helper.setText(R.id.tv_name, item.getTokenName());
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ivIcon.setImageResource(item.getIcon());

    }
}
