package com.xinshu.xinxiaoshu.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by sinyuk on 2017/4/7.
 */

public class PermissionHelper {
    public static void showAlertWindowDialog(final Context c) {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setCancelable(true)
                .setTitle("App需要\'出现在其他应用上\'的权限")
                .setMessage("请在应用信息的\'高级\'选项中设置")
                .setPositiveButton("前往",
                        (d, which) -> {
                            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + c.getPackageName()));
                            c.startActivity(i);
                            d.dismiss();
                        })
                .setNegativeButton("算了", (d, which) -> d.cancel())
                .show();
    }
}
