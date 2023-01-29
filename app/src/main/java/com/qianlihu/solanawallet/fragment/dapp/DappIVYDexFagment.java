package com.qianlihu.solanawallet.fragment.dapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.DappBscListAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.DappInfoBean;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappIVYDexFagment extends BaseFragment {

    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    public static Fragment newInstance(int id, int status) {
        DappIVYDexFagment fragment = new DappIVYDexFagment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_sol;
    }

    @Override
    public void initView() {

        List<DappInfoBean> list = new ArrayList<>();

        DappInfoBean bean5 = new DappInfoBean();
        bean5.setIcon(R.mipmap.icon_ivy);
        bean5.setName("IVYDex");
        bean5.setInfo("1");
        bean5.setDappUrl("https://web.wivyswap.com");
        bean5.setWebsite("http://www.wivyswap.com");
        bean5.setTelegram("https://t.me/zebececosystem");
        bean5.setTwitter("https://twitter.com/Zebec_HQ");
        list.add(bean5);

        DappBscListAdapter listAdapter = new DappBscListAdapter(list);
        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItem.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                junmpDappDialog(list.get(position));
            }
        });
    }

    @Override
    public void initData() {

    }

    private void junmpDappDialog(DappInfoBean bean) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_dapp_risk_tip, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvInfo = view.findViewById(R.id.tv_info);
        TextView tvWebsit = view.findViewById(R.id.tv_website);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView tvTelegram = view.findViewById(R.id.tv_telegram);
        TextView tvTwitter = view.findViewById(R.id.tv_twitter);
        LinearLayout llTelegram = view.findViewById(R.id.ll_telegram);
        LinearLayout llTwitter = view.findViewById(R.id.ll_twitter);
        Button btnGotIt = view.findViewById(R.id.btn_got_it);

        ivClose.setOnClickListener(v -> dialog.dismiss());

        tvName.setText(bean.getName());

        tvWebsit.setText(bean.getWebsite());

        tvInfo.setText(bean.getInfo());
        if (!TextUtils.isEmpty(bean.getTelegram())) {
            llTelegram.setVisibility(View.VISIBLE);
            tvTelegram.setText(bean.getTelegram());
        }

        if (!TextUtils.isEmpty(bean.getTwitter())) {
            llTwitter.setVisibility(View.VISIBLE);
            tvTwitter.setText(bean.getTwitter());
        }

        tvWebsit.setOnClickListener(v -> ClickCopyUtils.copy(getContext(), bean.getWebsite()));
//        tvEmail.setOnClickListener(v -> ClickCopyUtils.copy(getContext(), ""));
        tvTelegram.setOnClickListener(v -> ClickCopyUtils.copy(getContext(), bean.getTelegram()));
        tvTwitter.setOnClickListener(v -> ClickCopyUtils.copy(getContext(), bean.getTwitter()));

        btnGotIt.setOnClickListener(v -> {
            dialog.dismiss();
            LoadWebPageUtils.activityIntent(getContext(), 1, bean.getName(), bean.getDappUrl());
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    private void collectDialog(DappInfoBean bean) {

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_dapp_collect, null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView title = view.findViewById(R.id.tv_title);
        TextView tvCopy = view.findViewById(R.id.tv_copy);
        TextView tvCollect = view.findViewById(R.id.tv_collect);
        tvCancel.setOnClickListener(v -> dialog.dismiss());
        title.setText(getString(R.string.add)+bean.getName());
        tvCopy.setOnClickListener(v -> {
            dialog.dismiss();
            ClickCopyUtils.copy(getContext(), bean.getDappUrl());
        });

        tvCollect.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
}
