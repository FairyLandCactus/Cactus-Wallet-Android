package com.qianlihu.solanawallet.utils;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianlihu.solanawallet.MainActivity;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.interfere.IFingerprint;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * author : Duan
 * date   : 2022/8/6 9:57
 * desc   : 指纹验证
 * version: 1.0.0
 */
public class FingerprintPwd {

    private static final String DEFAULT_KEY_NAME = "default_key";
    private static boolean isCancel;

    public static void fingerprintVerify(Context context, IFingerprint fingerprint) {
        isCancel = false;
        View view = LayoutInflater.from(context).inflate(R.layout.fingerprint_dialog, null);
        TextView errorMsg = view.findViewById(R.id.error_msg);
        TextView tvPwdVerify = view.findViewById(R.id.pwd_verify);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        Dialog dialog = new DialogView(context, view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        //获取系统服务对象
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        //检测是否有指纹识别的硬件
        if (fingerprintManager == null) {
            fingerprint.onVerify(false);
            return;
        }
        if (!fingerprintManager.isHardwareDetected()) {
            fingerprint.onVerify(false);
//            Toast.makeText(this, "您的手机没有指纹功能", Toast.LENGTH_SHORT).show();
            return;
        }

        //检查设备是否处于安全状态中
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            //如果不是出于安全状态中，跳转打开安全保护（锁屏等）
            fingerprint.onVerify(false);
            return;
        }

        //检测系统中是否注册的指纹
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            //没有录入指纹，跳转到指纹录入
//            Toast.makeText(this, "没有录入指纹", Toast.LENGTH_SHORT).show();
            fingerprint.onVerify(false);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 构建对话框
            BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(context)
                    .setTitle(context.getString(R.string.verify_fingerprint))
                    .setDescription("")
                    .setNegativeButton(context.getString(R.string.pwd_verify), context.getMainExecutor(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(, "取消验证", Toast.LENGTH_SHORT).show();
                            fingerprint.onVerify(false);
                        }
                    }).build();
            // 指纹识别回调
            BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.i("TAG", "onAuthenticationError: errorCode = " + errorCode + ", errString = " + errString);
                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                        fingerprint.onVerify(false);
                    } else if (errorCode == FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED) {
                        fingerprint.onClose();
                        isCancel = true;

                    }
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.i("TAG", "onAuthenticationSucceeded:");
                    if (!isCancel) {
                        fingerprint.onVerify(true);
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.i("TAG", "onAuthenticationFailed:");
//                    fingerprint.onVerify(false);
                }
            };

            // 开始验证指纹
            CancellationSignal cancellationSignal = new CancellationSignal();
            cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {
                    Log.i("TAG", "setOnCancelListener:");
                }
            });
            biometricPrompt.authenticate(cancellationSignal, context.getMainExecutor(), authenticationCallback);
        } else {
            if (!supportFingerprint(context)) {
                fingerprint.onVerify(false);
                return;
            }
            KeyStore keyStore;
            try {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
                keyGenerator.init(builder.build());
                keyGenerator.generateKey();
                SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
                Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                cipher.init(Cipher.ENCRYPT_MODE, key);

                CancellationSignal mCancellationSignal = new CancellationSignal();
                fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        errorMsg.setText(errString);
                        if (errorCode == 5) {
                            errorMsg.setText("");
                            isCancel = true;
                        }
                        Log.i("TAG", "onAuthenticationError: errorCode = " + errorCode + ", errString = " + errString);
                        if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                            dialog.dismiss();
                            fingerprint.onVerify(false);
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        errorMsg.setText(helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        dialog.dismiss();
                        if (!isCancel) {
                            fingerprint.onVerify(true);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        errorMsg.setText(context.getString(R.string.fingerprint_authentication_failed));
                    }
                }, null);

                tvPwdVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCancellationSignal != null) {
                            errorMsg.setText("");
                            dialog.dismiss();
                            fingerprint.onVerify(false);
                            isCancel = true;
                        }
                    }
                });

                ivClose.setOnClickListener(v -> {
                    errorMsg.setText("");
                    dialog.dismiss();
                    fingerprint.onClose();
                });
                dialog.show();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean supportFingerprint(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
//            Toast.makeText(this, "您的系统版本过低，不支持指纹功能", Toast.LENGTH_LONG).show();
            return false;
        } else {
            KeyguardManager keyguardManager = context.getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = context.getSystemService(FingerprintManager.class);
            if (!fingerprintManager.isHardwareDetected()) {
//                Toast.makeText(this, "您的手机不支持指纹功能", Toast.LENGTH_LONG).show();
                return false;
            } else if (!keyguardManager.isKeyguardSecure()) {
//                Toast.makeText(this, "您还未设置锁屏，请先设置锁屏并添加一个指纹", Toast.LENGTH_LONG).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                Toast.makeText(this, "您至少需要在系统设置中添加一个指纹", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

}
