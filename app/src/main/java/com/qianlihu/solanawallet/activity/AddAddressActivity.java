package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.ChainAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddressManagerDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import org.litepal.LitePal;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rv_chain)
    RecyclerView rvChain;

    private String mainChain = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.add_address));
        selectChain();
    }

    @Override
    protected void initData() {
//        edOnListener();
    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_scan_address, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_scan_address:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                Intent intent = new Intent(this, QRActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_save:
                saveAddress();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                if (result.length() < 32) {
                    showInfo(getString(R.string.invalid_address));
                    return;
                }
                //获取到扫描二维码的数据
                etAddress.setText(result);
            }
        }
    }

    private void selectChain() {
        rvChain.setLayoutManager(new GridLayoutManager(this, 4));
        rvChain.addItemDecoration(new GridSpaceItemDecoration(4, 30, 40));
        List<String> listChain = Constant.LIST_CHAIN;
        ChainAdapter chainAdapter = new ChainAdapter(listChain);
        rvChain.setAdapter(chainAdapter);
        chainAdapter.setOnItemClickListener((adapter, view, position) -> {
            chainAdapter.select(position);
            mainChain = listChain.get(position);
        });
    }

    private void saveAddress() {

        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(mainChain)) {
            showInfo(getString(R.string.select_main_chain));
            return;
        }

        if (TextUtils.isEmpty(name)) {
            showInfo(getString(R.string.hint_name));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            showInfo(getString(R.string.hint_address));
            return;
        }
        if (etAddress.length() < 32) {
            showInfo(getString(R.string.invalid_address));
            return;
        }
        List<AddressManagerDB> dbList = LitePal.where("address = ?", address).find(AddressManagerDB.class);
        if (dbList.size() > 0) {
            showInfo(getString(R.string.address_alrealy_exists));
            return;
        }
        AddressManagerDB db = new AddressManagerDB();
        db.setName(name);
        db.setAddress(address);
        db.setMainChain(mainChain);
        db.save();
        showSuccessToast(getString(R.string.added_successfully));
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void edOnListener() {
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    btnSave.setBackground(getResources().getDrawable(R.drawable.address_save_select_bord));
                } else {
                    btnSave.setBackground(getResources().getDrawable(R.drawable.address_save_normal_bord));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                finish();
            }
        }
    }
}