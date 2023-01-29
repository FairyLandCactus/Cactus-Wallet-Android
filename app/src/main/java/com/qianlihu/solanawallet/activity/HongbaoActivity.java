package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.MeetingMoreAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.meeting.IconNameBean;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HongbaoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select_hongbao_type)
    TextView tvSelectHongbaoType;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.tv_join_member)
    TextView tvJoinMember;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_default_blessing)
    EditText etDefaultBlessing;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.btn_into_hongbao)
    TextView btnIntoHongbao;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    private String nubStr = "";
    private String amountStr = "";
    private String blessing = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hongbao;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.red_send));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }
        tvJoinMember.setText(String.format(getString(R.string.red_join_member), 0+""));
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                amountStr = s.toString();
                changeIntoBtnBg();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeIntoBtnBg();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etDefaultBlessing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeIntoBtnBg();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_select_hongbao_type, R.id.btn_into_hongbao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_select_hongbao_type:
                selectHongbaoTypeDialog();
                break;
            case R.id.btn_into_hongbao:
                break;
        }
    }

    private void changeIntoBtnBg() {
        nubStr = etNum.getText().toString();
        blessing = etDefaultBlessing.getText().toString();
        if (!amountStr.isEmpty()) {
            double amount = Double.valueOf(amountStr);
            if (amount > 0) {
                tvAmount.setText(amountStr);
                if (!nubStr.isEmpty()) {
                    int num = Integer.parseInt(nubStr);
                    if (num > 0 && !blessing.isEmpty()) {
                        btnIntoHongbao.setBackground(getDrawable(R.drawable.hongbao_btn_check_bord));
                        btnIntoHongbao.setTextColor(getColor(R.color.white));
                    } else {
                        btnIntoHongbao.setBackground(getDrawable(R.drawable.hongbao_btn_uncheck_bord));
                        btnIntoHongbao.setTextColor(getColor(R.color.text_F2F2F2));
                    }
                }
            } else if (amount == 0) {
                tvAmount.setText("0.00");
                btnIntoHongbao.setBackground(getDrawable(R.drawable.hongbao_btn_uncheck_bord));
                btnIntoHongbao.setTextColor(getColor(R.color.text_F2F2F2));
            }
        } else {
            tvAmount.setText("0.00");
            btnIntoHongbao.setBackground(getDrawable(R.drawable.hongbao_btn_uncheck_bord));
            btnIntoHongbao.setTextColor(getColor(R.color.text_F2F2F2));
        }
    }

    private void selectHongbaoTypeDialog() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_hongbao_type, null, false);
        TextView tvLuckHongbao = view.findViewById(R.id.tv_luck);
        TextView tvCommonHongbao = view.findViewById(R.id.tv_common);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> sheetDialog.dismiss());

       tvLuckHongbao.setOnClickListener(v -> {
           sheetDialog.dismiss();
           tvSelectHongbaoType.setText(getString(R.string.red_luck));
       });

        tvCommonHongbao.setOnClickListener(v -> {
            sheetDialog.dismiss();
            tvSelectHongbaoType.setText(getString(R.string.red_common));
        });

        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }
}