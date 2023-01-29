package com.qianlihu.solanawallet.activity;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.fragment.AirdropChildFagment;
import com.qianlihu.solanawallet.utils.DialogView;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClaimAirdropActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_airdrop)
    NoScrollViewPager vpAirdrop;

    private String[] mTabTitleArray = {"Ongoing", "Ended"};
    private List<String> mDataList = new ArrayList<>();

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_claim_airdrop;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.claim_airdrop));
        riskWarning();
    }

    @Override
    protected void initData() {
        mDataList.add(getString(R.string.ongoing));
        mDataList.add(getString(R.string.ended));
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        int tabColor = R.color.black;
        if (isDark) {
            tabColor = R.color.white;
        }
        MagicTitleUtils.magicTitleView(this, mDataList, magicIndicator, vpAirdrop, R.color.txt_666, tabColor, tabColor, 18f,false);
        initFragment();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private void initFragment() {
        List<BaseFragment> list = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            list.add((BaseFragment) AirdropChildFagment.newInstance(1,1));
        }
        BaseTabAdapter tabAdapter = new BaseTabAdapter(getSupportFragmentManager(), list);
        vpAirdrop.setAdapter(tabAdapter);
        vpAirdrop.setOffscreenPageLimit(mDataList.size());
    }

    private void riskWarning() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_risk_arning, null);
        TextView tvGotIt = view.findViewById(R.id.tv_got_it);
        Dialog dialog = new DialogView(this, view);

        tvGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}