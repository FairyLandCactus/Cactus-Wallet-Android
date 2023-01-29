package com.qianlihu.solanawallet.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

/**
 * author : Duan
 * date   : 2022/12/614:55
 * desc   :
 * version: 1.0.0
 */
public class LocationUtil {

    @SuppressLint("MissingPermission")
    public static void getCurrentLocation(Context context, LocationCallBack locationCallBack) {
        if (locationCallBack == null) {
            return;
        }
        if (context == null) {
            locationCallBack.onFail("");
        }
        //获取LocationManager对象
        LocationManager locationM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //实例化MyLocationListener
        MyLocationListener locationListener = new MyLocationListener(locationM, locationCallBack);
        //配置Criteria耗电低
        Criteria cri = new Criteria();
        cri.setPowerRequirement(Criteria.POWER_LOW);
        // 获取可用的provider,第二个参数标识 provider是否可用.
        String bestProvider = locationM.getBestProvider(cri, true);

        if (!TextUtils.isEmpty(bestProvider)) {
//            LogUtil.e("bestProvider = " + bestProvider + "可用");
            locationM.requestLocationUpdates(bestProvider, 0, 0, locationListener);
        } else if (locationM.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            LogUtil.e(LocationManager.NETWORK_PROVIDER + "可用");
            locationM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else if (locationM.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            LogUtil.e(LocationManager.GPS_PROVIDER + "可用");
            locationM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            //定位不可用，提示打开GPS
//            LogUtil.e("定位不可用，提示打开GPS");
            locationCallBack.onFail("无可用的定位方式，请打开GPS");
        }
    }

    /**
     * LocationListener 的实现类
     */
    private static class MyLocationListener implements LocationListener {
        private LocationManager mLocationManager;
        private LocationCallBack mLocationCallBack;

        public MyLocationListener(LocationManager locationManager, LocationCallBack locationCallBack) {
            this.mLocationManager = locationManager;
            this.mLocationCallBack = locationCallBack;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                if (mLocationCallBack != null) {
                    mLocationCallBack.onSuccess(location);
                }
                if (mLocationManager != null) {
                    mLocationManager.removeUpdates(this);
                }
            } else {
                if (mLocationCallBack != null) {
                    mLocationCallBack.onFail("location == null");
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 定位结果回调
     */
    public interface LocationCallBack {
        /**
         * 定位成功
         *
         * @param location
         */
        void onSuccess(Location location);

        /**
         * 定位失败
         *
         * @param msg
         */
        void onFail(String msg);
    }

}
