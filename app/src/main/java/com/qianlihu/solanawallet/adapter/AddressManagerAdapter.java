package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.AddressManagerDB;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 地址管理适配器
 * version: 1.0.0
 */
public class AddressManagerAdapter extends BaseQuickAdapter<AddressManagerDB, BaseViewHolder> {
    public AddressManagerAdapter(@Nullable List<AddressManagerDB> data) {
        super(R.layout.item_address_manager, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressManagerDB data) {
        helper.setText(R.id.tv_name, data.getName());
        helper.setText(R.id.tv_address, data.getAddress());
    }
}
