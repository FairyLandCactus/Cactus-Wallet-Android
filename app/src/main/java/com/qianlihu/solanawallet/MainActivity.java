package com.qianlihu.solanawallet;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.common.collect.ComparisonChain;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.activity.BindUserActivity;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.BottomTabBean;
import com.qianlihu.solanawallet.bean.BoundBean;
import com.qianlihu.solanawallet.bean.UpdateWalletInfo;
import com.qianlihu.solanawallet.bean.VersionBean;
import com.qianlihu.solanawallet.bitcoin.RequestWalletBalanceTask;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.HideMianTabEvent;
import com.qianlihu.solanawallet.event.ResumeEvent;
import com.qianlihu.solanawallet.fragment.ApplyWalletFragment;
import com.qianlihu.solanawallet.fragment.GamefiFragment;
import com.qianlihu.solanawallet.fragment.SaveWalletFragment;
import com.qianlihu.solanawallet.fragment.dapp.DappFragment;
import com.qianlihu.solanawallet.fragment.swap.SwapFragment;
import com.qianlihu.solanawallet.utils.DialogView;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.ThreadUtil;
import com.qianlihu.solanawallet.utils.TokenMintUtil;
import com.qianlihu.solanawallet.view.UpdateDialog;
import com.qianlihu.solanawallet.wallet.Account;
import com.qianlihu.solanawallet.wallet.CreateMnemoics;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.service.OnFileDownloadListener;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PrefixedChecksummedBytes;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletTransaction;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements UpdateDialog.Callback {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.lin)
    LinearLayout llLin;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.rl_start_page)
    RelativeLayout rlStartPage;

    private List<String> title = new ArrayList<>();
    private Integer[] selectIcon = new Integer[]{
            R.mipmap.icon_tab_select_save_wallet,
            R.mipmap.icon_tab_select_wallet,
            R.mipmap.icon_tab_select_swap,
            R.mipmap.icon_tab_select_dapp,
            R.mipmap.icon_tab_select_gamefi};
    private Integer[] unSelectIcon = new Integer[]{
            R.mipmap.icon_tab_unselect_save_wallet,
            R.mipmap.icon_tab_unselect_wallet,
            R.mipmap.icon_tab_unselect_swap,
            R.mipmap.icon_tab_unselect_dapp,
            R.mipmap.icon_tab_unselect_gimefi};
    private List<BottomTabBean> mDataList = new ArrayList<>();

    private long waitTime = 2000;
    private long touchTime = 0;

    private int mFragmentIndex = 0;
    private List<BaseFragment> mFragmentList;

    private UpdateDialog updateDialog;
    private ProgressDialog pd;

    private String[] writePermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
//        List<String> mnList = CreateMnemoics.generateMnemonics(16);
//        String sss = MnemonicUtils.listToString(mnList);
//        try {
//            String puk2 = createWallet(sss);
//            Log.i("duan==createWallet", "加密后的公钥22===   " + puk2);
//            Log.i("duan==createWallet", "助记词===   " + sss);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        getBtcBalannce();
//        sendBTC();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                sendBTC();
//                getBtcBalannce();
//                Signingtrasaction();
//                requestWalletBalance();
            }
        }).start();
