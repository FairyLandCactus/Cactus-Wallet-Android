package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.king.zxing.CaptureHelper;
import com.king.zxing.Intents;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;
import com.king.zxing.camera.CameraManager;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.utils.FileUtils;
import com.qianlihu.solanawallet.utils.PictureSelectedUtil;
import com.qianlihu.solanawallet.utils.QRCodeParseUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class QRActivity extends BaseActivity implements OnCaptureCallback {

    public static final String KEY_RESULT = Intents.Scan.RESULT;

    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private View ivTorch;

    private CaptureHelper mCaptureHelper;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_q_r;
    }

    @Override
    protected void initView() {
        initUI();
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化
     */
    public void initUI() {
        surfaceView = findViewById(R.id.surfaceView);
        viewfinderView = findViewById(R.id.viewfinderView);
        ivTorch = findViewById(R.id.ivTorch);
        ivTorch.setVisibility(View.GONE);
        mCaptureHelper = new CaptureHelper(this, surfaceView, viewfinderView, ivTorch);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.onCreate();
    }

    /**
     * Get {@link CaptureHelper}
     *
     * @return {@link #mCaptureHelper}
     */
    public CaptureHelper getCaptureHelper() {
        return mCaptureHelper;
    }

    /**
     * Get {@link CameraManager} use {@link #getCaptureHelper()#getCameraManager()}
     *
     * @return {@link #mCaptureHelper#getCameraManager()}
     */
    @Deprecated
    public CameraManager getCameraManager() {
        return mCaptureHelper.getCameraManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 接收扫码结果回调
     *
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    @Override
    public boolean onResultCallback(String result) {
        return false;
    }

    private String[] writePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @OnClick({R.id.iv_back, R.id.tv_album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_album:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                selectPic();
                break;
        }
    }

    private void selectPic() {
        PictureSelectedUtil.getInstance().picSelector(this, PictureSelectedUtil.HOME_MODE, true, new PictureSelectedUtil.IPathCallback() {
            @Override
            public void onResult(String path) {
                if (path.contains("content://")) {
                    Uri uri = Uri.parse(path);
                    path = FileUtils.getFilePathByUri_BELOWAPI11(uri, QRActivity.this);
                }
                Log.i("duan==path", "图片路径==  " + path);
                parsePhoto(path);
            }

            @Override
            public void OnCancel() {

            }
        });
    }

    /**
     * 启动线程解析二维码图片
     *
     * @param path
     */
    private void parsePhoto(String path) {
        //启动线程完成图片扫码
        new QrCodeAsyncTask(this, path).execute(path);
    }

    /**
     * AsyncTask 静态内部类，防止内存泄漏
     */
    static class QrCodeAsyncTask extends AsyncTask<String, Integer, String> {
        private WeakReference<Activity> mWeakReference;
        private String path;

        public QrCodeAsyncTask(Activity activity, String path) {
            mWeakReference = new WeakReference<>(activity);
            this.path = path;
        }

        @Override
        protected String doInBackground(String... strings) {
            // 解析二维码/条码
            return QRCodeParseUtils.syncDecodeQRCode(path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //识别出图片二维码/条码，内容为s
            QRActivity activity = (QRActivity) mWeakReference.get();
            if (activity != null) {
                activity.handleQrCode(s);
            }
        }
    }

    /**
     * 处理图片二维码解析的数据
     *
     * @param s
     */
    public void handleQrCode(String s) {
        if (null == s) {
            showInfo(getString(R.string.not_recognize_qr_code));
        } else {
            handleResult(s);
        }
    }
    private void handleResult(String result) {
        //处理二维码结果
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT,result);
        setResult(RESULT_OK, intent);
        finish();
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