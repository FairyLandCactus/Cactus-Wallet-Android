package com.qianlihu.solanawallet.fragment.manage_wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AddAddressActivity;
import com.qianlihu.solanawallet.activity.EditAddressActivity;
import com.qianlihu.solanawallet.adapter.AddressManagerAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddressManagerDB;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.DialogUtils;

import org.litepal.LitePal;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * author : Duan
 * date   : 2022/4/1515:24
 * desc   : 地址薄
 * version: 1.0.0
 */
public class AddressBookFragment extends BaseFragment {

    @BindView(R.id.rv_address)
    RecyclerView rvAddress;

    private boolean isSelectAddress = false;

    public static Fragment newInstance(boolean isSelectAddress) {
        AddressBookFragment fragment = new AddressBookFragment();
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
        return R.layout.fragment_address_book;
    }

    @Override
    public void initView() {
        isSelectAddress = getArguments().getBoolean("isSelectAddress");
    }

    @Override
    public void initData() {
        addressList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @OnClick(R.id.ll_add_new_address)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), AddAddressActivity.class);
        startActivityForResult(intent, 10022);
    }

    private void addressList() {
        rvAddress.setLayoutManager(new LinearLayoutManager(getContext()));
        List<AddressManagerDB> list = LitePal.findAll(AddressManagerDB.class);
        if (list.size() > 0) {
            AddressManagerAdapter managerAdapter = new AddressManagerAdapter(list);
            rvAddress.setAdapter(managerAdapter);
            managerAdapter.setOnItemClickListener((adapter, view, position) -> {
                if (isSelectAddress) {
                    CacheData.shared().address = list.get(position).getAddress();
                    getActivity().finish();
                } else {
                    addressManager(list, position, managerAdapter);
                }
            });
        } else {
            rvAddress.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_address_added_yet))));
        }
    }

    private void addressManager(List<AddressManagerDB> list, int position, AddressManagerAdapter managerAdapter) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog_manage_wallet, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        TextView tvEditAddress = view.findViewById(R.id.tv_edit_address);
        TextView tvCopyAddress = view.findViewById(R.id.tv_copy);
        TextView tvDeleteAddress = view.findViewById(R.id.tv_delete);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);

        tvEditAddress.setOnClickListener(v -> {
            dialog.dismiss();
//            Intent intent = new Intent(getActivity(), EditAddressActivity.class);
            Intent intent = new Intent(getActivity(), EditAddressActivity.class);
            intent.putExtra("address", list.get(position).getAddress());
            intent.putExtra("name", list.get(position).getName());
            intent.putExtra("chain", list.get(position).getMainChain());
//            startActivity(intent);
            startActivityForResult(intent, 10011);
        });

        tvCopyAddress.setOnClickListener(v -> {
            dialog.dismiss();
            ClickCopyUtils.copy(getContext(), list.get(position).getAddress());
        });

        tvDeleteAddress.setOnClickListener(v -> {
            dialog.dismiss();
            String address = list.get(position).getAddress();
            String name = list.get(position).getName();
            DialogUtils.changeTip(getActivity(), getString(R.string.tip_delete_address), (dialog1, which) -> {
                LitePal.deleteAll(AddressManagerDB.class, "name = ? and address = ?", name, address);
                showSuccessToast(getString(R.string.delete_succeeded));
                list.remove(position);
                managerAdapter.notifyDataSetChanged();
            }, null);
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10011) {
                addressList();
            } else if (requestCode == 10022){
                addressList();
            }
        }
    }
}
