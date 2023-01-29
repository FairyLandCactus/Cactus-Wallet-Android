package com.qianlihu.solanawallet.adapter;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.qianlihu.solanawallet.base.BaseFragment;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/213:45
 * desc   :
 * version: 1.0.0
 */
public class BaseTabAdapter extends FragmentStatePagerAdapter {
    private int position;

    private List<BaseFragment> list;

    public BaseTabAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    public BaseFragment getCurrentFragment() {
        return list.get(position);
    }

    public int getCurrentPosition() {
        return position;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.position = position;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
