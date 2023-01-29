package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Duan
 * date   : 2021/11/213:47
 * desc   :
 * version: 1.0.0
 */
public class SwapExchangeLiquidityFragment extends BaseFragment {
    @BindView(R.id.ll_liquidity)
    LinearLayout llLiquidity;
    @BindView(R.id.ll_import_pool)
    LinearLayout llImportPool;
    @BindView(R.id.tv_look_other_token)
    TextView tvLookOtherToken;
    @BindView(R.id.btn_add_liquidity)
    Button btnAddLiquidity;
    @BindView(R.id.iv_import_back)
    ImageView ivImportBack;
    @BindView(R.id.iv_add_back)
    ImageView ivAddBack;
    @BindView(R.id.iv_sell_pic)
    ImageView ivSellPic;
    @BindView(R.id.tv_sell_name)
    TextView tvSellName;
    @BindView(R.id.rl_sell)
    RelativeLayout rlSell;
    @BindView(R.id.tv_sell_balance)
    TextView tvSellBalance;
    @BindView(R.id.iv_buy_pic)
    ImageView ivBuyPic;
    @BindView(R.id.tv_buy_name)
    TextView tvBuyName;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @BindView(R.id.tv_buy_balance)
    TextView tvBuyBalance;
    @BindView(R.id.btn_exchange)
    Button btnExchange;
    @BindView(R.id.ll_add_pool)
    LinearLayout llAddPool;

    public static Fragment newInstance(int id, int status) {
        SwapExchangeLiquidityFragment fragment = new SwapExchangeLiquidityFragment();
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
        return R.layout.fragment_swap_exchange_liquidity;
    }

    @Override
    public void initView() {
        viewShow(1);
    }

    @Override
    public void initData() {
    }

    @OnClick({R.id.tv_look_other_token, R.id.btn_add_liquidity, R.id.iv_import_back, R.id.iv_add_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_look_other_token:
                viewShow(2);
                break;
            case R.id.btn_add_liquidity:
                viewShow(3);
                break;
            case R.id.iv_import_back:
            case R.id.iv_add_back:
                viewShow(1);
                break;
        }
    }

    private void viewShow(int flag) {
        if (flag == 1) {
            llLiquidity.setVisibility(View.VISIBLE);
            llImportPool.setVisibility(View.GONE);
            llAddPool.setVisibility(View.GONE);
        } else if (flag == 2) {
            llLiquidity.setVisibility(View.GONE);
            llImportPool.setVisibility(View.VISIBLE);
            llAddPool.setVisibility(View.GONE);
        } else if (flag == 3) {
            llLiquidity.setVisibility(View.GONE);
            llImportPool.setVisibility(View.GONE);
            llAddPool.setVisibility(View.VISIBLE);
        }
    }
}
