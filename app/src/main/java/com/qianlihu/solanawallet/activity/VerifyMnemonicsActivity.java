package com.qianlihu.solanawallet.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.BuildConfig;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.VerifyMnemonicsAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.utils.wallet_utils.MnemonicUtils;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;
import com.qianlihu.solanawallet.wallet.Account;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.LitePal;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyMnemonicsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_word)
    EditText etWord;
    @BindView(R.id.et_again_word)
    EditText etAgainWord;
    @BindView(R.id.tv_random_word)
    TextView tvRandomWord;
    @BindView(R.id.tv_random_word_2)
    TextView tvRandomWord2;
    @BindView(R.id.tv_mn_1)
    TextView tvMn1;
    @BindView(R.id.tv_mnemonics_1)
    TextView tvMnemonics1;
    @BindView(R.id.tv_mn_2)
    TextView tvMn2;
    @BindView(R.id.tv_mnemonics_2)
    TextView tvMnemonics2;
    @BindView(R.id.tv_mn_3)
    TextView tvMn3;
    @BindView(R.id.tv_mnemonics_3)
    TextView tvMnemonics3;
    @BindView(R.id.rv_mnemonics)
    RecyclerView rvMnemonics;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private List<String> mnList;
    private List<String> upsetMnList;
    private int indexRandomFirst = 1;
    private int indexRandomSecond = 1;
    private int indexRandomThird = 1;
    private String mWalletType = "0";
    private String mainChain = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_mnemonics;
    }

    @Override
    protected void initView() {
        tvTitle.setVisibility(View.GONE);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void initData() {
        mnList = getIntent().getStringArrayListExtra("mnemonics1");
        upsetMnList = getIntent().getStringArrayListExtra("mnemonics2");
        mWalletType = getIntent().getStringExtra("walletType");
        mainChain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
        if (mnList.size() > 0) {
            Random random = new Random();
            int[] arr = new int[3];
            //元素默认为-1
            for (int i = 0; i < arr.length; i++) {
                arr[i] = -1;
            }
            //下标
            int indext = 0;
            int max = mnList.size();
            while (indext < arr.length) {
                //是否重复
                boolean finth = true;
                //生成一个随机数可以修改
                int a = random.nextInt(max);
                //遍历数组
                for (int i = 0; i < arr.length; i++) {
                    //判断是否有重复
                    if (arr[i] == a) {
                        finth = false;
                    }
                }
                //若没有重复则赋值
                if (finth) {
                    arr[indext++] = a;
                }
            }
//            Log.i("duan==random", "随机数===  " + Arrays.toString(arr));
            indexRandomFirst = arr[0];
            indexRandomSecond = arr[1];
            indexRandomThird = arr[2];

            tvMn1.setText(String.format(getString(R.string.enter_th_word_1), (indexRandomFirst + 1)));
            tvMn2.setText(String.format(getString(R.string.enter_th_word_1), (indexRandomSecond + 1)));
            tvMn3.setText(String.format(getString(R.string.enter_th_word_1), (indexRandomThird + 1)));
            initMnemonics();
        }
    }


    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                verifyConfirm();
                break;
        }
    }

    private void initMnemonics() {
        if (upsetMnList.size() > 0) {
            Collections.shuffle(upsetMnList);
        }
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < upsetMnList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("mn", upsetMnList.get(i));
            map.put("flag", -1);
            mapList.add(map);
        }

        rvMnemonics.setLayoutManager(new GridLayoutManager(this, 3));
        rvMnemonics.addItemDecoration(new GridSpaceItemDecoration(3, 40, 40));
        VerifyMnemonicsAdapter mnemonicsAdapter = new VerifyMnemonicsAdapter(mapList);
        rvMnemonics.setAdapter(mnemonicsAdapter);

        tvMnemonics1.setOnClickListener(v -> clearNullSelect(mapList, tvMnemonics1, mnemonicsAdapter));

        tvMnemonics2.setOnClickListener(v -> clearNullSelect(mapList, tvMnemonics2, mnemonicsAdapter));

        tvMnemonics3.setOnClickListener(v -> clearNullSelect(mapList, tvMnemonics3, mnemonicsAdapter));

        mnemonicsAdapter.setOnItemClickListener((adapter, view, position) -> {
            String mn1 = tvMnemonics1.getText().toString().trim();
            String mn2 = tvMnemonics2.getText().toString().trim();
            String mn3 = tvMnemonics3.getText().toString().trim();
            Map<String, Object> map = mapList.get(position);
            int flag = (int) map.get("flag");
            String content = (String) map.get("mn");
            if (flag == -1) {
                if (!TextUtils.isEmpty(mn1) && !TextUtils.isEmpty(mn2) && !TextUtils.isEmpty(mn3)) {
                    return;
                }
                if (TextUtils.isEmpty(mn1)) {
                    tvMnemonics1.setText(content);
                } else if (!TextUtils.isEmpty(mn1) && TextUtils.isEmpty(mn2) && TextUtils.isEmpty(mn3)) {
                    tvMnemonics2.setText(content);
                } else if (!TextUtils.isEmpty(mn1) && !TextUtils.isEmpty(mn2) && TextUtils.isEmpty(mn3)) {
                    tvMnemonics3.setText(content);
                } else if (!TextUtils.isEmpty(mn1) && TextUtils.isEmpty(mn2) && !TextUtils.isEmpty(mn3)) {
                    tvMnemonics2.setText(content);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    map.replace("flag", position);
                }
            } else {
                if (mn1.equals(content)) {
                    tvMnemonics1.setText("");
                } else if (mn2.equals(content)) {
                    tvMnemonics2.setText("");
                } else if (mn3.equals(content)) {
                    tvMnemonics3.setText("");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    map.replace("flag", -1);
                }
            }
            mnemonicsAdapter.notifyDataSetChanged();
        });
    }

    private void clearNullSelect(List<Map<String, Object>> mapList, TextView textView, VerifyMnemonicsAdapter mnemonicsAdapter) {
        String mnemonics = textView.getText().toString().trim();
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            String content = (String) map.get("mn");
            if (mnemonics.equals(content)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    map.replace("flag", -1);
                }
                textView.setText("");
                mnemonicsAdapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private void verifyConfirm() {
        String mn1 = tvMnemonics1.getText().toString().trim();
        String mn2 = tvMnemonics2.getText().toString().trim();
        String mn3 = tvMnemonics3.getText().toString().trim();

        if (TextUtils.isEmpty(mn1)) {
            showInfo(String.format(getString(R.string.enter_th_word), indexRandomFirst));
            return;
        }
        if (TextUtils.isEmpty(mn2)) {
            showInfo(String.format(getString(R.string.enter_th_word), indexRandomSecond));
            return;
        }

        if (TextUtils.isEmpty(mn3)) {
            showInfo(String.format(getString(R.string.enter_th_word), indexRandomThird));
            return;
        }

        String mnemonics = "";
        String mnemonics2 = "";
        String mnemonics3 = "";
        if (mnList.size() > 0) {
            for (int i = 0; i < mnList.size(); i++) {
                if (i == indexRandomFirst) {
                    mnemonics = mnList.get(i);
                }
                if (i == indexRandomSecond) {
                    mnemonics2 = mnList.get(i);
                }
                if (i == indexRandomThird) {
                    mnemonics3 = mnList.get(i);
                }
            }
            if (!mnemonics.equals(mn1)) {
                showInfo(String.format(getString(R.string.word_wrong), indexRandomFirst));
                return;
            }

            if (!mnemonics2.equals(mn2)) {
                showInfo(String.format(getString(R.string.word_wrong), indexRandomSecond));
                return;
            }

            if (!mnemonics3.equals(mn3)) {
                showInfo(String.format(getString(R.string.word_wrong), indexRandomThird));
                return;
            }

            EnterPayPwd.password(this, 2, new IPassword() {
                @Override
                public void onPassword(String pwd) {
                    if (mainChain.equals(Constant.SOL_CHAIN)) {
                        createSolWallet(pwd);
                    } else if (mainChain.equals(Constant.ETH_CHAIN) || mainChain.equals(Constant.BNB_CHAIN)){
                        createBinanceOrEthWallet(pwd);
                    } else if (mainChain.equals(Constant.BTC_CHAIN)) {
                        createBitcoinWallet(pwd);
                    }
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    private void createSolWallet(String password) {
        Account acc = Account.fromMnemonic(mnList, "");
        if (acc == null) {
            return;
        }
        String pubKey = acc.getPublicKey().toString();
        Log.i("duan==wallet", "钱包地址===   " + pubKey);
        String encodePvaKay = Base58.encode(acc.getSecretKey());//加密
        Log.i("duan==wallet", "私钥加密===   " + encodePvaKay);
        saveWalletInfo(encodePvaKay, pubKey, password);
    }

    private void createBinanceOrEthWallet(String password) {
        //2.生成种子
        byte[] seed = MnemonicUtils.generateSeed(MnemonicUtils.listToString(mnList), null);
        // 3. 生成根私钥 root private key 树顶点的master key
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        if (BuildConfig.DEBUG) {
            // 根私钥进行 priB58编码,得到测试网站上显示的数据
            NetworkParameters params = MainNetParams.get();
            String priv = rootPrivateKey.serializePrivB58(params);
            Log.i("TAG", "BIP32 Extended Private Key:" + priv);
        }
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/60H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        byte[] privateKeyByte = child.getPrivKeyBytes();
        //通过私钥生成公私钥对
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
//        Log.i("TAG", "generateBip44Wallet: 钥匙对  私钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
//        Log.i("TAG", "generateBip44Wallet: 钥匙对  公钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPublicKey()));

        WalletFile walletFile = null;
        try {
            walletFile = Wallet.createLight(password, keyPair);
        } catch (CipherException e) {
            e.printStackTrace();
        }
        String walletAddress = Keys.toChecksumAddress(walletFile.getAddress());
        String privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
        Log.i("TAG", "generateBip44Wallet: 地址 = " + walletAddress);
        Log.i("TAG", "generateBip44Wallet: 私钥 = " + walletAddress);
        saveWalletInfo(privateKey, walletAddress, password);
    }

    //创建比特币钱包
    private void createBitcoinWallet(String password) {
        MainNetParams params = MainNetParams.get();//正式网络
        String passphrase = "";
        Long creationtime = System.currentTimeMillis() / 1000L;
        DeterministicSeed deterministicSeed = new DeterministicSeed(mnList, null, passphrase, creationtime);
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(deterministicSeed.getSeedBytes());
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/0H/0H/0/0");
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));

//        ECKey ecKey = ECKey.fromPrivate(child.getPrivKey());
//        Address address = ecKey.toAddress(params);
//        String piv = child.getPrivateKeyAsWiF(params);
//        String puk = address.toBase58();
//        saveWalletInfo(piv, puk, password);
    }

    private void saveWalletInfo(String encodePvaKay, String pubKey, String password) {
        List<WalletBean> list = LitePal.findAll(WalletBean.class);
        String walletName = mainChain + "Wallet" + (list.size() + 1);
        WalletBean bean = new WalletBean();
        bean.setName(walletName);
        bean.setPassword(password);
        bean.setPubKey(pubKey);
        bean.setPvaKey(encodePvaKay);
        bean.setMnemonics(MnemonicUtils.listToString(mnList));
        bean.setMainChain(mainChain);
        if (list.size() == 0) {
            bean.setSelect(1);
        }
        bean.setSelect(0);
        bean.setWalletType(String.valueOf(mWalletType));
        bean.save();
//        CacheData.shared().updateWalletInfoFlag = 66;
        if (mWalletType.equals("0")) {
            CacheData.shared().applyFlag = 67;
        } else {
            CacheData.shared().saveFlag = 67;
        }
        showSuccessToast(getString(R.string.wallet_create_success));
        Log.i("duan==wallet", "本地数据===   " + bean);
        finish();

    }

}