package com.qianlihu.solanawallet.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AddWalletActivity;
import com.qianlihu.solanawallet.adapter.ChainNameAdapter;
import com.qianlihu.solanawallet.adapter.MyWalletAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.bean.ChainNameBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.wallet.IPSelectWallet;
import com.qianlihu.solanawallet.wallet_bean.DappTypeBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.xuexiang.xutil.resource.ResUtils.getString;

/**
 * @Author: duan
 * @Date: 2021/8/30
 * @Description: 弹窗工具
 *
 */
public class DialogUtils {

    /**
     * 通用弹窗
     * @param context
     * @param contentView
     * @return
     */
    public static Dialog myDialog (Context context, View contentView) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.dimAmount =0.6f;
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 新人注册奖励弹窗
     * @param context
     * @param contentView
     * @return
     */
    public static Dialog homeNewParentRewardDialog (Context context, View contentView) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.dimAmount =0.6f;
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.dismiss();
        return dialog;
    }

    public static void changeTip(Activity activity, String cotent, MaterialDialog.SingleButtonCallback callback, MaterialDialog.SingleButtonCallback cancelCallback) {
        new MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.reminder))
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.black)
                .contentColorRes(R.color.black)
                .content(cotent)
                .onPositive(callback)
                .onNegative(cancelCallback)
                .cancelable(false)
                .positiveText(activity.getString(R.string.confirm))
                .negativeText(activity.getString(R.string.cancel))
                .show();
    }

    public static void changeTip2(Activity activity, String cotent, MaterialDialog.SingleButtonCallback moreCallback,  MaterialDialog.SingleButtonCallback callback, MaterialDialog.SingleButtonCallback cancelCallback) {
        new MaterialDialog.Builder(activity)
                .title(activity.getString(R.string.reminder))
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.black)
                .contentColorRes(R.color.black)
                .content(cotent)
                .onPositive(callback)
                .onNegative(cancelCallback)
                .onNeutral(moreCallback)
                .cancelable(false)
                .positiveText(activity.getString(R.string.delete))
                .negativeText(activity.getString(R.string.cancel))
                .neutralText(activity.getString(R.string.shield))
                .show();
    }

    public static void junmpDappDialog(Context context, int flag, DappTypeBean.DataBean.RowsBean bean) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_dapp_risk_tip, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvInfo = view.findViewById(R.id.tv_info);
        TextView tvWebsit = view.findViewById(R.id.tv_website);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvTelegram = view.findViewById(R.id.tv_telegram);
        TextView tvTwitter = view.findViewById(R.id.tv_twitter);
        TextView tvRiskTip = view.findViewById(R.id.tv_risk_tip);
        LinearLayout llTelegram = view.findViewById(R.id.ll_telegram);
        LinearLayout llTwitter = view.findViewById(R.id.ll_twitter);
        Button btnGotIt = view.findViewById(R.id.btn_got_it);

        ivClose.setOnClickListener(v -> dialog.dismiss());

        tvName.setText(bean.getName());

        tvWebsit.setText(bean.getWebsite());

        tvInfo.setText(bean.getIntroduce());
        tvRiskTip.setText(bean.getTip());
        if (!TextUtils.isEmpty(bean.getTelegram())) {
            llTelegram.setVisibility(View.VISIBLE);
            tvTelegram.setText(bean.getTelegram());
        }

        if (!TextUtils.isEmpty(bean.getTwitter())) {
            llTwitter.setVisibility(View.VISIBLE);
            tvTwitter.setText(bean.getTwitter());
        }

        tvWebsit.setOnClickListener(v -> ClickCopyUtils.copy(context, bean.getWebsite()));