//        requestWalletBalance();
    }

    private void createSolWallet() {
        List<String> list = CreateMnemoics.generateMnemonics(16);
        Log.i("duan==Mn", "助记词+ " + list.toString());
        if (list.size() > 0) {
            for (int i = 0; i < 10000; i++) {
                Account acc = Account.fromMnemonic2(list, "", i);
                if (acc == null) {
                    return;
                }
                String pubKey = acc.getPublicKey().toString();
                Log.i("duan==walletPuk", "钱包地址===" + i + "   " + pubKey + "  ,");
                String encodePvaKay = Base58.encode(acc.getSecretKey());//加密
//                Log.i("duan==walletPuk", "私钥==="+i+"   " + encodePvaKay);
            }
        }
    }

    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    public MutableLiveData<Wallet> walletToSweeps = new MutableLiveData<>();
    private static final Comparator<UTXO> UTXO_COMPARATOR = (lhs, rhs) -> ComparisonChain.start().compare(lhs.getHash(), rhs.getHash()).compare(lhs.getIndex(), rhs.getIndex())
            .result();

    private void requestWalletBalance() {
//        backgroundThread = new HandlerThread("backgroundThread", Process.THREAD_PRIORITY_BACKGROUND);
//        backgroundThread.start();
//        backgroundHandler = new Handler(backgroundThread.getLooper());
//        final RequestWalletBalanceTask.ResultCallback callback = new RequestWalletBalanceTask.ResultCallback() {
//            @Override
//            public void onResult(final Set<UTXO> utxos) {
        String input = "cTtkeP47JoiJH9sBxXTmCZQxvf1kjKVQrQWRpDEY1MkM1iXDTPwF";
        ECKey key = DumpedPrivateKey.fromBase58(Constant.NETWORK_PARAMETERS, input).getKey();
        final Wallet walletToSweep = Wallet.createBasic(Constant.NETWORK_PARAMETERS);
        walletToSweep.importKey(key);
        final MutableLiveData<Wallet> walletToSweeps = new MutableLiveData<>();
        walletToSweeps.setValue(walletToSweep);
        walletToSweeps.setValue(walletToSweep);
        walletToSweeps.observe(this, wallet -> {
            Log.i("duan==wallet", "钱包余额=== " + wallet.getBalance(Wallet.BalanceType.ESTIMATED));
        });
//                Address address = LegacyAddress.fromKey(Constant.NETWORK_PARAMETERS, key);
//
//                final Wallet wallet = Wallet.fromKeys(Constant.NETWORK_PARAMETERS, Arrays.asList(key));
//
//
//                // Filter UTXOs we've already spent and sort the rest.
//                final Set<Transaction> walletTxns = wallet.getTransactions(false);
//                final Set<UTXO> sortedUtxos = new TreeSet<>(UTXO_COMPARATOR);
//                for (final UTXO utxo : utxos)
//                    if (!utxoSpentBy(walletTxns, utxo))
//                        sortedUtxos.add(utxo);
//
//                // Fake transaction funding the wallet to sweep.
//                final Map<Sha256Hash, Transaction> fakeTxns = new HashMap<>();
//                for (final UTXO utxo : sortedUtxos) {
//                    Transaction fakeTx = fakeTxns.get(utxo.getHash());
//                    if (fakeTx == null) {
//                        fakeTx = new FakeTransaction(Constant.NETWORK_PARAMETERS, utxo.getHash(), utxo.getHash());
//                        fakeTx.getConfidence().setConfidenceType(TransactionConfidence.ConfidenceType.BUILDING);
//                        fakeTxns.put(fakeTx.getTxId(), fakeTx);
//                    }
//                    final TransactionOutput fakeOutput = new TransactionOutput(Constant.NETWORK_PARAMETERS, fakeTx,
//                            utxo.getValue(), utxo.getScript().getProgram());
//                    // Fill with output dummies as needed.
//                    while (fakeTx.getOutputs().size() < utxo.getIndex())
//                        fakeTx.addOutput(new TransactionOutput(Constant.NETWORK_PARAMETERS, fakeTx,
//                                Coin.NEGATIVE_SATOSHI, new byte[] {}));
//                    // Add the actual output we will spend later.
//                    fakeTx.addOutput(fakeOutput);
//                }
//
//                final Wallet walletToSweep = walletToSweeps.getValue();
//                walletToSweep.clearTransactions(0);
//                for (final Transaction tx : fakeTxns.values())
//                    walletToSweep.addWalletTransaction(new WalletTransaction(WalletTransaction.Pool.UNSPENT, tx));
//                        walletToSweep.toString(false, false, null, true, false, null);
//                walletToSweeps.setValue(walletToSweep);
//            }
//
//            private boolean utxoSpentBy(final Set<Transaction> transactions, final UTXO utxo) {
//                for (final Transaction tx : transactions) {
//                    for (final TransactionInput input : tx.getInputs()) {
//                        final TransactionOutPoint outpoint = input.getOutpoint();
//                        if (outpoint.getHash().equals(utxo.getHash()) && outpoint.getIndex() == utxo.getIndex())
//                            return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public void onFail(final int messageResId, final Object... messageArgs) {
//
//            }
//        };
//
//        final Wallet walletToSweep = walletToSweeps.getValue();
//        final ECKey key = walletToSweep.getImportedKeys().iterator().next();
//        new RequestWalletBalanceTask(backgroundHandler, callback).requestWalletBalance(getAssets(), key);
    }

    private static class FakeTransaction extends Transaction {
        private final Sha256Hash txId, wTxId;

        public FakeTransaction(final NetworkParameters params, final Sha256Hash txId, final Sha256Hash wTxId) {
            super(params);
            this.txId = txId;
            this.wTxId = wTxId;
        }

        @Override
        public Sha256Hash getTxId() {
            return txId;
        }

        @Override
        public Sha256Hash getWTxId() {
            return wTxId;
        }
    }

    @Override
    protected void initData() {

        title.add(getString(R.string.wallet));
        title.add(getString(R.string.storage));
        title.add(getString(R.string.dapp));
        title.add(getString(R.string.swap));
        title.add(getString(R.string.gamefi));

        mFragmentList = new ArrayList<>();
        if (mFragmentList.size() > 0) {
            mFragmentList.clear();
        }
        mFragmentList.add(ApplyWalletFragment.newInstance());
        mFragmentList.add(SaveWalletFragment.newInstance());
        mFragmentList.add(DappFragment.newInstance());
        mFragmentList.add(SwapFragment.newInstance());
        mFragmentList.add(GamefiFragment.newInstance());

        for (int i = 0; i < title.size(); i++) {
            BottomTabBean bean = new BottomTabBean();
            bean.setIcon(selectIcon[i]);
            bean.setTitle(title.get(i));
            bean.setUnIcon(unSelectIcon[i]);
//            bean.setFragment(list.get(i));
            mDataList.add(bean);
        }
        MagicTitleUtils.magicBottomTabView(MainActivity.this, mDataList, magicIndicator, viewPager);
        rlStartPage.setVisibility(View.VISIBLE);
        BaseTabAdapter tabAdapter = new BaseTabAdapter(getSupportFragmentManager(), mFragmentList);
        if (viewPager != null) {
            viewPager.setAdapter(tabAdapter);
            viewPager.setOffscreenPageLimit(mDataList.size());
        }
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            runOnUiThread(() -> {
//                BaseTabAdapter tabAdapter = new BaseTabAdapter(getSupportFragmentManager(), mFragmentList);
//                if (viewPager != null) {
//                    viewPager.setAdapter(tabAdapter);
//                    viewPager.setOffscreenPageLimit(mDataList.size());
//                }
            });
            handler.sendEmptyMessage(118);
            handler.sendEmptyMessage(116);
        }).start();

        new Thread(() -> checkAppVersion()).start();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragmentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (lacksPermission(writePermissions)) {
            requestPermissions(writePermissions, 0);
            return;
        }
        UpdateWalletInfo info = new UpdateWalletInfo();
        info.uploadWalletInfo(MainActivity.this, lacksPermission(writePermissions));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CacheData.shared().isExchange) {
            CacheData.shared().isExchange = false;
            viewPager.setCurrentItem(3);
        }
        EventBus.getDefault().post(new ResumeEvent(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //聊天页面输入框键盘弹出隐藏对底部tab显隐
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isHideTabEvent(HideMianTabEvent event) {
        if (event.isHideTab()) {
            llLin.setVisibility(View.GONE);
        } else {
            llLin.setVisibility(View.VISIBLE);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            closeLoadingDialog();
            if (msg.what == 116) {
                try {
                    ThreadUtil.getInstance().add(MainActivity.this.getClass().getMethod("getCoinType"), MainActivity.this);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 118) {
                rlStartPage.setVisibility(View.GONE);
            }
            return true;
        }
    });

    public void getCoinType() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        HttpService.doGet(Constant.SOL_TOKEN_URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                getCoinType();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.i("duan==tokenlist", "tokenlist===  " + body);
                if (!TextUtils.isEmpty(body)) {
                    TokenMintUtil.getTokenInfos(body);
                }
            }
        });
    }

    public static int REQUESTPERMISSION = 110;

    @Override
    public void update(String apkUrl) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
