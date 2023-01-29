package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.AddBnbTokenAdapter;
import com.qianlihu.solanawallet.adapter.AddTokenAdapter;
import com.qianlihu.solanawallet.adapter.AddTokenSelectMainChainAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SelectAddTokenWalletAdapter;
import com.qianlihu.solanawallet.adapter.TokenTypeAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.binance.BNBTokenBean;
import com.qianlihu.solanawallet.binance.BnbTokenJsonUtils;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.interfere.IFingerprint;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.FingerprintPwd;
import com.qianlihu.solanawallet.utils.TokenMintUtil;
import com.qianlihu.solanawallet.utils.WalletResource;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTokenActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_token)
    RecyclerView rvToken;
    @BindView(R.id.rv_token_type)
    RecyclerView rvTokenType;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout swipRefresh;
    @BindView(R.id.ll_custom_add)
    LinearLayout llCustomAdd;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_select_wallet)
    TextView tvSelectWallet;

    private List<Tokens> mTokensList;
    private AddTokenAdapter tokenAdapter;

    private List<BNBTokenBean> mTokensListBNB;
    private AddBnbTokenAdapter tokenAdapterBNB;

    private String mPuk = "";
    private String mWalletType = "0";
    private String mPwd = "";
    private String mPik = "";
    private String mChain = Constant.SOL_CHAIN;
    private String mWalletName = "";

    private List<WalletBean> mWalletList = new ArrayList<>();

    private String[] mTokenTypeName =WalletResource.mTokenTypeName;
    private int[] mTokenTypeUnIcon = WalletResource.mTokenTypeUnIcon;
    private int[] mTokenTypeIcon = WalletResource.mTokenTypeIcon;
    private TokenTypeAdapter mTypeAdapter;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_token;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        mPuk = getIntent().getStringExtra(Constant.SOL_PUK);
        WalletBean bean = WalletManager.getInstance().getWalletInfo(mPuk);
        if (bean != null) {
            mPwd = bean.getPassword();
            mChain = bean.getMainChain();
            mWalletName = bean.getName();
            tvSelectWallet.setText(mWalletName);
        }
        mWalletType = getIntent().getStringExtra(Constant.WALLET_TYPE);
        rvTokenType.setLayoutManager(new LinearLayoutManager(this));
        List<TokenTypeBean> beanList = new ArrayList<>();
        for (int i = 0; i < mTokenTypeUnIcon.length; i++) {
            TokenTypeBean typeBean = new TokenTypeBean();
            typeBean.setIcon(mTokenTypeUnIcon[i]);
            typeBean.setUnIcon(mTokenTypeIcon[i]);
            typeBean.setTokenName(mTokenTypeName[i]);
            beanList.add(typeBean);
        }
        tvAll.setText(getString(R.string.whole) + mTokenTypeName.length);
        mTypeAdapter = new TokenTypeAdapter(beanList);
        rvTokenType.setAdapter(mTypeAdapter);
        rvToken.setLayoutManager(new LinearLayoutManager(this));
        if (mChain.equals(Constant.SOL_CHAIN)) {
            addTokens();
            searchToken();
            mTypeAdapter.select(0);
        } else if (mChain.equals(Constant.BNB_CHAIN)){
            addTokensBNB();
            searchTokenBNB();
            mTypeAdapter.select(1);
        } else if (mChain.equals(Constant.ETH_CHAIN)) {
            addTokensBNB();
            searchTokenBNB();
            mTypeAdapter.select(2);
        }
        mTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTypeAdapter.select(position);
                tvSelectWallet.setText(getString(R.string.select_wallet));
                mWalletName = "";
                mPuk = "";
                if (position == 0) {
                    mChain = Constant.SOL_CHAIN;
                    addTokens();
                    searchToken();
                } else if (position == 1) {
                    mChain = Constant.BNB_CHAIN;
                    addTokensBNB();
                    searchTokenBNB();
                }else if (position == 2) {
                    mChain = Constant.ETH_CHAIN;
                    addTokensBNB();
                    searchTokenBNB();
                } else {
                    mChain = "";
                    rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.token_no_type))));
                }
            }
        });

        swipRefresh.setColorSchemeColors(getResources().getColor(R.color.yellow));
        swipRefresh.setOnRefreshListener(() -> {
            swipRefresh.setRefreshing(false);
//                getCoinType();
            if (mChain.equals(Constant.SOL_CHAIN)) {
                addTokens();
            } else if (mChain.equals(Constant.BNB_CHAIN)) {
                addTokensBNB();
            } else if (mChain.equals(Constant.ETH_CHAIN)) {
                addTokensBNB();
            }else {
                rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.token_no_type))));
            }

        });
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.tv_all, R.id.btn_custom, R.id.tv_select_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                String seachContent = etSearch.getText().toString();
                if (TextUtils.isEmpty(seachContent)) {
                    showInfo(getString(R.string.input_content_empty));
                    return;
                }
                if (mChain.equals(Constant.SOL_CHAIN)) {
                    updateSearchContent(seachContent);
                } else if (mChain.equals(Constant.BNB_CHAIN)){
                    updateSearchContentBNB(seachContent);
                }else if (mChain.equals(Constant.ETH_CHAIN)){
                    updateSearchContentBNB(seachContent);
                }
                break;
            case R.id.tv_all:
                allSelectMainChain();
                break;
            case R.id.btn_custom:
                Intent intent = new Intent(this, HandAddCurrencyActivity.class);
                intent.putExtra("pwd", mPwd);
                intent.putExtra(Constant.SOL_PUK, mPuk);
                intent.putExtra(Constant.WALLET_TYPE, mWalletType);
                startActivity(intent);
                break;
            case R.id.tv_select_wallet:
                selectWallet();
                break;
        }
    }

    //点击全部选择主链
    private void allSelectMainChain() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_select_main_chain_token, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        EditText etSearchChain = view.findViewById(R.id.et_search_chain);
        RecyclerView rvMainChain = view.findViewById(R.id.rv_main_chain);
        List<TokenTypeBean> beanList = new ArrayList<>();
        for (int i = 0; i < mTokenTypeName.length; i++) {
            TokenTypeBean bean = new TokenTypeBean();
            bean.setIcon(mTokenTypeIcon[i]);
            bean.setTokenName(mTokenTypeName[i]);
            beanList.add(bean);
        }
        AddTokenSelectMainChainAdapter chainAdapter = new AddTokenSelectMainChainAdapter(beanList);
        rvMainChain.setLayoutManager(new GridLayoutManager(this, 4));
        rvMainChain.addItemDecoration(new GridSpaceItemDecoration(4, 60, 80));
        rvMainChain.setAdapter(chainAdapter);
        selectMainChain(chainAdapter, dialog, beanList);
        ivClose.setOnClickListener(v -> dialog.dismiss());

        etSearchChain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().toLowerCase();
                if (TextUtils.isEmpty(content)) {
                    AddTokenSelectMainChainAdapter chainAdapter2 = new AddTokenSelectMainChainAdapter(beanList);
                    rvMainChain.setAdapter(chainAdapter2);
                    selectMainChain(chainAdapter2, dialog, beanList);
                    return;
                }
                List<TokenTypeBean> beanList1 = new ArrayList<>();
                for (int i = 0; i < beanList.size(); i++) {
                    String tokenName = beanList.get(i).getTokenName().toLowerCase();
                    if (tokenName.contains(content)) {
                        beanList1.add(beanList.get(i));
                    }
                }
                if (beanList1.size() > 0) {
                    AddTokenSelectMainChainAdapter chainAdapter3 = new AddTokenSelectMainChainAdapter(beanList1);
                    rvMainChain.setAdapter(chainAdapter3);
                    selectMainChain(chainAdapter3, dialog, beanList1);
                }
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    //选择主链列表项的点击事件
    private void selectMainChain(AddTokenSelectMainChainAdapter chainAdapter, BottomSheetDialog dialog, List<TokenTypeBean> beanList) {
        chainAdapter.setOnItemClickListener((adapter, view, position) -> {
            dialog.dismiss();
            String name = beanList.get(position).getTokenName();
            tvSelectWallet.setText(getString(R.string.select_wallet));
            mWalletName = "";
            mPuk = "";
            if (name.equals(mTokenTypeName[0])) {
                mTypeAdapter.select(0);
                rvTokenType.smoothScrollToPosition(0);//定位
                mChain = Constant.SOL_CHAIN;
                addTokens();
                searchToken();
            } else if (name.equals(mTokenTypeName[1])) {
                mTypeAdapter.select(1);
                rvTokenType.smoothScrollToPosition(1);//定位
                mChain = Constant.BNB_CHAIN;
                addTokensBNB();
                searchTokenBNB();
            } else if (name.equals(mTokenTypeName[2])) {
                mTypeAdapter.select(2);
                rvTokenType.smoothScrollToPosition(2);//定位
                mChain = Constant.ETH_CHAIN;
                addTokensBNB();
                searchTokenBNB();
            }else {
                for (int i = 0; i < mTokenTypeIcon.length; i++) {
                    if (mTokenTypeName[i].equals(name)) {
                        mTypeAdapter.notifyDataSetChanged();
                        rvTokenType.smoothScrollToPosition(i);//平滑定位
                        mTypeAdapter.select(i);
                    }
                }
                mChain = "";
                rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.token_no_type))));
            }
        });
    }

    //选择需要添加代币的钱包
    private void selectWallet() {
        if (TextUtils.isEmpty(mChain)) {
            showInfo(getString(R.string.token_no_type));
            return;
        }
        mWalletList = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChain).find(WalletBean.class);
        if (mWalletList.size() == 0) {
            showInfo(getString(R.string.token_no_wallet));
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
                    mChain = bean.getMainChain();
                }
                mWalletName = mWalletList.get(position).getName();
                tvSelectWallet.setText(mWalletName);
                if (mChain.equals(Constant.SOL_CHAIN)) {
                    addTokens();
                    searchToken();
                } else if (mChain.equals(Constant.BNB_CHAIN)){
                    addTokensBNB();
                    searchTokenBNB();
                }else if (mChain.equals(Constant.ETH_CHAIN)){
                    addTokensBNB();
                    searchTokenBNB();
                }
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

    private void addTokens() {
        if (mTokensList == null) {
            showLoadingDialog();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mTokensList == null) {
                            mTokensList = LitePal.findAll(Tokens.class);
                        }
                        handler.sendEmptyMessage(110);
                    }
                });
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 110) {
                closeLoadingDialog();
                List<AddTokenDB> dbList = LitePal.findAll(AddTokenDB.class);
                if (mTokensList.size() > 0) {
                    List<AddTokenDB> list = new ArrayList<>();
                    if (dbList.size() > 0) {
                        for (int i = 0; i < dbList.size(); i++) {
                            if (!TextUtils.isEmpty(mPuk)) {
                                if (mPuk.equals(dbList.get(i).getWalletAddress())) {
                                    list.add(dbList.get(i));
                                }
                            }
                        }
                    }
                    List<Tokens> selectTokenList = new ArrayList<>();
                    List<String> addressList = new ArrayList<>();
                    for (int i = 0; i < mTokensList.size(); i++) {
                        if (list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getTokenAddress().equals(mTokensList.get(i).getAddress())) {
                                    mTokensList.get(i).setSelect(1);
                                    selectTokenList.add(mTokensList.get(i));
//                                    mTokensList.remove(i);
                                    addressList.add(mTokensList.get(i).getAddress());
                                }
                            }
                        }
                    }
                    if (addressList.size() > 0) {
                        for (int i = 0; i < mTokensList.size(); i++) {
                            for (int j = 0; j < addressList.size(); j++) {
                                if (addressList.get(j).equals(mTokensList.get(i).getAddress())) {
                                    mTokensList.remove(i);
                                }
                            }
                        }
                    }
                    if (selectTokenList.size() > 0) {
                        List<Tokens> listTemp = new ArrayList();
                        String tokenAdd = "";
                        for (int i = 0; i < selectTokenList.size(); i++) {
                            if (!tokenAdd.equals(selectTokenList.get(i).getAddress())) {
                                listTemp.add(selectTokenList.get(i));
                            }
                            tokenAdd = selectTokenList.get(i).getAddress();
                        }
                        selectTokenList.clear();
                        selectTokenList.addAll(listTemp);
                    }
                    selectTokenList.addAll(mTokensList);
                    mTokensList.clear();
                    mTokensList.addAll(selectTokenList);
                    tokenAdapter = new AddTokenAdapter(mTokensList);
                    rvToken.setAdapter(tokenAdapter);
                    selectClick(mTokensList);
                }
            }
        }
    };

    private void searchToken() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llAll.setVisibility(View.VISIBLE);
                llCustomAdd.setVisibility(View.GONE);
                String content = s.toString();
                Log.i("duan==search", content);
                if (TextUtils.isEmpty(content)) {
                    if (mTokensList.size() > 0) {
                        tokenAdapter = new AddTokenAdapter(mTokensList);
                        rvToken.setAdapter(tokenAdapter);
                        selectClick(mTokensList);
                    }
                    return;
                }
                updateSearchContent(content);
            }
        });
    }

    private void updateSearchContent(String content) {
        List<Tokens> list = TokenMintUtil.searchTokens(content, mTokensList);

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).getSymbol();
                if (name.toUpperCase().equals(content.toUpperCase())) {
                    Collections.swap(list,0,i);
                }
            }
            tokenAdapter = new AddTokenAdapter(list);
            rvToken.setAdapter(tokenAdapter);
            tokenAdapter.notifyDataSetChanged();
            selectClick(list);
        } else {
//            rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_token))));
            llAll.setVisibility(View.GONE);
            llCustomAdd.setVisibility(View.VISIBLE);
        }

    }

    private void selectClick(List<Tokens> list) {
        tokenAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_select:
                    insertData(list, position);
                    break;
            }
        });
        tokenAdapter.setOnItemClickListener((adapter, view, position) -> insertData(list, position));
    }

    private void insertData(List<Tokens> list, int position) {
        if (TextUtils.isEmpty(mWalletName)) {
            showInfo(getString(R.string.token_select_add_wallet));
            return;
        }
        if (TextUtils.isEmpty(mPuk)) {
            showInfo(getString(R.string.not_yet));
            return;
        }
        if (list.get(position).getSelect() == 1) {
            showInfo(getString(R.string.not_need_add_again));
            return;
        }

        FingerprintPwd.fingerprintVerify(this, new IFingerprint() {
            @Override
            public void onVerify(boolean isVerify) {
                if (isVerify) {
                    saveToken(list, position);
                } else {
                    EnterPayPwd.password(AddTokenActivity.this, 1, new IPassword() {
                        @Override
                        public void onPassword(String pwd) {
                            if (!pwd.equals(mPwd)) {
                                showInfo(getString(R.string.pwd_error));
                                return;
                            }
                            saveToken(list, position);
                        }
                        @Override
                        public void onCancel() {
                        }
                    });
                }
            }

            @Override
            public void onClose() {

            }
        });


    }

    private void saveToken(List<Tokens> list, int position) {
        AddTokenDB db = new AddTokenDB();
        db.setWalletAddress(mPuk);
        db.setTokenAddress(list.get(position).getAddress());
        db.setSymbol(list.get(position).getSymbol());
        db.setName(list.get(position).getName());
        db.setLogoURI(list.get(position).getLogoURI());
        db.setDecimals(list.get(position).getDecimals());
        db.setWalletType(mWalletType);
        db.setShield("0");
        db.setUsdt(0.0);
        db.setAmount(0.0);
        db.save();
        list.get(position).setSelect(1);
        tokenAdapter.notifyDataSetChanged();
//                CacheData.shared().updateWalletInfoFlag = 66;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        addTokens();
    }

    private void searchTokenBNB() {
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
                if (TextUtils.isEmpty(content)) {
                    if (mTokensListBNB.size() > 0) {
                        tokenAdapterBNB = new AddBnbTokenAdapter(mTokensListBNB);
                        rvToken.setAdapter(tokenAdapterBNB);
                        selectClickBNB(mTokensListBNB);
                    }
                    return;
                }
                updateSearchContentBNB(content);
            }
        });
    }

    private void updateSearchContentBNB(String content) {
        List<BNBTokenBean> list = BnbTokenJsonUtils.searchTokens(content, mChain);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).getName();
                if (name.toUpperCase().equals(content.toUpperCase())) {
                    Collections.swap(list,0,i);
                }
            }
            tokenAdapterBNB = new AddBnbTokenAdapter(list);
            rvToken.setAdapter(tokenAdapterBNB);
            tokenAdapterBNB.notifyDataSetChanged();
            selectClickBNB(list);
        } else {
//            rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_token))));
            llAll.setVisibility(View.GONE);
            llCustomAdd.setVisibility(View.VISIBLE);
        }

    }

    private void addTokensBNB() {
        mTokensListBNB = BnbTokenJsonUtils.getTokenInfos(mChain);
        List<AddTokenDB> dbList = LitePal.where("walletAddress = ?", mPuk).find(AddTokenDB.class);
        if (mTokensListBNB.size() > 0) {
            List<BNBTokenBean> selectTokenList = new ArrayList<>();
            for (int i = 0; i < mTokensListBNB.size(); i++) {
                if (dbList.size() > 0) {
                    for (int j = 0; j < dbList.size(); j++) {
                        if (dbList.get(j).getTokenAddress().equals(mTokensListBNB.get(i).getContractAdd())) {
                            mTokensListBNB.get(i).setSelect(1);
                            selectTokenList.add(mTokensListBNB.get(i));
                            mTokensListBNB.remove(i);
                        }
                    }
                }
            }
            if (selectTokenList.size() > 0) {
                List<BNBTokenBean> listTemp = new ArrayList();
                String tokenAdd = "";
                for (int i = 0; i < selectTokenList.size(); i++) {
                    if (!tokenAdd.equals(selectTokenList.get(i).getContractAdd())) {
                        listTemp.add(selectTokenList.get(i));
                    }
                    tokenAdd = selectTokenList.get(i).getContractAdd();
                }
                selectTokenList.clear();
                selectTokenList.addAll(listTemp);
            }
            selectTokenList.addAll(mTokensListBNB);
            mTokensListBNB.clear();
            mTokensListBNB.addAll(selectTokenList);
            tokenAdapterBNB = new AddBnbTokenAdapter(mTokensListBNB);
            rvToken.setAdapter(tokenAdapterBNB);
            selectClickBNB(mTokensListBNB);
        }
    }

    private void selectClickBNB(List<BNBTokenBean> list) {
        tokenAdapterBNB.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.iv_select:
                    if (TextUtils.isEmpty(mWalletName)) {
                        showInfo(getString(R.string.token_select_add_wallet));
                        return;
                    }
                    insertDataBNB(list, position);
                    break;
            }
        });
        tokenAdapterBNB.setOnItemClickListener((adapter, view, position) -> insertDataBNB(list, position));
    }

    private void insertDataBNB(List<BNBTokenBean> list, int position) {
        if (TextUtils.isEmpty(mWalletName)) {
            showInfo(getString(R.string.token_select_add_wallet));
            return;
        }
        if (TextUtils.isEmpty(mPuk)) {
            showInfo(getString(R.string.not_yet));
            return;
        }
        if (list.get(position).getSelect() == 1) {
            showInfo(getString(R.string.not_need_add_again));
            return;
        }
        EnterPayPwd.password(this, 1, new IPassword() {
            @Override
            public void onPassword(String pwd) {
                if (!pwd.equals(mPwd)) {
                    showInfo(getString(R.string.pwd_error));
                    return;
                }

                AddTokenDB db = new AddTokenDB();
                db.setWalletAddress(mPuk);
                db.setTokenAddress(list.get(position).getContractAdd());
                db.setSymbol(list.get(position).getName());
                db.setName(list.get(position).getDec());
                db.setLogoURI(list.get(position).getLogoUrl());
                db.setDecimals(list.get(position).getDecimals());
                db.setWalletType(mWalletType);
                db.setShield("0");
                db.setUsdt(0.0);
                db.setAmount(0.0);
                db.save();
                list.get(position).setSelect(1);
                tokenAdapterBNB.notifyDataSetChanged();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
//            CacheData.shared().updateWalletInfoFlag = 66;
                addTokensBNB();
            }

            @Override
            public void onCancel() {

            }
        });

    }
}