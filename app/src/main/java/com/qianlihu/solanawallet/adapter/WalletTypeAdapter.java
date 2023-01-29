package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;

import java.util.List;
import java.util.Map;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 钱包类型适配器
 * version: 1.0.0
 */
public class WalletTypeAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

    private int selectIndex = 0;

    public WalletTypeAdapter(@Nullable List<Map<String, Object>> data) {
        super(R.layout.item_wallet_type, data);
    }

    public void selectItem(int position) {
        this.selectIndex = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, Object> map) {
        ImageView ivPic = helper.getView(R.id.iv_pic);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        helper.addOnClickListener(R.id.iv_select);
        ivPic.setImageResource((Integer) map.get("pic"));
        helper.setText(R.id.tv_type_name, (String) map.get("name"));
        helper.setText(R.id.tv_type_dec, (String) map.get("dec"));

        boolean isSelect = (boolean) map.get("select");
        int position = helper.getLayoutPosition();

        if (position == selectIndex) {
            ivSelect.setImageResource(R.mipmap.create_wallet_selected);
        } else {
            ivSelect.setImageResource(R.mipmap.create_wallet_add);
        }
    }
}
