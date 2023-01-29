package com.qianlihu.solanawallet.event;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/4/1210:44
 * desc   :
 * version: 1.0.0
 */
public class UsdtEvent {

    /**
     * arg : {"channel":"tickers","instId":"TRX-USDT"}
     * data : [{"instType":"SPOT","instId":"TRX-USDT","last":"0.05794","lastSz":"7096.534092","askPx":"0.05798","askSz":"117027.142229","bidPx":"0.05797","bidSz":"106011.268678","open24h":"0.06184","high24h":"0.06209","low24h":"0.05771","sodUtc0":"0.05823","sodUtc8":"0.05889","volCcy24h":"57263926.383114","vol24h":"958118508.704316","ts":"1649730866617"}]
     */

    private ArgBean arg;
    private List<DataBean> data;

    public ArgBean getArg() {
        return arg;
    }

    public void setArg(ArgBean arg) {
        this.arg = arg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ArgBean {
        /**
         * channel : tickers
         * instId : TRX-USDT
         */

        private String channel;
        private String instId;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getInstId() {
            return instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }
    }

    public static class DataBean {
        /**
         * instType : SPOT
         * instId : TRX-USDT
         * last : 0.05794
         * lastSz : 7096.534092
         * askPx : 0.05798
         * askSz : 117027.142229
         * bidPx : 0.05797
         * bidSz : 106011.268678
         * open24h : 0.06184
         * high24h : 0.06209
         * low24h : 0.05771
         * sodUtc0 : 0.05823
         * sodUtc8 : 0.05889
         * volCcy24h : 57263926.383114
         * vol24h : 958118508.704316
         * ts : 1649730866617
         */

        private String instType;
        private String instId;
        private String last = "0.00";
        private String lastSz = "0.00";
        private String askPx;
        private String askSz;
        private String bidPx;
        private String bidSz;
        private String open24h = "0.00";
        private String high24h;
        private String low24h;
        private String sodUtc0;
        private String sodUtc8;
        private String volCcy24h;
        private String vol24h;
        private String ts;

        public String getInstType() {
            return instType;
        }

        public void setInstType(String instType) {
            this.instType = instType;
        }

        public String getInstId() {
            return instId;
        }

        public void setInstId(String instId) {
            this.instId = instId;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getLastSz() {
            return lastSz;
        }

        public void setLastSz(String lastSz) {
            this.lastSz = lastSz;
        }

        public String getAskPx() {
            return askPx;
        }

        public void setAskPx(String askPx) {
            this.askPx = askPx;
        }

        public String getAskSz() {
            return askSz;
        }

        public void setAskSz(String askSz) {
            this.askSz = askSz;
        }

        public String getBidPx() {
            return bidPx;
        }

        public void setBidPx(String bidPx) {
            this.bidPx = bidPx;
        }

        public String getBidSz() {
            return bidSz;
        }

        public void setBidSz(String bidSz) {
            this.bidSz = bidSz;
        }

        public String getOpen24h() {
            return open24h;
        }

        public void setOpen24h(String open24h) {
            this.open24h = open24h;
        }

        public String getHigh24h() {
            return high24h;
        }

        public void setHigh24h(String high24h) {
            this.high24h = high24h;
        }

        public String getLow24h() {
            return low24h;
        }

        public void setLow24h(String low24h) {
            this.low24h = low24h;
        }

        public String getSodUtc0() {
            return sodUtc0;
        }

        public void setSodUtc0(String sodUtc0) {
            this.sodUtc0 = sodUtc0;
        }

        public String getSodUtc8() {
            return sodUtc8;
        }

        public void setSodUtc8(String sodUtc8) {
            this.sodUtc8 = sodUtc8;
        }

        public String getVolCcy24h() {
            return volCcy24h;
        }

        public void setVolCcy24h(String volCcy24h) {
            this.volCcy24h = volCcy24h;
        }

        public String getVol24h() {
            return vol24h;
        }

        public void setVol24h(String vol24h) {
            this.vol24h = vol24h;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }
    }
}