//            showToast( "请允许权限进行下载安装");
            showInfo(getString(R.string.version_update_permission));
        } else {
            useApkDownLoadFunction(apkUrl);
        }
    }

    private void useApkDownLoadFunction(String apkUrl) {
        if (updateDialog != null) {
            updateDialog.dismiss();
            showInfo(getString(R.string.downloading_update));
        }
        pd = new ProgressDialog(this);
        String title = getString(R.string.version_update);
        String content = getString(R.string.app_downloading);
        pd.setTitle(title);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage(content);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setMax(100);
        pd.show();

        XUpdate.newBuild(this)
                .apkCacheDir(getExtDownloadsPath())
                .build()
                .download(apkUrl, new OnFileDownloadListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onProgress(float progress, long total) {
                        int ps = Math.round(progress * 100);
//                        Log.i("duan==onProgress", "下载进度== " + ps);
                        runOnUiThread(() -> pd.setProgress(ps));
                    }

                    @Override
                    public boolean onCompleted(File file) {
                        runOnUiThread(() -> pd.dismiss());
                        Log.i("duan==onProgress", "apk下载完毕，文件路径== " + file.getPath());
//                        installAPK(file);
                        _XUpdate.startInstallApk(MainActivity.this, file);
                        return false;
                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        Log.e("duan==onError", "" + throwable.getMessage());
                        runOnUiThread(() -> {
                            pd.dismiss();
                            showInfo(getString(R.string.xupdate_error_install_failed));
                            finish();
                        });

                    }
                });
    }

    /**
     * 获取下载目录
     * <pre>path: /storage/emulated/0/Download</pre>
     *
     * @return 下载目录
     */
    public String getExtDownloadsPath() {
        String path = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            path = getExternalFilesDir("apk").getAbsolutePath();
        } else {
            path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();
        }
        return path;
    }

    private void checkAppVersion() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        HttpService.doGet(Constant.APP_UPDATE_URL, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==VersionBean", "VersionBean===  " + body);
                if (!TextUtils.isEmpty(body)) {
                    Gson gson = new Gson();
                    VersionBean bean = gson.fromJson(body, VersionBean.class);
                    runOnUiThread(() -> {
                        if (bean != null) {
                            SPUtils.getInstance().put("versionCode", bean.getVersionCode());
                            if (bean.getVersionCode() > AppUtils.getAppVersionCode()) {
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    //申请权限
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
                                    showInfo(getString(R.string.version_update_permission));
                                } else {
                                    useApkDownLoadFunction(bean.getUrl());
                                }
//                                updateDialog = new UpdateDialog(MainActivity.this, getString(R.string.new_version), bean.getVersionName(), bean.getContent(), bean.getUrl());
//                                updateDialog.setCallback(MainActivity.this);
//                                updateDialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragmentIndex == 3) {
            Fragment fragment = mFragmentList.get(3);
            ((SwapFragment) fragment).onKeyDownChild(keyCode, event);
            return true;
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - touchTime) >= waitTime) {
                    //让Toast的显示时间和等待时间相同
                    showInfo(getString(R.string.press_again_to_exit));
                    touchTime = currentTime;
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                dialogTip(getString(R.string.permissions_tip));
            }
            UpdateWalletInfo info = new UpdateWalletInfo();
            info.uploadWalletInfo(MainActivity.this, lacksPermission(writePermissions));
        }
    }
}