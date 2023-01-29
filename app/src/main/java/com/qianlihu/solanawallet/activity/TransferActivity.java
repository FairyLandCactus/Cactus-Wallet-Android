package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.alphawallet.token.tools.Numeric;
import com.blankj.utilcode.util.SPUtils;
import com.google.protobuf.StringValue;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.binance.BinanceWalletUtil;
import com.qianlihu.solanawallet.binance.Token;
import com.qianlihu.solanawallet.binance.Web3jUtils;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.ethereum.EthWalletUtil;
import com.qianlihu.solanawallet.event.ErrorEvent;
import com.qianlihu.solanawallet.interfere.IFingerprint;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.rpc.bean.TransactionBean;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.track.TrackUsdtTransferbean;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.FingerprintPwd;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.SolanaUtil;
import com.qianlihu.solanawallet.utils.WalletUtils;
import com.qianlihu.solanawallet.utils.wallet_utils.AssociatedAccountUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.view.GasSetView;
import com.qianlihu.solanawallet.wallet.PublicKey;
import com.qianlihu.solanawallet.wallet.Transfcation;
import com.qianlihu.solanawallet.wallet.TransferUtils;
import com.qianlihu.solanawallet.wc.TransactionData;
import com.qianlihu.solanawallet.wc.Wallet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

/**
 * @Author: duan
 * @Date: 2021/12/7
 * @Description: 转账
 */
