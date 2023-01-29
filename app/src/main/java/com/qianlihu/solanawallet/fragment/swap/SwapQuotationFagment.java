package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SwapCurveAdapter;
import com.qianlihu.solanawallet.adapter.SwappExchangeTokenAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.UsdtEvent;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.TokenMintUtil;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;
import com.qianlihu.solanawallet.ws.IWebsocketCallback;
import com.qianlihu.solanawallet.ws.JWebSocketClient;
import com.qianlihu.solanawallet.ws.WebsocketManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class SwapQuotationFagment extends BaseFragment {

    @BindView(R.id.rv_curve)
    RecyclerView rvCurve;
    @BindView(R.id.rv_statistical)
    RecyclerView rvStatistical;
    @BindView(R.id.tv_sell_balance)
    TextView tvSellBalance;
    @BindView(R.id.iv_sell_pic)
    CircleImageView ivSellPic;
    @BindView(R.id.tv_sell_name)
    TextView tvSellName;
    @BindView(R.id.tv_buy_balance)
    TextView tvBuyBalance;
    @BindView(R.id.iv_buy_pic)
    CircleImageView ivBuyPic;
    @BindView(R.id.tv_buy_name)
    TextView tvBuyName;
    @BindView(R.id.tv_sell_conversion)
    TextView tvSellConversion;
    @BindView(R.id.tv_buy_conversion)
    TextView tvBuyConversion;
    @BindView(R.id.ll_sell)
    LinearLayout llSell;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.tv_entrusted_name)
    TextView tvEntrustedName;
    @BindView(R.id.tv_quantity_name)
    TextView tvQuantityName;
    @BindView(R.id.tv_obtain_name)
    TextView tvObtainName;
    @BindView(R.id.tv_liquidity_pool_num1)
    TextView tvLiquidityPoolNum1;
    @BindView(R.id.tv_liquidity_pool_num2)
    TextView tvLiquidityPoolNum2;
    @BindView(R.id.ll_pool)
    LinearLayout llPool;
    @BindView(R.id.tv_token_address_1)
    TextView tvTokenAddress1;
    @BindView(R.id.tv_token_address_2)
    TextView tvTokenAddress2;
    @BindView(R.id.tv_obtain_conversion)
    TextView tvObtainConversion;

    private List<Tokens> mTokensList;
    private String sellLogo = "";
    private String buyLogo = "";
    private String sellName = "";
    private String buyName = "";
    private int sellLogoInt = R.mipmap.icon_doubt;
    private int buyLogoInt = R.mipmap.icon_doubt;

    private List<UsdtEvent> eventList = new ArrayList<>();
    private SwapCurveAdapter curveAdapter;
    private WebsocketManager wsManager = null;
