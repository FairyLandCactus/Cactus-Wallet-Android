package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.AddTokenAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SwappExchangeTokenAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.utils.TokenMintUtil;

import org.litepal.LitePal;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/213:47
 * desc   :
 * version: 1.0.0
 */
public class SwapExchangeChildFragment extends BaseFragment {
    @BindView(R.id.iv_sell_pic)
    CircleImageView ivSellPic;
    @BindView(R.id.tv_sell_name)
    TextView tvSellName;
    @BindView(R.id.tv_sell_balance)
    TextView tvSellBalance;
    @BindView(R.id.iv_buy_pic)
    CircleImageView ivBuyPic;
    @BindView(R.id.tv_buy_name)
    TextView tvBuyName;
    @BindView(R.id.tv_buy_balance)
    TextView tvBuyBalance;
    @BindView(R.id.btn_exchange)
    Button btnExchange;

    private List<Tokens> mTokensList;

    private String sellLogo = "";
    private String buyLogo = "";
    private int sellLogoInt = R.mipmap.transfer_accounts_ius;
    private int buyLogoInt = R.mipmap.icon_solana;
    private boolean isUpDownExchange = false;

    public static Fragment newInstance(int id, int status) {
        SwapExchangeChildFragment fragment = new SwapExchangeChildFragment();
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
        return R.layout.fragment_swap_exchange_child;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTokensList = LitePal.findAll(Tokens.class);
    }

    @OnClick({R.id.rl_sell, R.id.rl_buy, R.id.btn_exchange, R.id.iv_up_down_exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_sell:
                selectToken(0);
                break;
            case R.id.iv_up_down_exchange:
                upDownExchange();
                break;
            case R.id.rl_buy:
                selectToken(1);
                break;
            case R.id.btn_exchange:

                break;
        }
    }

    private void upDownExchange() {
        String sellName = tvSellName.getText().toString();
        String sellBalance = tvSellBalance.getText().toString();
        String buyName = tvBuyName.getText().toString();
        String buyBalance = tvBuyBalance.getText().toString();

        tvSellName.setText(buyName);
        tvSellBalance.setText(buyBalance);
        tvBuyName.setText(sellName);
        tvBuyBalance.setText(sellBalance);
        Glide.with(getActivity()).load(buyLogo).error(buyLogoInt).into(ivSellPic);
        Glide.with(getActivity()).load(sellLogo).error(sellLogoInt).into(ivBuyPic);
        String buyLogoUrl = buyLogo;
        int buLogoIntError = buyLogoInt;
        buyLogo = sellLogo;
        sellLogo = buyLogoUrl;
        buyLogoInt = sellLogoInt;
        sellLogoInt = buLogoIntError;

    }

    private SwappExchangeTokenAdapter tokenAdapter;

    private void selectToken(int flag) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_swap_select_token, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        EditText etSearch = view.findViewById(R.id.et_search);
        RecyclerView rvToken = view.findViewById(R.id.rv_token);
        ivClose.setOnClickListener(v -> dialog.dismiss());

        tokenAdapter = new SwappExchangeTokenAdapter(mTokensList);
        rvToken.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToken.setAdapter(tokenAdapter);
        selectClick(mTokensList, flag, dialog);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
//                Log.i("duan==search", content);
                if (TextUtils.isEmpty(content)) {
                    if (mTokensList.size() > 0) {
                        tokenAdapter = new SwappExchangeTokenAdapter(mTokensList);
                        rvToken.setAdapter(tokenAdapter);
                        selectClick(mTokensList, flag, dialog);
                    }
                    return;
                }
                List<Tokens> list = TokenMintUtil.searchTokens(content, mTokensList);
                if (list.size() > 0) {
                    tokenAdapter = new SwappExchangeTokenAdapter(list);
                    rvToken.setAdapter(tokenAdapter);
                    tokenAdapter.notifyDataSetChanged();
                    selectClick(list, flag, dialog);
                } else {
                    rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_token))));
                }
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    private void selectClick(List<Tokens> list, int flag, BottomSheetDialog dialog) {
        tokenAdapter.setOnItemClickListener((adapter, view, position) -> {
            String logoUrl = list.get(position).getLogoURI();
            String name = list.get(position).getSymbol();
            if (flag == 0) {
                Glide.with(getActivity()).load(logoUrl).error(R.mipmap.transfer_accounts_ius).into(ivSellPic);
                tvSellName.setText(name);
                sellLogo = logoUrl;
            } else {
                Glide.with(getActivity()).load(logoUrl).error(R.mipmap.icon_solana).into(ivBuyPic);
                tvBuyName.setText(name);
                buyLogo = logoUrl;
            }
            dialog.dismiss();
        });
    }
}
