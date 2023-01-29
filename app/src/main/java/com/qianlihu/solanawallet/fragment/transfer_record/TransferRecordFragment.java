package com.qianlihu.solanawallet.fragment.transfer_record;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.RecordDetailActivity;
import com.qianlihu.solanawallet.adapter.BnbTransderRecordDBAdapter;
import com.qianlihu.solanawallet.adapter.EthTransderRecordDBAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SolTokenTransderRecordAdapter;
import com.qianlihu.solanawallet.adapter.SolTransderRecordAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.BNBTransferRecordDB;
import com.qianlihu.solanawallet.bean.EthTransferRecordDB;
import com.qianlihu.solanawallet.bean.SolTokenTransferRecordBean;
import com.qianlihu.solanawallet.bean.SolanaTransferRecordBean;
import com.qianlihu.solanawallet.bean.UpdateBnbTransferRecordInfo;
import com.qianlihu.solanawallet.bean.UpdateEthTransferRecordInfo;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.DataUpdateEvent;
import com.qianlihu.solanawallet.event.ErrorEvent;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

/**
 * author : Duan
 * date   : 2022/3/3015:01
 * desc   : 交易记录
 * version: 1.0.0
 */
public class TransferRecordFragment extends BaseFragment {
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_foot_tip)
    TextView tvFootTip;

    private String mPuk = "";
    private String name = "";
    private String mMint = "";
    private String mWalletType = "0";
    private boolean isConnect = false;
    private String logoUrl = "";
    private AddTokenDB db = null;
    private String mMainChain = "";
    private int decimals = 0;
    private double mUsd = 0.0;

    private int page = 1, pageSize = 10, bnbTotal = 0;
    private int solTotal = 0;

    public static Fragment newInstance(AddTokenDB db, String puk, String mainChain, String walletType) {
        TransferRecordFragment fragment = new TransferRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("addTokenDB", db);
        bundle.putString(Constant.SOL_PUK, puk);
        bundle.putString(Constant.MAIN_CHAIN, mainChain);
        bundle.putString(Constant.WALLET_TYPE, walletType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_transfer_record;
    }

    @Override
    public void initView() {

        rvRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        mWalletType = getArguments().getString(Constant.WALLET_TYPE);
        isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET);

        db = (AddTokenDB) getArguments().getSerializable("addTokenDB");
        name = db.getSymbol();
        mMint = db.getTokenAddress();
        logoUrl = db.getLogoURI();
        decimals = db.getDecimals();
        mUsd = db.getUsd();
        mPuk = getArguments().getString(Constant.SOL_PUK);
        mMainChain = getArguments().getString(Constant.MAIN_CHAIN);
        if (mMainChain.equals(Constant.SOL_CHAIN)) {
            page = 0;
        }
        setupRefreshView();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }

    private void setupRefreshView() {
        refreshLayout.autoRefresh();
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> reloadAllData());
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            page = page + 1;
            if (mMainChain.equals(Constant.SOL_CHAIN)) {
//                pageSize = pageSize + 10;
                if (solTotal < pageSize) {
                    tvFootTip.setVisibility(View.VISIBLE);
                    refreshlayout.finishLoadMore();
                    return;
                }
                if (name.equals(Constant.SOL_CHAIN)) {
                    getSolanaTransferRecord();
                } else {
                    getSolTokenRecord();
                }
            } else if (mMainChain.equals(Constant.BNB_CHAIN)) {
                pageSize = pageSize + 10;
                if (bnbTotal < pageSize) {
                    tvFootTip.setVisibility(View.VISIBLE);
                    refreshlayout.finishLoadMore();
                    return;
                }
                bnbReloadMore();
            } else if (mMainChain.equals(Constant.ETH_CHAIN)) {
                pageSize = pageSize + 10;
                if (bnbTotal < pageSize) {
                    tvFootTip.setVisibility(View.VISIBLE);
                    refreshlayout.finishLoadMore();
                    return;
                }
                ethReloadMore();
            }
        });
    }

    //下拉刷新
    private void reloadAllData() {
        page = 1;
        if (mMainChain.equals(Constant.SOL_CHAIN)) {
            page = 0;
        }
        pageSize = 10;
        getRecord();
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setNoMoreData(false);
        tvFootTip.setVisibility(View.GONE);
    }

    //结束刷新
    public void finishRefresh() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }

    }

    private void getRecord() {
        if (mMainChain.equals(Constant.SOL_CHAIN)) {
            if (name.equals(Constant.SOL_CHAIN)) {
                getSolanaTransferRecord();
            } else {
                getSolTokenRecord();
            }
        } else if (mMainChain.equals(Constant.BNB_CHAIN)) {
            bnbReloadMore();
            UpdateBnbTransferRecordInfo recordInfosss = new UpdateBnbTransferRecordInfo();
            recordInfosss.transferRecord(mPuk, mWalletType, mMint, name, page, pageSize);
        } else if (mMainChain.equals(Constant.ETH_CHAIN)) {
            ethReloadMore();
            UpdateEthTransferRecordInfo recordInfosss = new UpdateEthTransferRecordInfo();
            recordInfosss.transferRecord(mPuk, mWalletType, mMint, name, page, pageSize);
        }
    }

    private void getSolanaTransferRecord() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", mPuk);
        map.put("offset", page);
        map.put("limit", pageSize);
        String url = "https://api.solscan.io/account/soltransfer/txs";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> finishRefresh());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> finishRefresh());
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    SolanaTransferRecordBean bean = gson.fromJson(body, SolanaTransferRecordBean.class);
                    if (bean != null) {
                        if (bean.isSucccess()) {
                            if (bean.getData().getTx() != null) {
                                solTotal = bean.getData().getTx().getTotal();
                                runOnUiThread(() -> {
                                    if (solTotal > 0) {
                                        solanaRecord(bean.getData().getTx().getTransactions());
                                    } else {
                                        if (rvRecord != null) {
                                            rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void solanaRecord(List<SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean> list) {
        SolTransderRecordAdapter recordAdapter = new SolTransderRecordAdapter(list, mPuk, name, mUsd);
        if (rvRecord != null) {
            rvRecord.setAdapter(recordAdapter);
        }
        recordAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), RecordDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("record", list.get(position));
            intent.putExtras(bundle);
            intent.putExtra(Constant.SOL_PUK, mPuk);
            intent.putExtra(Constant.MAIN_CHAIN, mMainChain);
            intent.putExtra("name", name);
            startActivity(intent);
        });
    }

    private void getSolTokenRecord() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", mPuk);
        map.put("offset", page);
        map.put("limit", pageSize);
        String url = "https://api.solscan.io/account/token/txs";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> finishRefresh());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> finishRefresh());
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    SolTokenTransferRecordBean bean = gson.fromJson(body, SolTokenTransferRecordBean.class);
                    if (bean != null) {
                        if (bean.isSucccess()) {
                            if (bean.getData() != null) {
                                if (bean.getData().getTx() != null) {
                                    solTotal = bean.getData().getTx().getTotal();
                                    runOnUiThread(() -> {
                                        if (solTotal > 0) {
                                            List<SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean> list = new ArrayList<>();
                                            for (int i = 0; i < bean.getData().getTx().getTransactions().size(); i++) {
                                                String tokenAddress = bean.getData().getTx().getTransactions().get(i).getChange().getTokenAddress();
                                                if (tokenAddress.equals(mMint)) {
                                                    list.add(bean.getData().getTx().getTransactions().get(i));
                                                }
                                            }
                                            if (list.size() > 0) {
                                                solanaTokenRecord(list);
                                            } else {
                                                if (rvRecord != null) {
                                                    rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.pull_up_loading_record))));
                                                }
                                            }
                                        } else {
                                            if (rvRecord != null) {
                                                rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void solanaTokenRecord(List<SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean> list) {
        if (solTotal > 0) {
            SolTokenTransderRecordAdapter recordAdapter = new SolTokenTransderRecordAdapter(list, name, mUsd);
            if (rvRecord != null) {
                rvRecord.setAdapter(recordAdapter);
            }
            recordAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent = new Intent(getContext(), RecordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("record", list.get(position));
                intent.putExtras(bundle);
                intent.putExtra(Constant.SOL_PUK, mPuk);
                intent.putExtra(Constant.MAIN_CHAIN, mMainChain);
                intent.putExtra("name", name);
                startActivity(intent);
            });
        } else {
            if (rvRecord != null) {
                rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
            }
        }
    }

    //BNB转账记录更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BnbtransferInfoEvent(List<BNBTransferRecordDB> event) {
        finishRefresh();
        if (mMainChain.equals(Constant.BNB_CHAIN)) {
            page = 1;
            pageSize = 10;
            queryBnbDB(event);
        }
    }

    //ETH转账记录更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EthtransferInfoEvent(List<EthTransferRecordDB> event) {
        finishRefresh();
        if (mMainChain.equals(Constant.ETH_CHAIN)) {
            page = 1;
            pageSize = 10;
            queryEthDB(event);
        }
    }

    private void bnbReloadMore() {
        List<BNBTransferRecordDB> dbs = LitePal.where("myPuk = ? and walletType = ? and contractAddress = ?", mPuk, mWalletType, mMint.toLowerCase())
                .order("timeStamp desc")
                .limit(pageSize)
                .find(BNBTransferRecordDB.class);
        queryBnbDB(dbs);
    }

    private void ethReloadMore() {
        List<EthTransferRecordDB> dbs = LitePal.where("myPuk = ? and walletType = ? and contractAddress = ?", mPuk, mWalletType, mMint.toLowerCase())
                .order("timeStamp desc")
                .limit(pageSize)
                .find(EthTransferRecordDB.class);
        queryEthDB(dbs);
    }


    //显示bnb转账记录列表
    private void queryBnbDB(List<BNBTransferRecordDB> dbs) {
        bnbTotal = dbs.size();
        finishRefresh();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bnbTotal > 0) {
                    BnbTransderRecordDBAdapter recordAdapter = new BnbTransderRecordDBAdapter(dbs, mPuk, mUsd);
                    if (rvRecord != null) {
                        rvRecord.setAdapter(recordAdapter);
                    }
                    recordAdapter.setOnItemClickListener((adapter, view, position) -> {
                        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("record", dbs.get(position));
                        intent.putExtras(bundle);
                        intent.putExtra(Constant.SOL_PUK, mPuk);
                        intent.putExtra("decimals", decimals);
                        intent.putExtra(Constant.MAIN_CHAIN, mMainChain);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    });

                    recordAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                        DialogUtils.changeTip(getActivity(), getString(R.string.delete_record), (dialog, which) -> {
                            dbs.get(position).delete();
                            dbs.remove(position);
                            recordAdapter.notifyDataSetChanged();
                        }, null);
                        return false;
                    });
                } else {
                    if (rvRecord != null) {
                        rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
                    }
                }
            }
        });
    }

    private void queryEthDB(List<EthTransferRecordDB> dbs) {
        bnbTotal = dbs.size();
        finishRefresh();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bnbTotal > 0) {
                    EthTransderRecordDBAdapter recordAdapter = new EthTransderRecordDBAdapter(dbs, mPuk, mUsd);
                    if (rvRecord != null) {
                        rvRecord.setAdapter(recordAdapter);
                    }
                    recordAdapter.setOnItemClickListener((adapter, view, position) -> {
                        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("record", dbs.get(position));
                        intent.putExtras(bundle);
                        intent.putExtra(Constant.SOL_PUK, mPuk);
                        intent.putExtra("decimals", decimals);
                        intent.putExtra(Constant.MAIN_CHAIN, mMainChain);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    });

                    recordAdapter.setOnItemLongClickListener((adapter, view, position) -> {
                        DialogUtils.changeTip(getActivity(), getString(R.string.delete_record), (dialog, which) -> {
                            dbs.get(position).delete();
                            dbs.remove(position);
                            recordAdapter.notifyDataSetChanged();
                        }, null);
                        return false;
                    });
                } else {
                    if (rvRecord != null) {
                        rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void errorTransferInfo(ErrorEvent event) {
        finishRefresh();
        closeLoadingDialog();
        String connectFail = "failed to connect to";
        if (event.getMessage().contains(connectFail)) {
            showInfo(getString(R.string.connect_fail));
        } else {
            if (event.getCode() == 412) {
                String msg = event.getMessage();
                if (msg.equals("No transactions found")) {
                    rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_transaction_record))));
                } else {
                    showInfo(msg);
                }
            } else {
                showInfo(getString(R.string.request_exception));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void firstUpdateTransferInfo(DataUpdateEvent event) {
        finishRefresh();
        if (rvRecord != null) {
            if (event.getTotal() > 0) {
                if (mMainChain.equals(Constant.SOL_CHAIN) || mMainChain.equals(Constant.BNB_CHAIN)) {
                    rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.loading_data))));
                } else {
                    rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
                }
            } else {
                rvRecord.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
            }
        }

    }
}
