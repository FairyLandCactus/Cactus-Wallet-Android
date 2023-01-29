package com.qianlihu.solanawallet.fragment.dapp;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.QRActivity;
import com.qianlihu.solanawallet.activity.SearchActivity;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jnr.ffi.annotations.In;

import static android.app.Activity.RESULT_OK;
import static com.king.zxing.CaptureActivity.KEY_RESULT;

/**
 * author : Duan
 * date   : 2021/11/118:15
 * desc   :
 * version: 1.0.0
 */
public class DappFragment extends BaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_dapp)
    NoScrollViewPager vpDapp;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_scan_search)
    ImageView ivScanSearch;

    private String[] mTabTitleArray = {"My", "Ranking", "SOL", "IUS"};
    private List<String> mDataList = new ArrayList<>();

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    public static DappFragment newInstance() {
        DappFragment fragment = new DappFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp;
    }

    @Override
    public void initView() {
        mDataList.add(getString(R.string.my));
        mDataList.add(getString(R.string.tool));
        mDataList.add(getString(R.string.IRS));
        mDataList.add(getString(R.string.sol));
        mDataList.add(getString(R.string.bsc));
        mDataList.add(getString(R.string.eth));
//        mDataList.add(getString(R.string.defii));
//        mDataList.add(getString(R.string.net));
//        mDataList.add(getString(R.string.tron));
//        mDataList.add(getString(R.string.iost));
        MagicTitleUtils.magicTitleView(getActivity(), mDataList, magicIndicator, vpDapp, R.color.txt_666, R.color.txt_6288FF, R.color.transparent, 18f, false);
        initFragment();
    }

    @Override
    public void initData() {
//        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                String content = v.getText().toString();
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    LoadWebPageUtils.activityIntent(getContext(), "", content);
//                }
//                return false;
//            }
//        });
    }

    private void initFragment() {
        List<BaseFragment> list = new ArrayList<>();
        list.add((BaseFragment) DappMyFagment.newInstance(1, 1));
        list.add((BaseFragment) DappToolFagment.newInstance(1, 1));
        list.add((BaseFragment) DappNewFagment.newInstance(1, 1));
        list.add((BaseFragment) DappSolFagment.newInstance(1, 1));
        list.add((BaseFragment) DappBscFagment.newInstance(1, 1));
        list.add((BaseFragment) DappEthFagment.newInstance(1, 1));

        BaseTabAdapter tabAdapter = new BaseTabAdapter(getChildFragmentManager(), list);
        vpDapp.setAdapter(tabAdapter);
        vpDapp.setOffscreenPageLimit(mDataList.size());

    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};
    @SuppressWarnings("deprecation")
    @OnClick({R.id.et_search, R.id.iv_scan_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;

            case R.id.iv_scan_search:
                Intent intent = new Intent();
                if (lacksPermission(writePermissions)) {
                    requestPermissions(writePermissions, 0);
                    return;
                }
                intent.setClass(getActivity(), QRActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("content", result);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                getActivity().finish();
            }
        }
    }
}
