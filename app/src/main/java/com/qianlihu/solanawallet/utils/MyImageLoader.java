package com.qianlihu.solanawallet.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.qianlihu.solanawallet.R;
import com.youth.banner.loader.ImageLoader;

/**
 * author : Duan
 * date   : 2021/6/79:21
 * desc   :  轮播图加载器
 * version: 1.0.0
 */
public class MyImageLoader extends ImageLoader {

    private int corners;

    public MyImageLoader(int corners) {
        this.corners = corners;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context.getApplicationContext())
                .load(path)
                .fitCenter()
                .centerCrop()
                .error(R.drawable.start_bg)
                .fallback(R.drawable.start_bg)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(corners)))
                .into(imageView);
    }
}
