package com.qianlihu.solanawallet.binance;

import android.text.TextUtils;

import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author : Duan
 * date   : 2022/2/1815:54
 * desc   :
 * version: 1.0.0
 */
public class BinanceWalletUtil {

    //生成转账凭据
    public static Credentials loadCredentials(String pik) {
        Credentials c = null;
        BigInteger pk = Numeric.toBigIntNoPrefix(pik);
        byte[] privateKeyByte = pk.toByteArray();
        ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
        try {
            WalletFile walletFile = Wallet.createLight("", keyPair);
            c = Credentials.create(Wallet.decrypt("", walletFile));
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return c;
    }

    //获取余额
    public static Single<BigInteger> getBalanceForETH(String address) {
        if (TextUtils.isEmpty(address)) {
            return Single.error(new NullPointerException("转账地址为空！"));
        }

        String addr = address.startsWith("0x") ? address : Keys.toChecksumAddress(address);
        Web3j web3j = Web3jUtils.initWeb3j();
        return Single.fromCallable(() -> web3j
                .ethGetBalance(addr, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<EthBlock.Block> getBlock() {
        return Single.fromCallable(() -> Web3jUtils.initWeb3j()
                .ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true)
                .send()
                .getBlock())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //获取当前区块节点
    public static Single<BigInteger> getBlockNumber() {
        return Single.fromCallable(() -> Web3jUtils.initWeb3j()
                .ethBlockNumber()
                .send()
                .getBlockNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<BigInteger> getGasPrice() {
        return Single.fromCallable(() -> Web3jUtils.initWeb3j()
                .ethGasPrice()
                .send()
                .getGasPrice())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //加载合约
    public static Single<Token> loadContract(String pik, String addr, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = loadCredentials(pik);
        Web3j web3j = Web3jUtils.initWeb3j();
        return Single.fromCallable(() -> Token.load(addr, web3j, credentials, gasPrice, gasLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    //通过合约获取代币余额
    public static Single<BigInteger> getBinanceTokenBalance(Token token, String address) {
        return Single.fromCallable(() -> token.balanceOf(Keys.toChecksumAddress(address))
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getName(Token token) {
        return Single.fromCallable(() -> token.name()
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<String> getSymbol(Token token) {
        return Single.fromCallable(() -> token.symbol()
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Type> getDecimals(Token token) {
        return Single.fromCallable(() -> token.decimals()
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
