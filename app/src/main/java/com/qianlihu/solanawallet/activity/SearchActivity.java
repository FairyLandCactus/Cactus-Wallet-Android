package com.qianlihu.solanawallet.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.ChainAdapter;
import com.qianlihu.solanawallet.adapter.SearchHistoryAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.SearchHistoryDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.rv_chain)
    RecyclerView rvChain;

    private SearchHistoryAdapter historyAdapter;
    private List<SearchHistoryDB> dbList = new ArrayList<>();
    private int mChainFlag = 0;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        dbList = LitePal.order("dateTime desc").find(SearchHistoryDB.class);
        if (dbList.size() > 0) {
            historyAdapter = new SearchHistoryAdapter(dbList);
            rvHistory.setAdapter(historyAdapter);
            historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mChainFlag == 0) {
                        showInfo(getString(R.string.select_main_chain));
                        return;
                    }
                    LoadWebPageUtils.activityIntent(SearchActivity.this, mChainFlag, "", dbList.get(position).getContent());
                    finish();
                }
            });
        }
        String content = getIntent().getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            etSearch.setText(content);
        }
        selectChain();
    }

    @Override
    protected void initData() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String content = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    List<SearchHistoryDB> dbs = LitePal.where("content = ?", content).find(SearchHistoryDB.class);
                    if (dbs.size() == 0) {
                        long timecurrentTimeMillis = System.currentTimeMillis();
                        SearchHistoryDB historyDB = new SearchHistoryDB();
                        historyDB.setContent(content);
                        historyDB.setDateTime(timecurrentTimeMillis);
                        historyDB.save();
                    }
                    if (mChainFlag == 0) {
                        showInfo(getString(R.string.select_main_chain));
                    } else {
                        LoadWebPageUtils.activityIntent(SearchActivity.this, mChainFlag, "", content);
                        finish();
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                String content = etSearch.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showInfo(getString(R.string.enter_dapp_web_address));
                    return;
                }
                List<SearchHistoryDB> dbs = LitePal.where("content = ?", content).find(SearchHistoryDB.class);
                if (dbs.size() == 0) {
                    long timecurrentTimeMillis = System.currentTimeMillis();
                    SearchHistoryDB historyDB = new SearchHistoryDB();
                    historyDB.setContent(content);
                    historyDB.setDateTime(timecurrentTimeMillis);
                    historyDB.save();
                }
                if (mChainFlag == 0) {
                    showInfo(getString(R.string.select_main_chain));
                    return;
                }
                LoadWebPageUtils.activityIntent(this, mChainFlag, "", content);
                finish();
                break;
            case R.id.iv_delete:
                LitePal.deleteAll(SearchHistoryDB.class);
                if (dbList != null) {
                    dbList.removeAll(dbList);
                }
                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
                }
                break;
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
            mChainFlag = position + 1;
        });
    }
}