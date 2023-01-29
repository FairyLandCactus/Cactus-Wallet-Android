package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   :
 * version: 1.0.0
 */
public class HelpCenterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HelpCenterAdapter(@Nullable List<String> data) {
        super(R.layout.item_help_center, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_title, mContext.getString(R.string.one_star_per_week)+item);
        RecyclerView rvChildCenter = helper.getView(R.id.rv_child_center);
        View line = helper.getView(R.id.view_liness);
        int position = helper.getLayoutPosition();
        if (position == 1) {
            line.setBackgroundColor(mContext.getResources().getColor(R.color.txt_09C8A1));
        } else if (position == 2) {
            line.setBackgroundColor(mContext.getResources().getColor(R.color.redEB));
        } else if (position == 3) {
            line.setBackgroundColor(mContext.getResources().getColor(R.color.txt_815BEC));
        } else if (position == 4) {
            line.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
        }

        List<String> list = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            list.add(i + "");
        }

        rvChildCenter.setLayoutManager(new LinearLayoutManager(mContext));
        rvChildCenter.setAdapter(new HelpCenterChildAdapter(list));
    }
}