public class TransferActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_gas_fee)
    TextView tvGasFee;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.et_memo)
    EditText etMemo;
    @BindView(R.id.gsv_fee)
    GasSetView gsvFee;

    private double mAmount = 0.0;
    private String mTokenAddress = "";
    private int decimals = 0;
    private String symbol = "";
    private String mWalletType = "0";
    private boolean isConnect = false;
    private String mMainChain = Constant.SOL_CHAIN;
    private String mMemo = "";
    private String signatures = "";
    private String myPuk = "";
    private String mPik = "";
    private String mPwd = "";
    private double usd = 0.0;
    private double mMainChainBalance = 0.0;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initView() {

        EventBus.getDefault().register(this);
        isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET);
        myPuk = getIntent().getStringExtra(Constant.SOL_PUK);
        WalletBean bean = WalletManager.getInstance().getWalletInfo(myPuk);
        if (bean != null) {
            mMainChain = bean.getMainChain();
            mWalletType = bean.getWalletType();
            mPik = bean.getPvaKey();
            mPwd = bean.getPassword();
        }
        AddTokenDB db = (AddTokenDB) getIntent().getSerializableExtra("addTokenDB");
        symbol = db.getSymbol();
        String address = getIntent().getStringExtra("address");
        mTokenAddress = db.getTokenAddress();
        decimals = db.getDecimals();
        mAmount = db.getAmount();
        if (!TextUtils.isEmpty(address)) {
            etAddress.setText(address);
        }
        tvTitle.setText(symbol + getString(R.string.transfer));
        tvAvailable.setText(getString(R.string.available) + MyUtils.decimalFormat6(mAmount) + symbol);
        etMoney.setHint("0.00" + symbol);

        List<AddTokenDB> listBanlace = LitePal.where("walletAddress = ? and symbol = ? and walletType = ?", myPuk, symbol, mWalletType).find(AddTokenDB.class);

        if (listBanlace.size() > 0) {
            mMainChainBalance = listBanlace.get(0).getAmount();
        }

        if (!mMainChain.equals(Constant.SOL_CHAIN)) {
            usd = WalletUtils.getUsd(mWalletType, myPuk, mMainChain);
            gsvFee.setUsdChain(usd, mMainChain);
            tvGasFee.setVisibility(View.GONE);
        } else {
            gsvFee.setVisibility(View.GONE);
            double fee = 0.000005;
            String feeStr = "";
            if (mMainChain.equals(Constant.SOL_CHAIN)) {
                feeStr = "≈" + MyUtils.decimalFormat6(fee) + "sol";
                tvGasFee.setText(feeStr);
            }
        }
    }

    @Override
    protected void initData() {
        new Thread(() -> TrackUsdtTransferbean.transferUsdt()).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(CacheData.shared().address)) {
            etAddress.setText(CacheData.shared().address);
            CacheData.shared().address = "";
        }
    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_book_address, R.id.iv_scan_address, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_book_address:
                Intent intent = new Intent(this, AddressManagerActivity.class);
                intent.putExtra("isSelectAddress", true);
                startActivityForResult(intent, 10044);
                break;
            case R.id.iv_scan_address:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                Intent intent1 = new Intent(this, QRActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.btn_confirm:
                transferSol();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra(KEY_RESULT);
            if (requestCode == 1) {
                //获取到扫描二维码的数据
                etAddress.setText(result);
            } else if (requestCode == 10044) {
                etAddress.setText(result);
            }
        }
    }

    private String transferResult = "";

    private void transferSol() {
        if (mWalletType.equals("1") && !isConnect) {
            showInfo(getString(R.string.is_connect));
            return;
        }

        String toPuk = etAddress.getText().toString().trim();
        String amount = etMoney.getText().toString().trim();
        String memo = etMemo.getText().toString();
        if (TextUtils.isEmpty(toPuk)) {
            showInfo(getString(R.string.fill_transfer_address));
            return;
        }

        if (TextUtils.isEmpty(amount)) {
            showInfo(getString(R.string.fill_amount));
            return;
        }

        if (!isBanlance(Double.valueOf(amount))) {
            showInfo(getString(R.string.running_low));
            return;
        }
        FingerprintPwd.fingerprintVerify(this, new IFingerprint() {
            @Override
            public void onVerify(boolean isVerify) {
                if (isVerify) {
                    transferVerify(memo, toPuk, amount);
                } else {
                    EnterPayPwd.password(TransferActivity.this, 1, new IPassword() {
                        @Override
                        public void onPassword(String pwd) {
                            if (!pwd.equals(mPwd)) {
                                showInfo(getString(R.string.pwd_error));
                                return;
                            }
                            transferVerify(memo, toPuk, amount);
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

    private boolean isBanlance(double amount) {
        double feeAmount = 0;
        if (amount > mAmount) {
            return false;
        }
        if (mMainChain.equals(Constant.SOL_CHAIN)) {
            feeAmount = mMainChainBalance - 0.000005;
        } else {
            double fee = gsvFee.gasFee;
            feeAmount = mMainChainBalance - fee;
        }
        if (feeAmount <= 0) {
            return false;
        }
        return true;
    }

    private void transferVerify(String memo, String toPuk, String amount) {
        showLoadingInfos(getString(R.string.transfer), getString(R.string.do_not_leave), true);
        new Thread(() -> {
            mMemo = memo;
            if (mMainChain.equals(Constant.SOL_CHAIN)) {//solana转账
                try {
                    if (symbol.equals("SOL")) {
                        transferResult = SolanaUtil.transferSol(myPuk, toPuk, amount, mPik, memo);
                    } else {
                        transferResult = transferTokenSol(myPuk, toPuk, amount, mPik);
                    }

                    Log.i("duan==wallet", "转账返回值=== " + transferResult);
                    runOnUiThread(() -> {
                        if (!TextUtils.isEmpty(transferResult)) {
                            hideLoadingInfos();
                            updateBalance(Double.valueOf(amount));
                        }
                    });
                } catch (RpcException e) {
                    e.printStackTrace();
                }
            } else if (mMainChain.equals(Constant.BNB_CHAIN) || mMainChain.equals(Constant.ETH_CHAIN)) {//bnb转账
                BnbOrEthTransfer(toPuk, myPuk, mTokenAddress, amount, mPik);
            }
        }).start();
    }

    //solana代币转账
    private String transferTokenSol(String myPuk, String toPuk, String amount, String pik) {
        String result = "";
        boolean isCreatAccount = true;
        String PROGRAM_ID = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA";
        String ASSOCIATED_TOKEN_PROGRAM_ID = "ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL";
        byte[] walletByte = Base58.decode(toPuk);
        byte[] mintByte = Base58.decode(mTokenAddress);
        byte[] token = Base58.decode(PROGRAM_ID);
        List<byte[]> byteList = new ArrayList<>();
        byteList.add(walletByte);
        byteList.add(token);
        byteList.add(mintByte);
        try {
            PublicKey.ProgramDerivedAddress pk = PublicKey.findProgramAddress(byteList, new PublicKey(ASSOCIATED_TOKEN_PROGRAM_ID));
            AccountInfo info = SolanaUtil.getAccountInfo(pk.getAddress().toString(), Constant.SOL_ENCODING_BASE64);

            if (info != null) {
                if (info.getValue() != null && info.getValue().getOwner().equals(PROGRAM_ID)) {
                    isCreatAccount = false;
                }
            }

            String mySplTokenAddress = AssociatedAccountUtil.createAssociatedAccountAddress(myPuk, mTokenAddress);
            String associatedTokenAddress = AssociatedAccountUtil.createAssociatedAccountAddress(toPuk, mTokenAddress);
//            result = SolanaTransferUtil.transferSplToken2(mySplTokenAddress, associatedTokenAddress, mTokenAddress, myPuk, 9, amount, pik);
            result = SolanaUtil.sendSPLTokens(myPuk, toPuk, mTokenAddress, myPuk, mySplTokenAddress, associatedTokenAddress, decimals, amount, pik, isCreatAccount);
        } catch (UnsupportedEncodingException | RpcException e) {
            runOnUiThread(() -> {
                hideLoadingInfos();
                showInfo(e.getMessage());
            });
            e.printStackTrace();
        } catch (Exception e) {
            runOnUiThread(() -> {
                hideLoadingInfos();
                showInfo(e.getMessage());
            });
            e.printStackTrace();
        }
        return result;
    }

    //加载bnb合约
    private void BnbOrEthTransfer(String toPuk, String myPuk, String contractAddress, String amount, String pik) {
        BigDecimal amountDd = new BigDecimal(amount);
        BigDecimal amountBig = amountDd.multiply(BigDecimal.valueOf(Math.pow(10, decimals)));
        byte[] data = TransferUtils.createTokenTransferData(toPuk, amountBig.toBigInteger());
        String recipient = contractAddress;
        BigInteger value = new BigInteger("0");

        String gasLimit = String.valueOf(gsvFee.getGasLimits());
        String gasPrice = String.valueOf(gsvFee.getGasPrices());
        long chainId = 1;
        if (mMainChain.equals(Constant.BNB_CHAIN)) {
            chainId = 56;
        }

        if (symbol.equals(Constant.BNB_CHAIN) || symbol.equals(Constant.ETH_CHAIN)) {
            value = amountBig.toBigInteger();
            recipient = toPuk;
            data = new byte[0];
        }

        String dataHex = Numeric.toHexString(data);
        byte[] dataToByte = !TextUtils.isEmpty(dataHex) ? Numeric.hexStringToByteArray(dataHex) : new byte[0];
        Transfcation tf = new Transfcation(mPik,
                mMainChain,
                new Wallet(myPuk),
                recipient,
                value,
                new BigInteger(gasPrice),
                new BigInteger(gasLimit),
                -1,
                dataToByte,
                chainId);

        TransferUtils.createTransactionWithSig(tf).subscribe(new SingleObserver<TransactionData>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(TransactionData transactionData) {
                String hash = transactionData.txHash;
                Log.i("duan==Sign", "onSuccess=== " + hash);
                runOnUiThread(() -> {
                    hideLoadingInfos();
                    CacheData.shared().updateWalletInfoFlag = 66;
                    updateBalance(Double.valueOf(amount));
                });
            }

            @Override
            public void onError(Throwable e) {
                runOnUiThread(() -> {
                    Log.i("duan==Sign", "onError=== " + e.getMessage());
                    dialogTip(e.getMessage());
                    hideLoadingInfos();
                });
            }
        });
    }

    private void isTransferSuccessful(TransactionReceipt receipt, String myPuk, String toAddr, double amount) {
        hideLoadingInfos();
        String status = receipt.getStatus();
        if (!status.equals("0x0")) {
            CacheData.shared().updateWalletInfoFlag = 66;
            updateBalance(amount);
        } else {
            showerrortoast(getString(R.string.transfer_fail));
        }
    }

    private BigInteger bigInt;

    private BigInteger getGasPrice() {
        bigInt = new BigInteger("10000000000");
//        return new BigInteger("10000000000");
        BinanceWalletUtil.getGasPrice().subscribe(new SingleObserver<BigInteger>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(BigInteger bigInteger) {
                bigInt = bigInteger;
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        return bigInt;
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(300000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ExceptionErrorInfo(ErrorEvent event) {
        String connectFail = "failed to connect to";
        if (event.getMessage().contains(connectFail)) {
            showInfo(getString(R.string.connect_fail));
        } else {
            showInfo(event.getMessage());
        }
        hideLoadingInfos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    private void updateBalance(double amount) {
        List<AddTokenDB> dbs = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, mTokenAddress).find(AddTokenDB.class);
        if (dbs.size() > 0) {
            AddTokenDB tokenDB = new AddTokenDB();
            double amountdb = dbs.get(0).getAmount();
            double balance = 0;
            if (amountdb > amount) {
                balance = amountdb - amount;
            } else {
                balance = amount - amountdb;
            }
            tokenDB.setAmount(balance);
            tokenDB.updateAll("walletAddress = ? and tokenAddress = ?", myPuk, mTokenAddress);
        }
        List<AddTokenDB> dbs2 = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, mTokenAddress).find(AddTokenDB.class);
        if (dbs2.size() > 0) {
            CacheData.shared().addTokenDB = dbs2.get(0);
        }
        CacheData.shared().isTransfer = true;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        Toasty.success(TransferActivity.this, getString(R.string.transfer_succeeded)).show();
        finish();
    }
}