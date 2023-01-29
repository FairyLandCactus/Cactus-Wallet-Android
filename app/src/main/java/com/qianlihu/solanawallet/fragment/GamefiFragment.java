package com.qianlihu.solanawallet.fragment;

import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AboutIUSActivity;
import com.qianlihu.solanawallet.activity.AddressManagerActivity;
import com.qianlihu.solanawallet.activity.BindUserActivity;
import com.qianlihu.solanawallet.activity.ClaimAirdropActivity;
import com.qianlihu.solanawallet.activity.CommunityActivity;
import com.qianlihu.solanawallet.activity.HelpCenterActivity;
import com.qianlihu.solanawallet.activity.InviteActivity;
import com.qianlihu.solanawallet.activity.SetUpActivity;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.BoundBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.NodeUpdateEvent;
import com.qianlihu.solanawallet.event.ResumeEvent;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.wallet_bean.NodeWalletStatusBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.xuexiang.xutil.XUtil.getContentResolver;

/**
 * author : Duan
 * date   : 2021/11/118:15
 * desc   :
 * version: 1.0.0
 */
public class GamefiFragment extends BaseFragment {

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_node_address)
    TextView tvNodeAddress;
    @BindView(R.id.tv_bind)
    TextView tvBind;
    @BindView(R.id.tv_bind_wallet)
    TextView tvBindWallet;
    @BindView(R.id.rl_node)
    RelativeLayout rlNode;

    public static GamefiFragment newInstance() {
        GamefiFragment fragment = new GamefiFragment();
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        EventBus.getDefault().register(this);
        return R.layout.fragment_gamefi;
    }

    @Override
    public void initView() {
        tvVersion.setText("V" + AppUtils.getAppVersionName());
        List<WalletBean> list = LitePal.where("walletType = ?", "0").find(WalletBean.class);
        if (list.size() > 0) {
            tvNodeAddress.setText(list.get(0).getPubKey());
        }
    }

    @Override
    public void initData() {
//        inviteBoundIsShow();
        getNode();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resumeEvent(ResumeEvent event) {
        Log.i("duan==main", "我的onResume");
//        inviteBoundIsShow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nodeUpdateEvent(NodeUpdateEvent event) {
        getNode();
    }

    private void inviteBoundIsShow() {
        boolean isInviteBound = SPUtils.getInstance().getBoolean(Constant.IS_BOUND_INVITE);
        boolean isWalletBound = SPUtils.getInstance().getBoolean(Constant.IS_BOUND_WALLET);
        if (isWalletBound) {
            tvBindWallet.setVisibility(View.GONE);
            if (isInviteBound) {
                tvBind.setVisibility(View.GONE);
            } else {
                tvBind.setVisibility(View.VISIBLE);
                new Thread(() -> getInviteBoundInfo()).start();
            }
        } else {
            tvBind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getContext());
    }

    @OnClick({R.id.tv_manage_wallet, R.id.tv_blind_box, R.id.rl_commuity,R.id.tv_airdrop, R.id.tv_invite, R.id.tv_help_center, R.id.tv_telegram, R.id.tv_follow_twitter, R.id.ll_about_ius, R.id.tv_set_up, R.id.tv_bind, R.id.tv_bind_wallet})
    public void onViewClicked(View view) {
//        showInfo(getString(R.string.not_yet_open));
        switch (view.getId()) {
            case R.id.tv_manage_wallet:
                startActivity(new Intent(getContext(), AddressManagerActivity.class));
//                showInfo(getString(R.string.not_yet_open));
                break;
            case R.id.tv_blind_box:
                showInfo(getString(R.string.not_yet_open));
                break;
            case R.id.tv_airdrop:
                startActivity(new Intent(getContext(), ClaimAirdropActivity.class));
                break;
            case R.id.rl_commuity:
                startActivity(new Intent(getContext(), CommunityActivity.class));
                break;
            case R.id.tv_invite:
                startActivity(new Intent(getContext(), InviteActivity.class));
                break;
            case R.id.tv_help_center:
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
                break;
            case R.id.tv_telegram:
                LoadWebPageUtils.activityIntent2(getContext(), getString(R.string.telegram), Constant.WEB_URL_TELEGRAM);
                break;
            case R.id.tv_follow_twitter:
                LoadWebPageUtils.activityIntent2(getContext(), getString(R.string.follow_on_twitter), Constant.WEB_URL_TWITTER);
                break;
            case R.id.ll_about_ius:
                startActivity(new Intent(getContext(), AboutIUSActivity.class));
                break;
            case R.id.tv_set_up:
                CacheData.shared().languageType = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
                startActivity(new Intent(getContext(), SetUpActivity.class));
                break;
            case R.id.tv_bind:
                Intent intent = new Intent(getContext(), BindUserActivity.class);
                intent.putExtra("bindType", 1);
                startActivity(intent);
                break;
            case R.id.tv_bind_wallet:
                intent = new Intent(getContext(), BindUserActivity.class);
                intent.putExtra("bindType", 0);
                startActivity(intent);
                break;
        }
    }

    private void getInviteBoundInfo() {
        String androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("deviceUniqueId", androidId);
        String url = "http://ioyapi.wivyswap.com/api/user/verifyBindingInvite";
        HttpService.post(url, map, true, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("duan==bound", "请求失败===   " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==bound", "请求成功===   " + body);
                Gson gson = new Gson();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(body)) {
                            if (!body.startsWith("{") && !body.endsWith("}")) {
//                                showInfo(getString(R.string.request_abnormal));
                                return;
                            }
                            BoundBean bean = gson.fromJson(body, BoundBean.class);
                            if (bean != null) {
                                if (bean.getCode() == 200) {
                                    if (bean.isFlag()) {
                                        SPUtils.getInstance().put(Constant.IS_BOUND_INVITE, true);
                                        tvBind.setVisibility(View.GONE);
                                    }
                                } else {
//                                    showInfo(bean.getMsg());
                                }
                            } else {
//                                showInfo(getString(R.string.request_abnormal));
                            }
                        } else {
//                            showInfo(getString(R.string.request_abnormal));
                        }
                    }
                });
            }
        });
    }

    private void getNode() {
        List<WalletBean> list = LitePal.where("walletType = ? and select = ?", "1", "1").find(WalletBean.class);
        if (list.size() > 0) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("address", list.get(0).getPubKey());
            HttpService.doGet(Constant.WALLET_NODE, map, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = response.body().string();
//                    Log.i("duan==respon", "oooooooooooo   " + body);
                    getActivity().runOnUiThread(() -> {
                        if (!TextUtils.isEmpty(body)) {
                            Gson gson = new Gson();
                            NodeWalletStatusBean bean = gson.fromJson(body, NodeWalletStatusBean.class);
                            if (bean.getCode() == 1) {
                                if (bean.getData() == 1) {
                                    rlNode.setVisibility(View.VISIBLE);
                                } else {
                                    rlNode.setVisibility(View.GONE);
                                }
                            } else {
//                                showInfo(bean.getMsg());
                            }
                        } else {
//                            showInfo(getString(R.string.data_exception));
                        }
                    });
                }
            });
        }
    }
}
