package com.qianlihu.solanawallet.base;

/**
 * author : Duan
 * date   : 2021/4/89:09
 * desc   :
 * version: 1.0.0
 */
public interface BaseView {

    /**
     * 显示dialog
     */
    void showLoading();

    void showLoadingInfos(String title, String content, boolean style);

    void hideLoadingInfos();

    /**
     * 显示下载文件dialog
     */

    void showLoadingFileDialog(String msg);

    /**
     * 隐藏下载文件dialog
     */

    void hideLoadingFileDialog();

    /**
     * 下载进度
     *
     * @param totalSize
     * @param downSize
     */

    void onProgress(long totalSize, long downSize);

    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);

    /**
     * 成功提示信息
     * @param msg
     */
    void showSuccess(String msg);

    /**
     * 错误码
     */
    void onErrorCode(int code, String msg);

}
