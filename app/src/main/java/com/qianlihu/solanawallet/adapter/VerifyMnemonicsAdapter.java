package com.qianlihu.solanawallet.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;

import java.util.List;
import java.util.Map;

/**
 * author : Duan
 * date   : 2022/4/29:44
 * desc   :
 * version: 1.0.0
 */
public class VerifyMnemonicsAdapter extends BaseQuickAdapter<Map<String, Object>, BaseViewHolder> {

    public VerifyMnemonicsAdapter(@Nullable List<Map<String, Object>> data) {
        super(R.layout.item_verify_mnemonics, data);
    }

    public void select() {

    }

    @Override
    protected void convert(BaseViewHolder helper, Map<String, Object> item) {

        TextView tvMnemonics = helper.getView(R.id.tv_mnemonics);
        tvMnemonics.setText((String) item.get("mn"));
        int flag = (int) item.get("flag");
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        int textColor1 = R.color.white;

        if (!isDark) {
            textColor1 = R.color.txt_333;
        }

        if (flag == -1) {
            tvMnemonics.setTextColor(mContext.getColor(textColor1));
        } else {
            tvMnemonics.setTextColor(mContext.getColor(!isDark ? R.color.txt_CACBDB : R.color.txt_3F4155));
        }
    }
}
