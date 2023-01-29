package com.qianlihu.solanawallet.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AddTokenActivity;
import com.qianlihu.solanawallet.activity.AddWalletActivity;
import com.qianlihu.solanawallet.activity.CollectionActivity;
import com.qianlihu.solanawallet.activity.JoinMeetingActivity;
import com.qianlihu.solanawallet.activity.ManageWalletActivity;
import com.qianlihu.solanawallet.activity.MyWalletActivity;
import com.qianlihu.solanawallet.activity.QRActivity;
import com.qianlihu.solanawallet.activity.TransactionRecordActivity;
import com.qianlihu.solanawallet.activity.TransferActivity;
import com.qianlihu.solanawallet.adapter.AssetsAdapter;
import com.qianlihu.solanawallet.adapter.ChainNameAdapter;
import com.qianlihu.solanawallet.adapter.MyWalletAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SelectCurrencyAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.ChainNameBean;
import com.qianlihu.solanawallet.bean.UpdateWalletInfo;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.ErrorEvent;
import com.qianlihu.solanawallet.event.ResumeEvent;
import com.qianlihu.solanawallet.event.UpdateUserInfoEvent;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.track.TrackUsdtTransferbean;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.RepeatClickUtil;
import com.qianlihu.solanawallet.utils.SolanaUtil;
import com.qianlihu.solanawallet.utils.ThemeUtil;
import com.qianlihu.solanawallet.utils.WalletResource;
import com.qianlihu.solanawallet.utils.wallet_utils.AssociatedAccountUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.wallet.PublicKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.king.zxing.CaptureActivity.KEY_RESULT;
import static com.xuexiang.xutil.resource.ResUtils.getColor;

/**
 * author : Duan
 * date   : 2021/11/1 18:15
 * desc   : 应用钱包
 * version: 1.0.0
 */
public class ApplyWalletFragment extends BaseFragment {

