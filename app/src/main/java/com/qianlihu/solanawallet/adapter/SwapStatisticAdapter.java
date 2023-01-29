package com.qianlihu.solanawallet.adapter;

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
public class SwapStatisticAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SwapStatisticAdapter(@Nullable List<String> data) {
        super(R.layout.item_swap_statistical, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {

        helper.setText(R.id.tv_title, data);
        RecyclerView rvStatistic = helper.getView(R.id.rv_revenue_record);
        rvStatistic.setLayoutManager(new LinearLayoutManager(mContext));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("IUS" + i);
        }
        rvStatistic.setAdapter(new YuanUniverseAdapter(list));
    }
}
