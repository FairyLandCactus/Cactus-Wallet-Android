package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.BuildConfig;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.utils.wallet_utils.MnemonicUtils;
import com.qianlihu.solanawallet.wallet.Account;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.DumpedPrivateKey;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class ImportExistingWalletActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_tips)
    ImageView ivTips;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.et_private_key)
    EditText etPrivateKey;
    @BindView(R.id.btn_confirm_import)
    Button btnConfirmImport;

    private String mPrivateKey = "";
    private String mWalletType = "0";
    private String mainChain = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_existing_wallet;
    }

    @Override
    protected void initView() {
        tvTitle.setVisibility(View.GONE);
        ivScan.setVisibility(View.VISIBLE);
        ivTips.setVisibility(View.VISIBLE);
        mWalletType = getIntent().getStringExtra("walletType");
        mainChain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
    }

    @Override
    protected void initData() {
        mPrivateKey = etPrivateKey.getText().toString();
        if (!TextUtils.isEmpty(mPrivateKey)) {
            btnConfirmImport.setBackground(getResources().getDrawable(R.drawable.btn_blue_1_bord));
        }
        etPrivateKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrivateKey = etPrivateKey.getText().toString();
                if (TextUtils.isEmpty(mPrivateKey)) {
                    btnConfirmImport.setBackground(getResources().getDrawable(R.drawable.btn_blue_1_bord));
                } else {
                    btnConfirmImport.setBackground(getResources().getDrawable(R.drawable.btn_blue_1_bord));
                }
            }
        });

    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_tips, R.id.iv_scan, R.id.btn_confirm_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_tips:
                importPrivateKeyTip();
                break;
            case R.id.iv_scan:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                Intent intent = new Intent(this, QRActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_confirm_import:
                confirmImport();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                //获取到扫描二维码的数据
                etPrivateKey.setText(result);
            }
        }
    }

    private void importPrivateKeyTip() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_import_wallet_tip, null);
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        TextView tvClose = view.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }

    private String walletAddress = "";
    private String privateKey = "";
    private void confirmImport() {
        String privateKeyOfMn = etPrivateKey.getText().toString();
        if (TextUtils.isEmpty(privateKeyOfMn)) {
            showInfo(getString(R.string.enter_mnemonic_or_private_key));
            return;
        }
        String wordStore = privateKeyOfMn;
        String[] word = wordStore.split("\\s+");
        List<String> wordsList = Arrays.asList(word);

        if (mainChain.equals(Constant.SOL_CHAIN)) {
            importSolWallet(privateKeyOfMn, wordsList);
        } else if (mainChain.equals(Constant.ETH_CHAIN) || mainChain.equals(Constant.BNB_CHAIN)){
            try {
                importBinanseOrEthWallet(privateKeyOfMn, wordsList);
            } catch (CipherException e) {
                e.printStackTrace();
            }
        } else if (mainChain.equals(Constant.BTC_CHAIN)) {
            importBitcoinWallet(privateKeyOfMn, wordsList);
        }

    }

    private void importSolWallet(String privateKeyOfMn, List<String> wordsList) {
        if (wordsList.size() >= 12) {
            Account acc = Account.fromMnemonic(wordsList, "");
            if (acc == null) {
                showInfo(getString(R.string.incorrect_input));
                return;
            }
//            Log.i("duan==wallet", "钱包地址===   " + acc.getPublicKey());
            walletAddress = acc.getPublicKey().toString();
            String encode = Base58.encode(acc.getSecretKey());//加密
            String decode = MyUtils.bytesToHex(Base58.decode(encode));//解密
            privateKey = encode;
//            Log.i("duan==wallet", "私钥加密===   " + encode.length());
//            Log.i("duan==wallet", "私钥解密===   " + decode.length());
        } else {
            String privateKy = privateKeyOfMn.substring(0, 2);
            if (privateKy.equals("0x")) {
                privateKeyOfMn = privateKeyOfMn.substring(2);
            }
            if (privateKeyOfMn.length() == 128 && MyUtils.isNumeric(privateKeyOfMn)) {
                String encode = Base58.encode(MyUtils.hexToByteArray(privateKeyOfMn));//加密
                byte[] decode = Base58.decode(encode);
                byte[] ir = Arrays.copyOfRange(decode, 32, 64);
                String puk = Base58.encode(ir);
                privateKey = encode;
                walletAddress = puk;
            }else {
                if (privateKeyOfMn.length() < 40) {
                    showInfo(getString(R.string.incorrect_input));
                    return;
                }

                if (privateKeyOfMn.contains("0") || privateKeyOfMn.contains("l") ||privateKeyOfMn.contains("O") || privateKeyOfMn.contains("I")) {
                    showInfo(getString(R.string.incorrect_input));
                    return;
                }

                byte[] decode = Base58.decode(privateKeyOfMn);
                byte[] ir = Arrays.copyOfRange(decode, 32, 64);
//                Log.i("duan==wallet", "解密后的密钥===   " + MyUtils.bytesToHex(decode).length());
//                Log.i("duan==wallet", "解密后的公钥===   " + MyUtils.bytesToHex(ir));
                privateKey = privateKeyOfMn;
                walletAddress = Base58.encode(ir);
            }
        }

        List<WalletBean> walletBeanList = LitePal.where("pubKey = ?", walletAddress).find(WalletBean.class);
        if (walletBeanList.size() > 0) {
            showInfo(getString(R.string.wallet_already_exists));
            return;
        }
        EnterPayPwd.password(this, 3, new IPassword() {
            @Override
            public void onPassword(String pwd) {
                saveWalletInfo(pwd, wordsList);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void importBinanseOrEthWallet(String privateKeyOfMn, List<String> wordsList) throws CipherException {
        if (wordsList.size() >= 12) {
            //2.生成种子
            byte[] seed = MnemonicUtils.generateSeed(privateKeyOfMn, null);
            // 3. 生成根私钥 root private key 树顶点的master key
            DeterministicKey child = getDeterministicKeyBip44BySeed(seed);
            byte[] privateKeyByte = child.getPrivKeyBytes();
            //通过私钥生成公私钥对
            ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
            WalletFile walletFile = Wallet.createLight("", keyPair);
            walletAddress = Keys.toChecksumAddress(walletFile.getAddress());
            privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());
        } else {
            if (privateKeyOfMn.startsWith("0x")) {
                privateKeyOfMn = privateKeyOfMn.substring(2);
            }

            if (!MyUtils.isNumeric(privateKeyOfMn)) {
                showInfo(getString(R.string.incorrect_input));
                return;
            }

            if (privateKeyOfMn.length() < 60) {
                showInfo(getString(R.string.incorrect_input));
                return;
            }
            // 3. 生成根私钥 root private key 树顶点的master key
            BigInteger pk = Numeric.toBigIntNoPrefix(privateKeyOfMn);
            byte[] privateKeyByte = pk.toByteArray();

            //通过私钥生成公私钥对
            ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
            WalletFile walletFile = Wallet.createLight("", keyPair);
            walletAddress = Keys.toChecksumAddress(walletFile.getAddress());
            privateKey = Numeric.toHexStringNoPrefix(keyPair.getPrivateKey());

        }
//        Log.i("TAG", "generateBip44Wallet: 地址 = " + walletAddress);
        List<WalletBean> walletBeanList = LitePal.where("pubKey = ?", walletAddress).find(WalletBean.class);
        if (walletBeanList.size() > 0) {
            showInfo(getString(R.string.wallet_already_exists));
            return;
        }
        EnterPayPwd.password(this, 3, new IPassword() {
            @Override
            public void onPassword(String pwd) {
                saveWalletInfo(pwd, wordsList);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void importBitcoinWallet(String privateKeyOfMn, List<String> wordsList) {
//        if (wordsList.size() >= 12) {
//            MainNetParams params = MainNetParams.get();//正式网络
//            String passphrase = "";
//            Long creationtime = System.currentTimeMillis() / 1000L;
//            DeterministicSeed deterministicSeed = new DeterministicSeed(wordsList, null, passphrase, creationtime);
//            DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(deterministicSeed.getSeedBytes());
//            DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
//            List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/0H/0H/0/0");
//            DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
//            ECKey ecKey = ECKey.fromPrivate(child.getPrivKey());
//            Address address = ecKey.toAddress(params);
//            privateKey = child.getPrivateKeyAsWiF(params);
//            walletAddress = address.toBase58();
//        } else {
//            MainNetParams mainNetParams = MainNetParams.get();
//            ECKey key = DumpedPrivateKey.fromBase58(MainNetParams.get(), privateKeyOfMn).getKey();
//            Address address = key.toAddress(mainNetParams);
//            walletAddress = address.toBase58();
//            privateKey = key.getPrivateKeyAsWiF(mainNetParams);
//        }
    }

    private void saveWalletInfo(String pwd, List<String> wordsList) {
        showSuccessToast(getString(R.string.inprot_wallet_success));
        List<WalletBean> walletBeanList = LitePal.findAll(WalletBean.class);
        String walletName = mainChain+"Wallet" + (walletBeanList.size() + 1);
        WalletBean bean = new WalletBean();
        bean.setName(walletName);
        bean.setPassword(pwd);
        bean.setPubKey(walletAddress);
        bean.setPvaKey(privateKey);
        bean.setMainChain(mainChain);
        bean.setWalletType(String.valueOf(mWalletType));
        if (wordsList.size() >= 12) {
            bean.setMnemonics(MnemonicUtils.listToString(wordsList));
        }
        if (walletBeanList.size() == 0) {
            bean.setSelect(1);
        }
        bean.setSelect(0);
        bean.save();
        if (mWalletType.equals("0")) {
            CacheData.shared().applyFlag = 67;
        } else {
            CacheData.shared().saveFlag = 67;
        }
//        Log.i("duan==wallet", "本地数据===   " + bean);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                finish();
            }
        }
    }

    private static DeterministicKey getDeterministicKeyBip44BySeed(byte[] seed) {
        // 3. 生成根私钥 root private key 树顶点的master key
        DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
        if (BuildConfig.DEBUG) {
            // 根私钥进行 priB58编码,得到测试网站上显示的数据
            NetworkParameters params = MainNetParams.get();
            String priv = rootPrivateKey.serializePrivB58(params);
//            Log.i("TAG", "BIP32 Extended Private Key:" + priv);
        }
        // 4. 由根私钥生成 第一个HD 钱包
        DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
        // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/60H/0H/0");
        // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
        DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
        return child;
    }
}