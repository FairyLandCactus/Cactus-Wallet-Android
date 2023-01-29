package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SelectAddTokenWalletAdapter;
import com.qianlihu.solanawallet.adapter.SelectMainChainAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.SelectMainChainBean;
import com.qianlihu.solanawallet.bean.SolanaBlanceBean;
import com.qianlihu.solanawallet.bean.SolscanTokenBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.binance.BinanceWalletUtil;
import com.qianlihu.solanawallet.binance.Token;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.ethereum.EthWalletUtil;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.utils.TokenMintUtil;
import com.qianlihu.solanawallet.wallet_bean.TokenIconBean;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.web3j.abi.datatypes.Type;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

/**
 * @Author: duan
 * @Date: 2022/7/26
 * @Description: 自定义添加代币
 *
 */
public class HandAddCurrencyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_token_address)
    EditText etTokenAddress;
    @BindView(R.id.tv_main_chain)
    TextView tvMainChain;
    @BindView(R.id.tv_select_wallet)
    TextView tvSelectWallet;

    private String mIconPath = "";
    private String mPuk = "";
    private String mWalletType = "0";
    private String mPik = "";

    private String mChain = "";
    private List<WalletBean> mWalletList = new ArrayList<>();
    private String mName = "";
    private String mSymbol = "";
    private int decimal = 18;
    private String mMint = "";
    private String mSearchAddress = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hand_add_currency;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.token_custom_add_token));
        mWalletType = getIntent().getStringExtra(Constant.WALLET_TYPE);
    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_scan_address, R.id.btn_confirm_add, R.id.ll_select_main_chain, R.id.ll_select_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_select_main_chain:
                selectMainChain();
                break;
            case R.id.ll_select_wallet:
                selectWallet();
                break;
            case R.id.iv_scan_address:
                scanTokenQr();
                break;
            case R.id.btn_confirm_add:
                getConstractAddressInfo();
                break;
        }
    }

    private void selectMainChain() {
        String[] symbol = {"BSC", "Solana", "Ethereum", "Bitcoin"};
        String[] name = {"BNB", "SOL", "ETH", "BTC"};
        Integer[] pic = {R.mipmap.icon_binance_logo, R.mipmap.icon_solana, R.mipmap.icon_eth, R.mipmap.icon_bitcoin};
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_select_main_chain, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvChain = view.findViewById(R.id.rv_main_chain);
        rvChain.setLayoutManager(new LinearLayoutManager(this));
        List<SelectMainChainBean> list = new ArrayList<>();
        for (int i = 0; i < symbol.length; i++) {
            SelectMainChainBean bean = new SelectMainChainBean();
            bean.setLogo(pic[i]);
            bean.setName(name[i]);
            bean.setSymbol(symbol[i]);
            list.add(bean);
        }
        SelectMainChainAdapter chainAdapter = new SelectMainChainAdapter(list);
        rvChain.setAdapter(chainAdapter);
        chainAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (position == 3) {
                showInfo(getString(R.string.not_yet_open));
                return;
            }
            mChain = name[position];
            tvMainChain.setText(mChain);
            mPuk = "";
            tvSelectWallet.setText("");
            mWalletList = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChain).find(WalletBean.class);
            dialog.dismiss();
        });

        ivClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();

    }

    private void selectWallet() {
        if (TextUtils.isEmpty(mChain)) {
            showInfo(getString(R.string.token_dialog_select_main_chain));
            return;
        }
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_select_add_walldet, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvWallet = view.findViewById(R.id.rv_wallet);
        rvWallet.setLayoutManager(new LinearLayoutManager(this));
        if (mWalletList.size() > 0) {
            SelectAddTokenWalletAdapter walletAdapter = new SelectAddTokenWalletAdapter(mWalletList);
            rvWallet.setAdapter(walletAdapter);
            walletAdapter.setOnItemClickListener((adapter, view1, position) -> {
                mPuk = mWalletList.get(position).getPubKey();
                WalletBean bean = WalletManager.getInstance().getWalletInfo(mPuk);
                if (bean != null) {
                    mPik = bean.getPvaKey();
                }
                tvSelectWallet.setText(mWalletList.get(position).getName());
                dialog.dismiss();
            });
        } else {
            rvWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet))));
        }
        ivClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    private void scanTokenQr() {
        if (lacksPermission(writePermissions)) {
            ActivityCompat.requestPermissions(this, writePermissions, 0);
            return;
        }
        Intent intent = new Intent(this, QRActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                if (result.length() < 42) {
                    showInfo(getString(R.string.invalid_address));
                    return;
                }
                //获取到扫描二维码的数据
                etTokenAddress.setText(result);
            }
        }
    }

    private void getConstractAddressInfo() {
        mMint = etTokenAddress.getText().toString().trim();
        if (TextUtils.isEmpty(mMint)) {
            showInfo(getString(R.string.enter_token_address));
            return;
        }
        if (mMint.length() < 42) {
            showInfo(getString(R.string.invalid_address));
            return;
        }
        if (TextUtils.isEmpty(mChain)) {
            showInfo(getString(R.string.token_dialog_select_main_chain));
            return;
        }
        if (TextUtils.isEmpty(mPuk)) {
            showInfo(getString(R.string.token_dialog_select_add_wallet));
            return;
        }
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getTokenIcon();
                if (mChain.equals(Constant.SOL_CHAIN)) {
                    getSolanaToken();
                } else if (mChain.equals(Constant.BNB_CHAIN)){
                    getBNBTokenInfo();
                }else if (mChain.equals(Constant.ETH_CHAIN)){
                    getETHTokenInfo();
                }
            }
        }).start();

    }

    private void getSolanaToken() {
        List<Tokens> tokensList = LitePal.findAll(Tokens.class);
        if (tokensList.size() > 0) {//查询本地数据库是否有该token
            List<Tokens> list = TokenMintUtil.searchTokenAddress(mMint, tokensList);
            if (list.size() > 0) {
                String name = list.get(0).getName();
                String symbol = list.get(0).getSymbol();
                int decimals = list.get(0).getDecimals();
                runOnUiThread(() -> {
                    closeLoadingDialog();
                    addToken(symbol, name, decimals);
                });
            } else {//如果本地没该token，通过网络查询
                getSolanaOnlineTokenInfo();
            }
        } else {
            getSolanaOnlineTokenInfo();
        }
    }

    private void getSolanaOnlineTokenInfo() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        HttpService.doGet(Constant.SOL_TOKEN_URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> closeLoadingDialog());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==tokenlist", "tokenlist===  " + body);
                runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(body)) {
                        TokenMintUtil.getTokenInfos(body);
                        List<Tokens> tokensList = LitePal.findAll(Tokens.class);
                        if (tokensList.size() > 0) {
                            closeLoadingDialog();
                            List<Tokens> list = TokenMintUtil.searchTokenAddress(mMint, tokensList);
                            if (list.size() > 0) {
                                String name = list.get(0).getName();
                                String symbol = list.get(0).getSymbol();
                                int decimals = list.get(0).getDecimals();
                                addToken(symbol, name, decimals);
                            } else {
//                                dialogTip(getString(R.string.token_no_found_contract));
                                getSolscanToken();
                            }
                        } else {
//                            closeLoadingDialog();
//                            dialogTip(getString(R.string.token_no_found_contract));
                            getSolscanToken();
                        }
                    }else {
//                        closeLoadingDialog();
//                        dialogTip(getString(R.string.token_no_found_contract));
                        getSolscanToken();
                    }
                });

            }
        });
    }

    private void getSolscanToken() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", mMint);
        String url = "https://api.solscan.io/account";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeLoadingDialog();
                        dialogTip(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==body", "getSolanaBalanceInfo  === " + body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeLoadingDialog();
                        if (!TextUtils.isEmpty(body)) {
                            if (!body.startsWith("{") && !body.endsWith("}")) {
                                dialogTip(getString(R.string.token_no_found_contract));
                                return;
                            }
                            Gson gson = new Gson();
                            SolscanTokenBean bean = gson.fromJson(body, SolscanTokenBean.class);
                            if (bean != null) {
                                if (bean.getData() != null) {
                                    if (bean.getData().getTokenInfo() != null) {
                                        String name = bean.getData().getTokenInfo().getName();
                                        String symbol = bean.getData().getTokenInfo().getSymbol();
                                        int decimals = bean.getData().getTokenInfo().getDecimals();
                                        addToken(symbol, name, decimals);
                                    } else {
                                        dialogTip(getString(R.string.token_no_found_contract));
                                    }
                                }
                            }
                        }
                    }
                });

            }
        });
    }

    private void getBNBTokenInfo() {
        BinanceWalletUtil.loadContract(mPik, mMint, BigInteger.valueOf(0), BigInteger.valueOf(0)).subscribe(new SingleObserver<Token>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Token token) {
                Log.i("duan==Constract", "加载完成");
                BinanceWalletUtil.getSymbol(token).subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        mSymbol = s;
                        BinanceWalletUtil.getName(token).subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(String name) {
                                mName = name;
                                BinanceWalletUtil.getDecimals(token).subscribe(new SingleObserver<Type>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onSuccess(Type type) {
                                        closeLoadingDialog();
                                        if (type != null) {
                                            if (type.getValue() != null) {
                                                decimal = Integer.parseInt(String.valueOf(type.getValue()));
                                                addToken(mSymbol, mName, decimal);
                                            }
                                        }
                                        Log.i("duan==Constract", "代币精度== " + type.getValue());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        closeLoadingDialog();
                                        showInfo(e.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                closeLoadingDialog();
                                showInfo(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeLoadingDialog();
                        dialogTip(getString(R.string.token_no_found_contract));
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                closeLoadingDialog();
                dialogTip(getString(R.string.token_no_found_contract));
            }
        });
    }

    private void getETHTokenInfo() {
        EthWalletUtil.loadContract(mPik, mMint, BigInteger.valueOf(0), BigInteger.valueOf(0)).subscribe(new SingleObserver<Token>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Token token) {
                Log.i("duan==Constract", "加载完成");
                EthWalletUtil.getSymbol(token).subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        mSymbol = s;
                        EthWalletUtil.getName(token).subscribe(new SingleObserver<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(String name) {
                                mName = name;
                                EthWalletUtil.getDecimals(token).subscribe(new SingleObserver<Type>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onSuccess(Type type) {
                                        closeLoadingDialog();
                                        if (type != null) {
                                            if (type.getValue() != null) {
                                                decimal = Integer.parseInt(String.valueOf(type.getValue()));
                                                addToken(mSymbol, mName, decimal);
                                            }
                                        }
                                        Log.i("duan==Constract", "代币精度== " + type.getValue());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        closeLoadingDialog();
                                        showInfo(e.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                closeLoadingDialog();
                                showInfo(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        closeLoadingDialog();
                        dialogTip(getString(R.string.token_no_found_contract));
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                closeLoadingDialog();
                dialogTip(getString(R.string.token_no_found_contract));
            }
        });
    }

    private void getTokenIcon() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("search", mMint);
        HttpService.doGet(Constant.GET_TOKEN_ICON, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    showInfo(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(body)) {
                        Gson gson = new Gson();
                        TokenIconBean bean = gson.fromJson(body, TokenIconBean.class);
                        if (bean.getCode() == 1) {
                            mIconPath = bean.getData().getImage();
                        } else {
                            showInfo(bean.getMsg());
                        }
                    } else {
                        showInfo(getString(R.string.data_exception));
                    }
                });
            }
        });
    }

    private void addToken(String symbol, String name, int decimals) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_add_token, null, false);
        Button btnNotAddedYet = view.findViewById(R.id.btn_not_added_yet);
        Button btnAddNow = view.findViewById(R.id.btn_add_now);
        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvSymbol = view.findViewById(R.id.tv_symbol);

        Glide.with(this).load(mIconPath).error(R.mipmap.icon_unknown).centerCrop().into(ivIcon);
        tvSymbol.setText(symbol);
        tvName.setText(name);
        btnNotAddedYet.setOnClickListener(v -> dialog.dismiss());

        btnAddNow.setOnClickListener(v -> {
            List<AddTokenDB> dbList = LitePal.where("walletAddress = ? and tokenAddress = ?", mPuk, mMint).find(AddTokenDB.class);
            if (dbList.size() > 0) {
                showInfo(getString(R.string.not_need_add_again));
                dialog.dismiss();
                return;
            }

            AddTokenDB db = new AddTokenDB();
            db.setWalletAddress(mPuk);
            db.setTokenAddress(mMint);
            db.setSymbol(symbol);
            db.setName(name);
            db.setLogoURI(mIconPath);
            db.setDecimals(decimals);
            db.setWalletType(mWalletType);
            db.setShield("0");
            db.setUsdt(0.0);
            db.setAmount(0.0);
            db.save();
//            CacheData.shared().updateWalletInfoFlag = 66;
            if (mWalletType.equals("0")) {
                CacheData.shared().applyFlag = 67;
            } else {
                CacheData.shared().saveFlag = 67;
            }
            showSuccessToast(getString(R.string.added_successfully));
            dialog.dismiss();
            finish();
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

}