//    private JWebSocketClient client;

    public static Fragment newInstance(int id, int status) {
        SwapQuotationFagment fragment = new SwapQuotationFagment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
//        EventBus.getDefault().register(this);
        return R.layout.fragment_swap_quotation;
    }

    @Override
    public void initView() {
        wsManager = new WebsocketManager();
        rvCurve.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvCurve.addItemDecoration(new GridSpaceItemDecoration(3, 0, 40));

        if (eventList.size() > 0) {
            eventList.clear();
        }

        UsdtEvent event = new UsdtEvent();
        UsdtEvent.ArgBean bean = new UsdtEvent.ArgBean();
        List<UsdtEvent.DataBean> list = new ArrayList<>();
        bean.setInstId("SOL-USDT");
        event.setArg(bean);
        event.setData(list);
        eventList.add(event);

        UsdtEvent event1 = new UsdtEvent();
        UsdtEvent.ArgBean bean1 = new UsdtEvent.ArgBean();
        List<UsdtEvent.DataBean> list1 = new ArrayList<>();
        bean1.setInstId("TRX-USDT");
        event1.setArg(bean1);
        event1.setData(list1);
        eventList.add(event1);

        UsdtEvent event2 = new UsdtEvent();
        UsdtEvent.ArgBean bean2 = new UsdtEvent.ArgBean();
        List<UsdtEvent.DataBean> list2 = new ArrayList<>();
        bean2.setInstId("BNB-USDT");
        event2.setArg(bean2);
        event2.setData(list2);
        eventList.add(event2);

        curveAdapter = new SwapCurveAdapter(eventList);
        rvCurve.setAdapter(curveAdapter);

        rvStatistical.setLayoutManager(new LinearLayoutManager(getContext()));
//        List<String> list1 = new ArrayList<>();
//        list1.add("HOT");
//        list1.add("Polygon ECO");
//        list1.add(getString(R.string.GameFi));
//        rvStatistical.setAdapter(new SwapStatisticAdapter(list1));
        rvStatistical.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));

    }

    @Override
    public void initData() {
        new Thread(() -> mTokensList = LitePal.findAll(Tokens.class)).start();

//        new Thread(() -> websocketClient()).start();
//        websocketClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wsManager != null) {
            wsManager.close();
            wsManager = null;
        }
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (wsManager != null) {
            if (isVisibleToUser) {
                CacheData.shared().isSwapQShow = true;
                new Thread(() -> websocketClient()).start();
            } else {
                new Thread(() -> wsManager.close()).start();
                CacheData.shared().isSwapQShow = false;
            }
        }

    }

    @OnClick({R.id.tv_collect, R.id.tv_increase, R.id.tv_decline, R.id.tv_new_currency, R.id.tv_all, R.id.ll_sell, R.id.ll_buy, R.id.rl_exchange, R.id.tv_token_address_1, R.id.tv_token_address_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_collect:
                break;
            case R.id.tv_increase:
                break;
            case R.id.tv_decline:
                break;
            case R.id.tv_new_currency:
                break;
            case R.id.tv_all:
                break;
            case R.id.ll_sell:
                selectToken(0);
                break;
            case R.id.ll_buy:
                selectToken(1);
                break;
            case R.id.rl_exchange:
                upDownExchange();
                break;
            case R.id.tv_token_address_1:
                String address1 = tvTokenAddress1.getText().toString();
                ClickCopyUtils.copy(getContext(), address1);
                break;
            case R.id.tv_token_address_2:
                String address2 = tvTokenAddress2.getText().toString();
                ClickCopyUtils.copy(getContext(), address2);
                break;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 119) {
//                String message = msg.getData().getString("data");
                String message = msg.obj.toString();
                Gson gson = new Gson();
                UsdtEvent event = gson.fromJson(message, UsdtEvent.class);
//                EventBus.getDefault().post(event);
                if (event != null) {
                    if (event.getArg() != null) {
                        String newName = event.getArg().getInstId();
                        if (event.getData() != null) {
                            if (event.getData().size() > 0) {
                                for (int i = 0; i < eventList.size(); i++) {
                                    String oldName = eventList.get(i).getArg().getInstId();
                                    if (oldName.equals(newName)) {
                                        eventList.set(i, event);
                                        if (curveAdapter != null) {
                                            curveAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            return true;
        }
    });

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 119) {
//                String message = msg.getData().getString("data");
//                Gson gson = new Gson();
//                UsdtEvent event = gson.fromJson(message, UsdtEvent.class);
////                EventBus.getDefault().post(event);
//                if (event != null) {
//                    if (event.getArg() != null) {
//                        String newName = event.getArg().getInstId();
//                        if (event.getData() != null) {
//                            if (event.getData().size() > 0) {
//                                for (int i = 0; i < eventList.size(); i++) {
//                                    String oldName = eventList.get(i).getArg().getInstId();
//                                    if (oldName.equals(newName)) {
//                                        eventList.set(i, event);
//                                        if (curveAdapter != null) {
//                                            curveAdapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        }
//    };

    private void websocketClient() {
        wsManager.connect(Constant.URL_TICKER_WS, new IWebsocketCallback() {
            @Override
            public void onMessage(String message) {
                if (!TextUtils.isEmpty(message)) {
                    Message msg =  Message.obtain();
                    msg.what = 119;
                    msg.obj = message;
                    handler.sendMessage(msg);
                }
            }
            @Override
            public void onOpen() {
            }

            @Override
            public void onError(Exception ex) {

            }
        });

//        URI uri = URI.create(Constant.URL_TICKER_WS);
//        client = new JWebSocketClient(uri) {
//            @Override
//            public void onMessage(String message) {
//                //message就是接收到的消息
//                Log.e("duan==swap", message);
//                if (!TextUtils.isEmpty(message)) {
//                    Message msg =  Message.obtain();
//                    msg.what = 119;
//                    msg.obj = message;
//                    handler.sendMessage(msg);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Gson gson = new Gson();
//                            UsdtEvent event = gson.fromJson(message, UsdtEvent.class);
//                            if (event != null) {
//                                if (event.getArg() != null) {
//                                    String newName = event.getArg().getInstId();
//                                    if (event.getData() != null) {
//                                        if (event.getData().size() > 0) {
//                                            for (int i = 0; i < eventList.size(); i++) {
//                                                String oldName = eventList.get(i).getArg().getInstId();
//                                                if (oldName.equals(newName)) {
//                                                    eventList.set(i, event);
//                                                    if (curveAdapter != null) {
//                                                        curveAdapter.notifyDataSetChanged();
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        };
//        if (client.isClosed()) {
//            return;
//        }
//        client.connect();
    }

    private void upDownExchange() {
        String sellName = tvSellName.getText().toString();
        String sellBalance = tvSellBalance.getText().toString();
        String buyName = tvBuyName.getText().toString();
        String buyBalance = tvBuyBalance.getText().toString();
        String sellConversion = tvSellConversion.getText().toString();
        String buyConversion = tvBuyConversion.getText().toString();

        tvSellName.setText(buyName);
        tvSellBalance.setText(buyBalance);
        tvSellConversion.setText(buyConversion);
        tvBuyName.setText(sellName);
        tvBuyBalance.setText(sellBalance);
        tvBuyConversion.setText(sellConversion);

        Glide.with(getActivity()).load(buyLogo).error(buyLogoInt).into(ivSellPic);
        Glide.with(getActivity()).load(sellLogo).error(sellLogoInt).into(ivBuyPic);
        String buyLogoUrl = buyLogo;
        int buLogoIntError = buyLogoInt;
        buyLogo = sellLogo;
        sellLogo = buyLogoUrl;
        buyLogoInt = sellLogoInt;
        sellLogoInt = buLogoIntError;

    }

    private SwappExchangeTokenAdapter tokenAdapter;

    private void selectToken(int flag) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_swap_select_token, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        EditText etSearch = view.findViewById(R.id.et_search);
        RecyclerView rvToken = view.findViewById(R.id.rv_token);
        ivClose.setOnClickListener(v -> dialog.dismiss());

        tokenAdapter = new SwappExchangeTokenAdapter(mTokensList);
        rvToken.setLayoutManager(new LinearLayoutManager(getContext()));
        rvToken.setAdapter(tokenAdapter);
        selectClick(mTokensList, flag, dialog);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
//                Log.i("duan==search", content);
                if (TextUtils.isEmpty(content)) {
                    if (mTokensList.size() > 0) {
                        tokenAdapter = new SwappExchangeTokenAdapter(mTokensList);
                        rvToken.setAdapter(tokenAdapter);
                        selectClick(mTokensList, flag, dialog);
                    }
                    return;
                }
                List<Tokens> list = TokenMintUtil.searchTokens(content, mTokensList);
                if (list.size() > 0) {
                    tokenAdapter = new SwappExchangeTokenAdapter(list);
                    rvToken.setAdapter(tokenAdapter);
                    tokenAdapter.notifyDataSetChanged();
                    selectClick(list, flag, dialog);
                } else {
                    rvToken.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_token))));
                }
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    private void selectClick(List<Tokens> list, int flag, BottomSheetDialog dialog) {
        tokenAdapter.setOnItemClickListener((adapter, view, position) -> {
            String logoUrl = list.get(position).getLogoURI();
            String name = list.get(position).getSymbol();
            String address = list.get(position).getAddress();
            if (flag == 0) {
                Glide.with(getActivity()).load(logoUrl).error(R.mipmap.icon_doubt).into(ivSellPic);
                tvSellName.setText(name);
                sellLogo = logoUrl;
                tvSellConversion.setVisibility(View.VISIBLE);
                tvSellConversion.setText("1 " + name + "≈$1.00120000");
                tvLiquidityPoolNum1.setText(name);
                tvEntrustedName.setText(name);
                tvQuantityName.setText(name);
                tvTokenAddress1.setText(address);
            } else {
                Glide.with(getActivity()).load(logoUrl).error(R.mipmap.icon_doubt).into(ivBuyPic);
                tvBuyName.setText(name);
                buyLogo = logoUrl;
                tvBuyConversion.setVisibility(View.VISIBLE);
                tvBuyConversion.setText("1 " + name + "≈$1.00120000");
                tvLiquidityPoolNum2.setText(name);
                tvObtainName.setText(name);
                tvTokenAddress2.setText(address);

            }
            if (!TextUtils.isEmpty(sellLogo) && !TextUtils.isEmpty(buyLogo)) {
                String buyName = tvBuyName.getText().toString();
                String sellName = tvSellName.getText().toString();
                llPool.setVisibility(View.VISIBLE);
                tvObtainConversion.setText("1 " + sellName + "≈0.9615517724 " + buyName);
            }
            dialog.dismiss();
        });
    }
}
