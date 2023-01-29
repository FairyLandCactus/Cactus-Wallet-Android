package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SolanaTransferRecordBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.WalletUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 转账记录适配器
 * version: 1.0.0
 */
public class SolTransderRecordAdapter extends BaseQuickAdapter<SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean, BaseViewHolder> {

    private String mPuk = "";
    private String name = "";
    private double mUsd = 0.0;

    public SolTransderRecordAdapter(@Nullable List<SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean> data, String puk, String name, double usd) {
        super(R.layout.item_transfer_record, data);
        this.mPuk = puk;
        this.name = name;
        this.mUsd = usd;
    }

    @Override
    protected void convert(BaseViewHolder helper, SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean db) {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        String from = db.getSrc();
        String to = db.getDst();
        long longTime = db.getBlockTime();
        String amount = db.getLamport();
        int decimals = db.getDecimals();
        double amountDb = 0.0;
        if (!TextUtils.isEmpty(amount)) {
            BigInteger bg = new BigInteger(amount);
            amountDb = bg.doubleValue() / Math.pow(10, decimals);
        }
        String amountStr = MyUtils.decimalFormat6(amountDb);
        ImageView ivIcon = helper.getView(R.id.iv_icon);

        if (!to.equals(mPuk)) {
            ivIcon.setImageResource(R.mipmap.sol_page_transfer);
            helper.setText(R.id.tv_sol_money, "-" + amountStr);
            helper.setText(R.id.tv_transfer_address, "Transfer  " + to);
        } else {
            ivIcon.setImageResource(R.mipmap.sol_page_receive);
            helper.setText(R.id.tv_sol_money, "+" +amountStr);
            helper.setText(R.id.tv_transfer_address, "Collect    " + from);
        }

        if (name.equals(Constant.SOL_CHAIN)) {
            double totalUsdt = amountDb * mUsd;
            helper.setText(R.id.tv_usdt, "$" + MyUtils.decimalFormat6(totalUsdt));
        }

        if (type.equals("en")) {
            helper.setText(R.id.tv_time,  DateTimeUtils.convertTimestamp2DateEnglish(longTime));
        } else {
            helper.setText(R.id.tv_time,  DateTimeUtils.convertTimestamp2Date(longTime));
        }
    }
}
