package com.example.giaodientest.Activities;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;

public class ZUltility {
    static final int QUAN_AO_REQUEST = 200;
    static final int DON_HANG_REQUEST = 300;

    static final String ERROR_INVALID_CUSTOM_TOKEN = "ERROR_INVALID_CUSTOM_TOKEN";
    static final String ERROR_CUSTOM_TOKEN_MISMATCH = "ERROR_CUSTOM_TOKEN_MISMATCH";
    static final String ERROR_INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL";
    static final String ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL";
    static final String ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD";
    static final String ERROR_USER_MISMATCH = "ERROR_USER_MISMATCH";
    static final String ERROR_REQUIRES_RECENT_LOGIN = "ERROR_REQUIRES_RECENT_LOGIN";
    static final String ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL
            = "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL";
    static final String ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE";
    static final String ERROR_CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE";
    static final String ERROR_USER_DISABLED = "ERROR_USER_DISABLED";
    static final String ERROR_USER_TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED";
    static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    static final String ERROR_INVALID_USER_TOKEN = "ERROR_INVALID_USER_TOKEN";
    static final String ERROR_OPERATION_NOT_ALLOWED = "ERROR_OPERATION_NOT_ALLOWED";
    static final String ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD";

    static String formatMoney(String number)
    {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amount);
    }

    static String getNotifyException(Exception e) {
        String notiText = "";
        String errorCode = ((FirebaseAuthException) e).getErrorCode();
        switch (errorCode)
        {
            case ERROR_INVALID_CREDENTIAL:
                notiText = "Credential không hợp lệ";
                break;

            case ERROR_INVALID_EMAIL:
                notiText = "Email này không hợp lệ";
                break;
            case ERROR_WRONG_PASSWORD:
                notiText = "Mật khẩu sai";
                break;
            case ERROR_REQUIRES_RECENT_LOGIN:
                notiText = "Yêu cầu lần đăng nhập gần đây nhất";
                break;
            case ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL:
                notiText = "Tài khoản này đã tồn tại với 1 credential khác";
                break;

            case ERROR_EMAIL_ALREADY_IN_USE:
                notiText = "Tài khoản hiện đang được truy cập ở 1 thiết bị khác";
                break;
            case ERROR_CREDENTIAL_ALREADY_IN_USE:
                notiText = "Tài khoản này đã tồn tại";
                break;
            case ERROR_USER_DISABLED:
                notiText = "Quản trị đã vô hiệu hóa tài khoản của bạn";
                break;
            case ERROR_USER_TOKEN_EXPIRED:
                notiText = "Tài khoản này đã hết hạn sử dụng";
                break;

            case ERROR_USER_NOT_FOUND:
                notiText = "Email này không tồn tại";
                break;
            case ERROR_WEAK_PASSWORD:
                notiText = "Mật khẩu này quá yếu";
                break;
            default:
                notiText = e.toString();
                break;
        }

        return notiText;
    }

    public static String getNotifyFirebaseExeption(FirebaseFirestoreException e) {
        String notiText = "";
        switch (e.getCode())
        {
            case PERMISSION_DENIED:
                notiText = "Người dùng không có quyền truy cập dữ liệu";
                break;
            case ABORTED:
                notiText = "Lệnh truy cập dữ liệu bị hủy";
                break;
            case DEADLINE_EXCEEDED:
                notiText = "Quá hạn truy cập dữ liệu, hãy kiểm tra internet";
                break;

            case UNAVAILABLE:
                notiText = "Dữ liệu không tồn tại";
                break;
            case ALREADY_EXISTS:
                notiText = "Dữ liệu đã tồn tại";
                break;
            case INVALID_ARGUMENT:
                notiText = "Tham số truy vấn không hợp lệ";
                break;

            case CANCELLED:
            case DATA_LOSS:
            case UNIMPLEMENTED:
            case UNAUTHENTICATED:
            case NOT_FOUND:
            case FAILED_PRECONDITION:
            case RESOURCE_EXHAUSTED:
            case OUT_OF_RANGE:
            default:
                notiText = e.toString();
                break;
        }

        return notiText;
    }
}
