package com.qianlihu.solanawallet.rpc;

public enum Cluster {
    TESTNET("https://api.testnet.solana.com"),
    DEVNET("https://api.devnet.solana.com"),
//    MAINNET("https://solana-api.projectserum.com"
    MAINNET("https://api.mainnet-beta.solana.com"
    );
    private String endpoint;

    Cluster(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
