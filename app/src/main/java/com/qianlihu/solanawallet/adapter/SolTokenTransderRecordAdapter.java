package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SolTokenTransferRecordBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.WalletUtils;

import java.math.BigInteger;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 转账记录适配器
 * version: 1.0.0
 */
public class SolTokenTransderRecordAdapter extends BaseQuickAdapter<SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean, BaseViewHolder> {

    private String name = "";
    private double usd = 0.0;

    public SolTokenTransderRecordAdapter(@Nullable List<SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean> data, String name, double usd) {
        super(R.layout.item_transfer_record, data);
        this.name = name;
        this.usd = usd;
    }

    @Override
    protected void convert(BaseViewHolder helper, SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean db) {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);

        if (db.getChange() == null) {
            Toasty.error(mContext, mContext.getString(R.string.request_abnormal)).show();
            return;
        }
        String symbol = db.getChange().getSymbol();
        long longTime = db.getBlockTime();
        String amount = db.getChange().getChangeAmount();
        int decimals = db.getChange().getDecimals();
        double amountDb = 0.0;
        if (!TextUtils.isEmpty(amount)) {
            BigInteger bg = new BigInteger(amount);
            amountDb = bg.doubleValue() / Math.pow(10, decimals);
        }
        String amountStr = MyUtils.decimalFormat6(amountDb);
        ImageView ivIcon = helper.getView(R.id.iv_icon);

        if (amountDb < 0) {
            ivIcon.setImageResource(R.mipmap.sol_page_transfer);
            helper.setText(R.id.tv_sol_money, amountStr);
        } else {
            ivIcon.setImageResource(R.mipmap.sol_page_receive);
            helper.setText(R.id.tv_sol_money, "+" + amountStr);
        }
        helper.setText(R.id.tv_transfer_address, symbol);
        if (TextUtils.isEmpty(symbol)) {
            helper.setText(R.id.tv_transfer_address, name);
        }

        double totalUsdt = amountDb * usd;
        helper.setText(R.id.tv_usdt, "$ " + MyUtils.decimalFormat6(totalUsdt));

        if (type.equals("en")) {
            helper.setText(R.id.tv_time, DateTimeUtils.convertTimestamp2DateEnglish(longTime));
        } else {
            helper.setText(R.id.tv_time, DateTimeUtils.convertTimestamp2Date(longTime));
        }
    }
}
