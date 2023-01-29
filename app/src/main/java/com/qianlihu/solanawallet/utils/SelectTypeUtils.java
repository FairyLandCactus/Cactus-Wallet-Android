package com.qianlihu.solanawallet.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.PopupWindow;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.SelectTypeAdapter;

import java.util.List;

/**
 * @Author: duan
 * @Date: 2021/9/3
 * @Description: 下拉选择工具
 */
public class SelectTypeUtils {

    public static void selectTypePop(Context context, View view, View view2, int dr, int yoff, int popWidth, List<String> list, SelectTypeAdapter.OnItemClickListener itemClickListener) {
        PopupWindow typeSelectPopup;
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        SelectTypeAdapter adapter = new SelectTypeAdapter(list);
        recyclerView.setAdapter(adapter);

        typeSelectPopup = new PopupWindow(recyclerView, popWidth, ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景
        Drawable drawable = ContextCompat.getDrawable(context, dr);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(() -> {
            // 关闭popup窗口
            typeSelectPopup.dismiss();
            view2.setVisibility(View.INVISIBLE);
        });
        // 使用isShowing()检查popup窗口是否在显示状态
        if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
            typeSelectPopup.showAsDropDown(view, 0, yoff);
        }
        adapter.setOnItemClickListener((itemView, position) -> {
            itemClickListener.onItemClick(itemView, position);
            typeSelectPopup.dismiss();
            view2.setVisibility(View.INVISIBLE);
        });

        view2.setVisibility(View.VISIBLE);
    }

}
