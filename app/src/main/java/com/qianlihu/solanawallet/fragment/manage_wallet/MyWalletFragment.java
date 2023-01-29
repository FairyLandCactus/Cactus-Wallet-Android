package com.qianlihu.solanawallet.fragment.manage_wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.MyWalletActivity;
import com.qianlihu.solanawallet.adapter.AddressManagerAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddressManagerDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * author : Duan
 * date   : 2022/4/1515:25
 * desc   : 我的钱包
 * version: 1.0.0
 */
public class MyWalletFragment extends BaseFragment {

    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    private boolean isSelectAddress = false;

    public static Fragment newInstance(boolean isSelectAddress) {
        MyWalletFragment fragment = new MyWalletFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSelectAddress" , isSelectAddress);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_wallet;
    }

    @Override
    public void initView() {
        isSelectAddress = getArguments().getBoolean("isSelectAddress");
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        myWallet();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10033) {
                myWallet();
            }
        }
    }

    private void myWallet() {
        List<WalletBean> list = LitePal.findAll(WalletBean.class);
        if (list.size() > 0) {
            List<AddressManagerDB> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                AddressManagerDB db = new AddressManagerDB();
                db.setName(list.get(i).getName());
                db.setAddress(list.get(i).getPubKey());
                list1.add(db);
            }
            AddressManagerAdapter managerAdapter = new AddressManagerAdapter(list1);
            rvAddress.setAdapter(managerAdapter);
            managerAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent = new Intent();
                if (isSelectAddress) {
                    CacheData.shared().address = list.get(position).getPubKey();
                    getActivity().finish();
                } else {
                    intent.putExtra("position", position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wallet", list.get(position));
                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), MyWalletActivity.class);
                    startActivityForResult(intent, 10033);
                }

            });
        } else {
            rvAddress.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet))));
        }
    }
}
