package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   : 排行列表适配器
 * version: 1.0.0
 */
public class DappRankListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int listSize = 0;

    public DappRankListAdapter(@Nullable List<String> data) {
        super(R.layout.item_dapp_right_list, data);
        this.listSize = data.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);

        View line = helper.getView(R.id.view_line);
        if (helper.getLayoutPosition() == (listSize-1)) {
            line.setVisibility(View.GONE);
        }

    }
}
