package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddressManagerDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DialogUtils;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class EditAddressActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_clear_name)
    ImageView ivClearName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.iv_clear_address)
    ImageView ivClearAddress;
    @BindView(R.id.tv_sol)
    TextView tvSol;
    @BindView(R.id.tv_bnb)
    TextView tvBnb;

    private String mAddress = "";
    private String mName = "";
    private String mainChain = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.edit_address));
        editOnListener();
    }

    @Override
    protected void initData() {
        mAddress = getIntent().getStringExtra("address");
        mName = getIntent().getStringExtra("name");
        mainChain = getIntent().getStringExtra("chain");
        if (mainChain.equals(Constant.SOL_CHAIN)) {
            selectChain(1);
        } else {
            selectChain(2);
        }
        etName.setText(mName);
        etAddress.setText(mAddress);
    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_clear_name, R.id.iv_clear_address, R.id.iv_scan_address, R.id.tv_sol, R.id.tv_bnb, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_name:
                etName.setText("");
                break;
            case R.id.iv_clear_address:
                etAddress.setText("");
                break;
            case R.id.iv_scan_address:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                Intent intent = new Intent(this, QRActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_sol:
                selectChain(1);
                break;
            case R.id.tv_bnb:
                selectChain(2);
                break;
            case R.id.btn_delete:
                deleteAddress();
                break;
            case R.id.btn_save:
                saveUpdateAddress();
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

    private void selectChain(int flag) {
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (flag == 1) {
            mainChain = Constant.SOL_CHAIN;
            if (isDark) {
                tvSol.setTextColor(getColor(R.color.txt_3757FF));
                tvBnb.setTextColor(getColor(R.color.white));
                tvSol.setBackground(getDrawable(R.drawable.select_main_chain_bg_bord_dark));
                tvBnb.setBackground(getDrawable(R.drawable.unselect_main_chain_bg_bord_dark));
            } else {
                tvSol.setTextColor(getColor(R.color.txt_6880FF));
                tvBnb.setTextColor(getColor(R.color.txt_C1C1C1));
                tvSol.setBackground(getDrawable(R.drawable.select_main_chain_bg_bord_light));
                tvBnb.setBackground(getDrawable(R.drawable.unselect_main_chain_bg_bord_light));
            }
        } else {
            mainChain = Constant.BNB_CHAIN;
            if (isDark) {
                tvBnb.setTextColor(getColor(R.color.txt_3757FF));
                tvSol.setTextColor(getColor(R.color.white));
                tvBnb.setBackground(getDrawable(R.drawable.select_main_chain_bg_bord_dark));
                tvSol.setBackground(getDrawable(R.drawable.unselect_main_chain_bg_bord_dark));
            } else {
                tvBnb.setTextColor(getColor(R.color.txt_6880FF));
                tvSol.setTextColor(getColor(R.color.txt_C1C1C1));
                tvBnb.setBackground(getDrawable(R.drawable.select_main_chain_bg_bord_light));
                tvSol.setBackground(getDrawable(R.drawable.unselect_main_chain_bg_bord_light));
            }
        }

    }

    private void saveUpdateAddress() {
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showInfo(getString(R.string.hint_name));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            showInfo(getString(R.string.hint_address));
            return;
        }
        AddressManagerDB db = new AddressManagerDB();
        db.setName(name);
        db.setAddress(address);
        db.setMainChain(mainChain);
        db.saveOrUpdate("address = ?", mAddress);
        showSuccessToast(getString(R.string.modified_successfully));
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void deleteAddress() {
        DialogUtils.changeTip(this, getString(R.string.tip_delete_address), (dialog, which) -> {
            LitePal.deleteAll(AddressManagerDB.class, "name = ? and address = ?", mName, mAddress);
            showSuccessToast(getString(R.string.delete_succeeded));
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }, null);
    }

    private void editOnListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ivClearName.setVisibility(View.GONE);
                } else {
                    ivClearName.setVisibility(View.VISIBLE);
                }
            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    ivClearAddress.setVisibility(View.GONE);
                } else {
                    ivClearAddress.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}