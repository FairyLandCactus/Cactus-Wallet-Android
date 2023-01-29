package com.qianlihu.solanawallet.utils.wallet_utils.wallet.key;

import com.qianlihu.solanawallet.wallet.PublicKey;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/1/314:40
 * desc   :
 * version: 1.0.0
 */
public class MemoProgram extends Program {

    private static PublicKey PROGRAM_ID = new PublicKey("Memo1UhkJRfHyvLMcVucJwxXeuD728EqVDDwQDxFMNo");

    public static TransactionInstruction writeUtf8(PublicKey account, String memo) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(account, true, false));

        byte[] memoBytes = memo.getBytes(StandardCharsets.UTF_8);
        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                memoBytes
        );
    }

}