    @BindView(R.id.rv_assets)
    RecyclerView rvAssets;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_my_wallet)
    TextView tvMyWallet;
    @BindView(R.id.swip_refresh)
    SwipeRefreshLayout swipRefresh;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.iv_polygon)
    ImageView ivPolygon;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.ll_flow)
    LinearLayout llFlow;
    @BindView(R.id.switch_net)
    Switch switchNet;
    @BindView(R.id.iv_night)
    ImageView ivNight;
    @BindView(R.id.tv_my_address)
    TextView tvMyAddress;

    private String mWalletType = "0";
    private String mMainChainType = Constant.SOL_CHAIN;
    private UpdateWalletInfo mWalletInfo;
    private String mPuk = "";
    private List<WalletBean> mWalletlist = new ArrayList<>();

    private boolean isHint = false;

    public static ApplyWalletFragment newInstance() {
        ApplyWalletFragment fragment = new ApplyWalletFragment();
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_apply_wallet;
    }

    @Override
    public void initView() {
        exChangeTheme(0);
        handler.sendEmptyMessage(117);
        mWalletInfo = new UpdateWalletInfo();
        rvAssets.setLayoutManager(new LinearLayoutManager(getContext()));
        swipRefresh.setColorSchemeColors(getColor(R.color.yellow));
        swipRefresh.setOnRefreshListener(() -> {
            swipRefresh.setRefreshing(false);
            handler.sendEmptyMessage(116);
        });
    }

    @Override
    public void initData() {
        initAboutWalletInfo();
        mWalletlist = WalletManager.getInstance().getWalletTypeInfo(mWalletType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resumeEvent(ResumeEvent event) {
        Log.i("duan==main", "应用onResume");
        updateWalletData();
        TrackUsdtTransferbean.transferUsdt();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHint = isVisibleToUser;
    }

    private void updateWalletData() {
        isHint = true;
        int flag = CacheData.shared().applyFlag;
        if (flag == 66) {
            CacheData.shared().applyFlag = 0;
            handler.sendEmptyMessage(116);
        }
        if (flag == 67) {
            CacheData.shared().applyFlag = 0;
            initAboutWalletInfo();
        }
        mWalletlist = WalletManager.getInstance().getWalletTypeInfo(mWalletType);
    }

    //获取usdt数据
    private void getUsd() {
        UpdateWalletInfo info = new UpdateWalletInfo();
        info.getUsd(mMainChainType, mPuk, mWalletType);
    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.tv_my_wallet, R.id.tv_my_assets, R.id.tv_scan, R.id.tv_fransfe, R.id.tv_collection, R.id.tv_add_currency, R.id.iv_night, R.id.tv_my_address})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_night:
                exChangeTheme(1);
                break;
            case R.id.tv_my_wallet:
                myWallet();
                break;
            case R.id.tv_my_assets:
                break;
            case R.id.tv_scan:
                if (mWalletlist.size() == 0) {
                    showInfo(getString(R.string.no_wallet));
                    return;
                }
                if (lacksPermission(writePermissions)) {
                    requestPermissions(writePermissions, 0);
                    return;
                }
                intent.setClass(getActivity(), QRActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_fransfe:
                selectCurrency(1, "");
                break;
            case R.id.tv_collection:
                selectCurrency(2, "");
                break;
            case R.id.tv_add_currency:
                if (mWalletlist.size() == 0) {
                    showInfo(getString(R.string.no_wallet));
                    return;
                }
                intent.putExtra(Constant.SOL_PUK, mPuk);
                intent.putExtra(Constant.WALLET_TYPE, mWalletType);
                intent.setClass(getActivity(), AddTokenActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 10088);
                break;
            case R.id.tv_my_address:
                ClickCopyUtils.copy(getContext(), mPuk);
                break;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            TrackUsdtTransferbean.transferUsdt();
            getUsd();
            if (msg.what == 116) {
//                initAboutWalletInfo();
                mWalletInfo.walletInfo(mPuk, mMainChainType, mWalletType);
            } else if (msg.what == 117) {
                changeMainChainType();
            }
            return true;
        }
    });

    private void exChangeTheme(int flag) {
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (flag == 0) {
            if (isDark) {
                ivNight.setImageResource(R.mipmap.icon_dark);
            } else {
                ivNight.setImageResource(R.mipmap.icon_light);
            }
        } else {
            if (isDark) {
                isDark = false;
                ivNight.setImageResource(R.mipmap.icon_light);
            } else {
                isDark = true;
                ivNight.setImageResource(R.mipmap.icon_dark);
            }
            ThemeUtil.reCreate(getActivity());
        }
        SPUtils.getInstance().put(Constant.NIGHT, isDark);
        ThemeUtil.setTheme(getActivity());
    }

    //初始化钱包信息
    public void initAboutWalletInfo() {
        CacheData.shared().walletType = mWalletType;
        List<WalletBean> list = LitePal.where("walletType = ?", mWalletType).find(WalletBean.class);
        Log.i("duan==wallet", "我的钱包== " + list.toString());
        if (list.size() > 0) {
            if (list.size() == 1) {
                defualtSelectWallet(list);
            } else {
                List<WalletBean> listSelect = LitePal.where("walletType = ? and select = ?", mWalletType, "1").find(WalletBean.class);
                if (listSelect.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        int isSelsct = list.get(i).getSelect();
                        if (isSelsct == 1) {
                            changeWallet(list, i);
                        }
                    }
                } else {
                    defualtSelectWallet(list);
                }

            }
        } else {
            tvMyWallet.setText(getString(R.string.my_wallet));
            tvMoney.setText("0.0000");
            rvAssets.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet_data))));
            tvMyAddress.setVisibility(View.GONE);
            return;
        }
    }

    //默认选中钱包
    private void defualtSelectWallet(List<WalletBean> list) {
        WalletBean bean = new WalletBean();
        bean.setSelect(1);
        mPuk = list.get(0).getPubKey();
        mMainChainType = list.get(0).getMainChain();
        bean.updateAll("pubKey = ? and walletType = ?", mPuk, mWalletType);
        changeWallet(list, 0);
    }

    //当主链类型为空时，修改主链类型为对应的主链型
    private void changeMainChainType() {
        List<WalletBean> beanList = LitePal.findAll(WalletBean.class);
        for (int i = 0; i < beanList.size(); i++) {
            if (TextUtils.isEmpty(beanList.get(i).getMainChain())) {
                WalletBean bean = new WalletBean();
                String puk = beanList.get(i).getPubKey();
                if (puk.length() == 44) {
                    bean.setMainChain(Constant.SOL_CHAIN);
                } else {
                    bean.setMainChain(Constant.BNB_CHAIN);
                }
                bean.updateAll("pubKey = ?", puk);
            }
        }
    }

    //跟新余额信息事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBalanceInfoEvent(UpdateUserInfoEvent event) {
        if (!mPuk.equals(event.getPuk())) {
            return;
        }
        showBalanceInfo();
    }

    //显示余额信息
    private AssetsAdapter assetsAdapter;

    private void showBalanceInfo() {
        List<AddTokenDB> dbList = LitePal.where("walletAddress = ? and walletType = ? and shield = ?", mPuk, mWalletType, "0").find(AddTokenDB.class);
        if (dbList.size() > 0) {
            double total = 0.0;
            for (int i = 0; i < dbList.size(); i++) {
                double usd = dbList.get(i).getUsdTotal();
                if (dbList.get(i).getSymbol() != null) {
                    String name = dbList.get(i).getSymbol();
                    if (name.equals("SOL") || name.equals("BNB") || name.equals("ETH")) {
                        Collections.swap(dbList, 0, i);
                    } else if (name.equals("WSOL")) {
                        Collections.swap(dbList, 1, i);
                    }
                }
                total = total + usd;
                tvMoney.setText(MyUtils.decimalFormat(total));
            }
            Log.i("duan==walletToken", "代币列表==  " + dbList.toString());
            assetsAdapter = new AssetsAdapter(dbList);
            rvAssets.setAdapter(assetsAdapter);
            assetsAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (dbList.get(position).getSymbol() != null) {
                    Intent intent = new Intent(getActivity(), TransactionRecordActivity.class);
                    intent.putExtra(Constant.SOL_PUK, mPuk);
                    intent.putExtra(Constant.MAIN_CHAIN, mMainChainType);
                    intent.putExtra(Constant.WALLET_TYPE, mWalletType);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addTokenDB", dbList.get(position));
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 10088);
                }
            });

            assetsAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                if (mPuk.equals(dbList.get(position).getWalletAddress())) {
                    DialogUtils.changeTip2(getActivity(), getString(R.string.tip_delete_token), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AddTokenDB tokenDB = new AddTokenDB();
                            tokenDB.setShield("1");
                            tokenDB.updateAll("walletAddress = ? and tokenAddress = ?", mPuk, dbList.get(position).getTokenAddress());
                            dbList.remove(position);
                            assetsAdapter.notifyDataSetChanged();
                        }
                    }, (dialog, which) -> {
                        LitePal.deleteAll(AddTokenDB.class, "walletAddress = ? and tokenAddress = ?", mPuk, dbList.get(position).getTokenAddress());
                        dbList.remove(position);
                        assetsAdapter.notifyDataSetChanged();
                    }, null);
                }
                return false;
            });
        } else {
            rvAssets.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void errorInfo(ErrorEvent event) {
        closeLoadingDialog();
//        showInfo(event.getMessage());
        String connectFail = "failed to connect to";
        if (isHint) {
            if (event.getMessage().contains(connectFail)) {
                showInfo(getString(R.string.connect_fail));
            } else {
                showInfo(event.getMessage());
            }
        }
        hideLoadingInfos();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                if (result.contains("join_meeting#")) {
                    String str1 = result.substring(0, result.indexOf("#"));
                    String str2 = result.substring(str1.length() + 1);
                    Intent intent = new Intent(getActivity(), JoinMeetingActivity.class);
                    intent.putExtra("roomNo", str2);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    return;
                }
                if (result.length() < 40) {
                    showInfo(getString(R.string.invalid_address));
                    return;
                }
                //获取到扫描二维码的数据
                selectCurrency(1, result);
            } else if (requestCode == 10088) {
                handler.sendEmptyMessage(116);
            }
        }
    }

    //我的钱包
    private void myWallet() {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_my_wallet, null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvMyWallet = view.findViewById(R.id.rv_my_wallet);
        RecyclerView rvMainChain = view.findViewById(R.id.rv_main_chain);
        Button btnManageWallet = view.findViewById(R.id.btn_manage_wallet);
        View viewLine = view.findViewById(R.id.view_line);
        Button btnAddWallet = view.findViewById(R.id.btn_add_wallet);
        final List<WalletBean>[] list = new List[]{LitePal.where("walletType = ? and mainChain = ?", mWalletType, mMainChainType).find(WalletBean.class)};
//        Log.i("duan==wallet", "所有钱包===   " + list.toString());
        rvMyWallet.setLayoutManager(new LinearLayoutManager(getContext()));
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
            rvMainChain.setLayoutManager(new LinearLayoutManager(getContext()));
            rvMainChain.setAdapter(mTypeAdapter);

            for (int i = 0; i < mChianName.length; i++) {
                if (mMainChainType.equals(mChianName[i])) {
                    mTypeAdapter.select(i);
                }
            }
            final MyWalletAdapter[] myWalletAdapter = {new MyWalletAdapter(list[0])};
            rvMyWallet.setAdapter(myWalletAdapter[0]);
            mTypeAdapter.setOnItemClickListener((adapter, view12, position) -> {
                list[0] = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChianName[position]).find(WalletBean.class);
                mTypeAdapter.select(position);
                if (list[0].size() != 0) {
                    myWalletAdapter[0] = new MyWalletAdapter(list[0]);
                    rvMyWallet.setAdapter(myWalletAdapter[0]);
                    myWalletAdapter[0].notifyDataSetChanged();
                    itemCheckWallet(myWalletAdapter, list, dialog);
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

            for (int i = 0; i < list[0].size(); i++) {
                int isSelsct = list[0].get(i).getSelect();
                if (isSelsct == 1) {
                    myWalletAdapter[0].selectItem(i);
                    tvMyWallet.setText(list[0].get(i).getName());
                }
            }
            itemCheckWallet(myWalletAdapter, list, dialog);
        } else {
            viewLine.setVisibility(View.GONE);
            rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet_data))));
        }

        ivClose.setOnClickListener(v -> dialog.dismiss());
        btnAddWallet.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("walletType", mWalletType);
            intent.setClass(getActivity(), AddWalletActivity.class);
            startActivity(intent);
        });

        btnManageWallet.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("walletType", mWalletType);
            intent.putExtra("mainChain", mMainChainType);
            intent.setClass(getActivity(), ManageWalletActivity.class);
            startActivity(intent);
        });
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    //钱包列表选项点击事件
    private void itemCheckWallet(MyWalletAdapter[] myWalletAdapter, List<WalletBean>[] list, BottomSheetDialog dialog) {
        myWalletAdapter[0].setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (RepeatClickUtil.isFastDoubleClick()) {
                    showInfo(getString(R.string.operate));
                    return;
                }
                myWalletAdapter[0].selectItem(position);
                WalletBean bean1 = new WalletBean();
                bean1.setSelect(0);
                bean1.updateAll("walletType = ?", mWalletType);
                //切换钱包修改选中状态
                WalletBean bean = new WalletBean();
                bean.setSelect(1);
                String puk = list[0].get(position).getPubKey();
                bean.saveOrUpdate("pubKey = ? and walletType = ?", puk, mWalletType);
                changeWallet(list[0], position);
                dialog.dismiss();
            }
        });

        myWalletAdapter[0].setOnItemChildClickListener((adapter, view1, position) -> {
            if (view1.getId() == R.id.iv_ed_wallet) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallet", list[0].get(position));
                intent.putExtras(bundle);
                intent.setClass(getActivity(), MyWalletActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    //切换钱包
    private void changeWallet(List<WalletBean> list, int position) {
        mPuk = list.get(position).getPubKey();
        tvMyWallet.setText(list.get(position).getName());
        tvMyAddress.setVisibility(View.VISIBLE);
        tvMyAddress.setText(list.get(position).getPubKey());
        CacheData.shared().collectionPwd = "";
        showBalanceInfo();
        mMainChainType = list.get(position).getMainChain();
        List<AddTokenDB> tokenDBList = LitePal.where("walletAddress = ? and walletType = ?", mPuk, mWalletType).find(AddTokenDB.class);
        double usdTotal = 0.0;
        if (tokenDBList.size() > 0) {
            for (int i = 0; i < tokenDBList.size(); i++) {
                usdTotal = usdTotal + tokenDBList.get(i).getUsdTotal();
            }
        }
        tvMoney.setText(MyUtils.decimalFormat(usdTotal));
        handler.sendEmptyMessage(116);
    }

    //选择币种类型
    private void selectCurrency(int type, String tranferAddress) {
        if (TextUtils.isEmpty(mPuk)) {
            showInfo(getString(R.string.no_wallet));
            return;
        }
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.setCancelable(false);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_currency, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvCurrency = view.findViewById(R.id.rv_currency);
        rvCurrency.setLayoutManager(new LinearLayoutManager(getContext()));
        List<AddTokenDB> dbList = LitePal.where("walletAddress = ? and walletType = ?", mPuk, mWalletType).find(AddTokenDB.class);
        SelectCurrencyAdapter currencyAdapter = new SelectCurrencyAdapter(dbList);
        rvCurrency.setAdapter(currencyAdapter);
        currencyAdapter.setOnItemClickListener((adapter, view1, position) -> {
            sheetDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra(Constant.SOL_PUK, mPuk);
            intent.putExtra(Constant.MAIN_CHAIN, mMainChainType);
            Bundle bundle = new Bundle();
            bundle.putSerializable("addTokenDB", dbList.get(position));
            intent.putExtras(bundle);
            if (type == 1) {
                if (!TextUtils.isEmpty(tranferAddress)) {
                    intent.putExtra("address", tranferAddress);
                }
                intent.setClass(getActivity(), TransferActivity.class);
            } else {
                intent.setClass(getActivity(), CollectionActivity.class);
            }
            startActivityForResult(intent, 10088);
//            startActivity(intent);
        });
        ivClose.setOnClickListener(v -> sheetDialog.dismiss());
        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                getActivity().finish();
            }
        }
    }
}