//        tvEmail.setOnClickListener(v -> ClickCopyUtils.copy(context, ""));
        tvTelegram.setOnClickListener(v -> ClickCopyUtils.copy(context, bean.getTelegram()));
        tvTwitter.setOnClickListener(v -> ClickCopyUtils.copy(context, bean.getTwitter()));

        btnGotIt.setOnClickListener(v -> {
            dialog.dismiss();
            LoadWebPageUtils.activityIntent(context, flag, bean.getName(), bean.getWebsite());
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    public static void collectDialog(Context context, DappTypeBean.DataBean.RowsBean bean) {

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_dapp_collect, null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView title = view.findViewById(R.id.tv_title);
        TextView tvCopy = view.findViewById(R.id.tv_copy);
        TextView tvCollect = view.findViewById(R.id.tv_collect);
        tvCancel.setOnClickListener(v -> dialog.dismiss());
        title.setText(context.getString(R.string.add) + bean.getName());
        tvCopy.setOnClickListener(v -> {
            dialog.dismiss();
            ClickCopyUtils.copy(context, bean.getWebsite());
        });

        tvCollect.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    //我的钱包
    public static void selectMyWallet(Context context, String mWalletType, IPSelectWallet selectWallet) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_my_wallet, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvMyWallet = view.findViewById(R.id.rv_my_wallet);
        RecyclerView rvMainChain = view.findViewById(R.id.rv_main_chain);
        Button btnManageWallet = view.findViewById(R.id.btn_manage_wallet);
        View viewLine = view.findViewById(R.id.view_line);
        Button btnAddWallet = view.findViewById(R.id.btn_add_wallet);
        final List<WalletBean>[] list = new List[]{LitePal.where("walletType = ? and mainChain = ?", mWalletType, Constant.SOL_CHAIN).find(WalletBean.class)};
        rvMyWallet.setLayoutManager(new LinearLayoutManager(context));
        if (list[0].size() > 0) {
            String[] mChianName = WalletResource.mChainName;
            int[] mTypeUnIcon = WalletResource.mTokenTypeUnIcon;
            int[] mTypeIcon = WalletResource.mTokenTypeIcon;
            List<ChainNameBean> beanList = new ArrayList<>();
            for (int i = 0; i < mTypeUnIcon.length; i++) {
                ChainNameBean typeBean = new ChainNameBean();
                typeBean.setIcon(mTypeUnIcon[i]);
                typeBean.setUnIcon(mTypeIcon[i]);
                typeBean.setChain(mChianName[i]);
                beanList.add(typeBean);
            }
            ChainNameAdapter mTypeAdapter = new ChainNameAdapter(beanList);
            rvMainChain.setLayoutManager(new LinearLayoutManager(context));
            rvMainChain.setAdapter(mTypeAdapter);

            mTypeAdapter.select(0);

            final MyWalletAdapter[] myWalletAdapter = {new MyWalletAdapter(list[0])};
            rvMyWallet.setAdapter(myWalletAdapter[0]);
            mTypeAdapter.setOnItemClickListener((adapter, view12, position) -> {
                list[0] = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChianName[position]).find(WalletBean.class);
                mTypeAdapter.select(position);
                if (list[0].size() != 0) {
                    myWalletAdapter[0] = new MyWalletAdapter(list[0]);
                    rvMyWallet.setAdapter(myWalletAdapter[0]);
                    myWalletAdapter[0].notifyDataSetChanged();
                    itemCheckWallet(myWalletAdapter, list, dialog, context, selectWallet);
                    for (int i = 0; i < list[0].size(); i++) {
                        int isSelsct = list[0].get(i).getSelect();
                        if (isSelsct == 1) {
                            myWalletAdapter[0].selectItem(i);
                        }
                    }
                } else {
                    if (position > 2) {
                        rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.not_yet_open))));
                        return;
                    }
                    rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet_data))));
                }
            });
            itemCheckWallet(myWalletAdapter, list, dialog, context, selectWallet);
        } else {
            viewLine.setVisibility(View.GONE);
            rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet_data))));
        }

        ivClose.setOnClickListener(v -> {
            selectWallet.onDismiss();
            dialog.dismiss();
        });

        btnAddWallet.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("walletType", mWalletType);
            intent.setClass(context, AddWalletActivity.class);
            context.startActivity(intent);
        });

        btnManageWallet.setVisibility(View.GONE);
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    //钱包列表选项点击事件
    private static void itemCheckWallet(MyWalletAdapter[] myWalletAdapter, List<WalletBean>[] list, BottomSheetDialog dialog, Context context, IPSelectWallet selectWallet) {
        myWalletAdapter[0].setOnItemClickListener((adapter, view, position) -> {
            if (RepeatClickUtil.isFastDoubleClick()) {
                Toasty.info(context,getString(R.string.operate)).show();
                return;
            }
            selectWallet.onSelectWallet(list[0].get(position));
            dialog.dismiss();
        });
    }
}
