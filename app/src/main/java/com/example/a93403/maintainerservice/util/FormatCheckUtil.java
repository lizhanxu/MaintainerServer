package com.example.a93403.maintainerservice.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.a93403.maintainerservice.R;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class FormatCheckUtil {

    public static boolean checkPhoneNumber(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(context, R.string.phone_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneNumber.substring(0, 1).equals("1")) {
            Toast.makeText(context, R.string.phone_form_not_specs, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNumber.length() != 11) {
            Toast.makeText(context, R.string.phone_length_not_specs, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.password_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, R.string.password_not_more_six, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkEmail(String email) {
        // 验证邮箱的正则表达式
        String format = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return email.matches(format);
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }
}
