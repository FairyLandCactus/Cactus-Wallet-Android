package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SearchHistoryDB;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   :
 * version: 1.0.0
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistoryDB, BaseViewHolder> {

    public SearchHistoryAdapter(@Nullable List<SearchHistoryDB> data) {
        super(R.layout.item_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchHistoryDB db) {
        helper.setText(R.id.tv_content, db.getContent());
    }
}
