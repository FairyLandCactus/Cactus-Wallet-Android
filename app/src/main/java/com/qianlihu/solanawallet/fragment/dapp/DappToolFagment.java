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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.AddTokenSelectMainChainAdapter;
import com.qianlihu.solanawallet.adapter.DappBscListAdapter;
import com.qianlihu.solanawallet.adapter.DappMainChainAdapter;
import com.qianlihu.solanawallet.adapter.DappRankListAdapter;
import com.qianlihu.solanawallet.adapter.DappRankTypeAdapter;
import com.qianlihu.solanawallet.adapter.DappToolAdapter;
import com.qianlihu.solanawallet.adapter.DappToolItemAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.DappInfoBean;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappToolFagment extends BaseFragment {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    private int[] selectLightIcon = {
            R.mipmap.icon_dapp_tool_select_light_1,
            R.mipmap.icon_dapp_tool_select_light_2,
            R.mipmap.icon_dapp_tool_select_light_3,
            R.mipmap.icon_dapp_tool_select_light_4,
            R.mipmap.icon_dapp_tool_select_light_5,
            R.mipmap.icon_dapp_tool_select_light_6,
            R.mipmap.icon_dapp_tool_select_light_7,
            R.mipmap.icon_dapp_tool_select_light_8,
            R.mipmap.icon_dapp_tool_select_light_9,
            R.mipmap.icon_dapp_tool_select_light_10
    };

    private int[] unSelectLightIcon = {
            R.mipmap.icon_dapp_tool_unselect_light_1,
            R.mipmap.icon_dapp_tool_unselect_light_2,
            R.mipmap.icon_dapp_tool_unselect_light_3,
            R.mipmap.icon_dapp_tool_unselect_light_4,
            R.mipmap.icon_dapp_tool_unselect_light_5,
            R.mipmap.icon_dapp_tool_unselect_light_6,
            R.mipmap.icon_dapp_tool_unselect_light_7,
            R.mipmap.icon_dapp_tool_unselect_light_8,
            R.mipmap.icon_dapp_tool_unselect_light_9,
            R.mipmap.icon_dapp_tool_unselect_light_10
    };

    private int[] unSelectDarkIcon = {
            R.mipmap.icon_dapp_tool_unselect_dark_1,
            R.mipmap.icon_dapp_tool_unselect_dark_2,
            R.mipmap.icon_dapp_tool_unselect_dark_3,
            R.mipmap.icon_dapp_tool_unselect_dark_4,
            R.mipmap.icon_dapp_tool_unselect_dark_5,
            R.mipmap.icon_dapp_tool_unselect_dark_6,
            R.mipmap.icon_dapp_tool_unselect_dark_7,
            R.mipmap.icon_dapp_tool_unselect_dark_8,
            R.mipmap.icon_dapp_tool_unselect_dark_9,
            R.mipmap.icon_dapp_tool_unselect_dark_10
    };

    private boolean isDark;

    public static Fragment newInstance(int id, int status) {
        DappToolFagment fragment = new DappToolFagment();
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
        return R.layout.fragment_dapp_tool;
    }

    @Override
    public void initView() {

        List<TokenTypeBean> list = new ArrayList<>();
        isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);

        for (int i = 0; i < selectLightIcon.length; i++) {
            TokenTypeBean bean = new TokenTypeBean();
            bean.setIcon(selectLightIcon[i]);
            if (isDark) {
                bean.setUnIcon(unSelectDarkIcon[i]);
            } else {
                bean.setUnIcon(unSelectLightIcon[i]);
            }
            list.add(bean);
        }
        DappToolAdapter toolAdapter = new DappToolAdapter(list);
        rvType.setLayoutManager(new LinearLayoutManager(getContext()));
        rvType.setAdapter(toolAdapter);
        selectItem(0);
        toolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toolAdapter.select(position);
                selectItem(position);
            }
        });
        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void initData() {

    }

    private void selectItem(int position) {
        List<DappInfoBean> list2 = new ArrayList<>();
        DappInfoBean infoBean = new DappInfoBean();
        int icon1 = R.mipmap.icon_dapp_tool_dark_item_1;
        if (!isDark) {
            icon1 = R.mipmap.icon_dapp_tool_item_1;
        }
        infoBean.setIcon(icon1);
        infoBean.setName(getString(R.string.Blockchain_browser));
        infoBean.setInfo(getString(R.string.blockchain_browser_porta));
        infoBean.setDappUrl("https://bk.page/walletTools?utm_source=bitkeep&_needChain=");
        list2.add(infoBean);

        DappInfoBean infoBean2 = new DappInfoBean();
        int icon2 = R.mipmap.icon_dapp_tool_dark_item_2;
        if (!isDark) {
            icon2 = R.mipmap.icon_dapp_tool_item_2;
        }
        infoBean2.setIcon(icon2);
        infoBean2.setName(getString(R.string.Batch_transfer));
        infoBean2.setInfo(getString(R.string.batch_transfer_tool));
        infoBean2.setDappUrl("https://bk.page/batchSend?utm_source=bitkeep&_needChain=eth");
        list2.add(infoBean2);

        DappInfoBean infoBean3 = new DappInfoBean();
        int icon3 = R.mipmap.icon_dapp_tool_dark_item_3;
        if (!isDark) {
            icon3 = R.mipmap.icon_dapp_tool_item_3;
        }
        infoBean3.setIcon(icon3);
        infoBean3.setName(getString(R.string.Contract_inspection));
        infoBean3.setInfo(getString(R.string.contract_detection_tool));
        infoBean3.setDappUrl("https://bk.page/ContractDetection/56/?utm_source=bitkeep&_needChain=eth");
        list2.add(infoBean3);

        DappToolItemAdapter listAdapter = new DappToolItemAdapter(list2);
        rvItem.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                junmpDappDialog(list2.get(position), position);
            }
        });

        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                collectDialog(list2.get(position));
            }
        });
    }

    private String chainName;

    private void junmpDappDialog(DappInfoBean bean, int position) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_dapp_tool_risk_tip, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ImageView ivLogo = view.findViewById(R.id.iv_dapp_logo);
        TextView tvDappTitle = view.findViewById(R.id.tv_dapp_title);
        TextView tvDappDec = view.findViewById(R.id.tv_dapp_dec);
        RecyclerView rvMainChain = view.findViewById(R.id.rv_main_chain);
        Button btnGotIt = view.findViewById(R.id.btn_got_it);

        Glide.with(mContext).load(bean.getIcon()).into(ivLogo);
        tvDappTitle.setText(bean.getName());
        tvDappDec.setText(bean.getInfo());

        String[] chainNameArr = {"ETH", "BNB", "TRX", "MATIC", "AVAX_C", "IOST", "SOL", "OKT", "EOS"};
        int[] logo = {
                R.mipmap.ic_dapp_tool_dialog_light_1,
                R.mipmap.ic_dapp_tool_dialog_light_2,
                R.mipmap.ic_dapp_tool_dialog_light_3,
                R.mipmap.ic_dapp_tool_dialog_light_4,
                R.mipmap.ic_dapp_tool_dialog_light_5,
                R.mipmap.ic_dapp_tool_dialog_light_6,
                R.mipmap.ic_dapp_tool_dialog_light_7,
                R.mipmap.ic_dapp_tool_dialog_light_8,
                R.mipmap.ic_dapp_tool_dialog_light_9
        };

        List<TokenTypeBean> beanList = new ArrayList<>();
        for (int i = 0; i < chainNameArr.length; i++) {
            TokenTypeBean typeBean = new TokenTypeBean();
            typeBean.setIcon(logo[i]);
            typeBean.setTokenName(chainNameArr[i]);
            beanList.add(typeBean);
        }

        DappMainChainAdapter chainAdapter = new DappMainChainAdapter(beanList);
        rvMainChain.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvMainChain.addItemDecoration(new GridSpaceItemDecoration(3, 30, 40));
        rvMainChain.setAdapter(chainAdapter);
        chainName = beanList.get(0).getTokenName().toLowerCase();
        chainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                chainAdapter.select(position);
                chainName = beanList.get(position).getTokenName().toLowerCase();
            }
        });

        ivClose.setOnClickListener(v -> dialog.dismiss());

        btnGotIt.setOnClickListener(v -> {
            dialog.dismiss();
            if (position == 1) {
                LoadWebPageUtils.activityIntent(getContext(), 2, bean.getName(), bean.getDappUrl() + chainName);
            } else {
                LoadWebPageUtils.activityIntent2(getContext(), bean.getName(), bean.getDappUrl() + chainName);
            }
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
        title.setText(getString(R.string.add) + bean.getName());
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
