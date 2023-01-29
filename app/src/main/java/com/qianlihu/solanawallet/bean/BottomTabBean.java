package com.qianlihu.solanawallet.bean;

import com.qianlihu.solanawallet.base.BaseFragment;

/**
 * author : Duan
 * date   : 2022/4/710:21
 * desc   :
 * version: 1.0.0
 */
public class BottomTabBean {

    private String title;
    private Integer icon;
    private Integer unIcon;
    private BaseFragment fragment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getUnIcon() {
        return unIcon;
    }

    public void setUnIcon(Integer unIcon) {
        this.unIcon = unIcon;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }
}
