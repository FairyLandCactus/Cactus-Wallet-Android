package com.qianlihu.solanawallet.utils.wallet_utils.wallet;

import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.Transaction;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.TransactionInstruction;
import com.qianlihu.solanawallet.wallet.Account;

import java.util.List;

/**
 * Builder for constructing {@link Transaction} objects to be used in sendTransaction.
 */
public class TransactionBuilder {

    private final Transaction transaction;

    public TransactionBuilder() {
        transaction = new Transaction();
    }

    public TransactionBuilder addInstruction(TransactionInstruction transactionInstruction) {
        transaction.addInstruction(transactionInstruction);
        return this;
    }

    public TransactionBuilder setRecentBlockHash(String recentBlockHash) {
        transaction.setRecentBlockHash(recentBlockHash);
        return this;
    }

    public TransactionBuilder setSigners(List<Account> signers) {
        transaction.sign((Account) signers);
        return this;
    }

    public Transaction build() {
        return transaction;
    }

}
