package com.meihuayishu.vone.modul;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ScrollView;

import com.meihuayishu.vone.ui.MainActivity;
import com.meihuayishu.vone.ui.ResultActivity;
import com.meihuayishu.vone.utils.RequestPermissionUtils;
import com.meihuayishu.vone.utils.ScreenShot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dell on 2017-3-30.
 */
public class ShareThePageImp implements ShareThePageItfc {
    private String[] permissions;

    @Override
    public void sharePageToAllPlatform(final Activity activity, String str) {

        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (0 != RequestPermissionUtils.isPermissionComplete(activity, permissions).size()) {
            RequestPermissionUtils.requestPermission(activity, permissions,"分享动作需要获取以下权限");
            return;
        }

        OnekeyShare oks = new OnekeyShare();
        final String fileName = activity.getCacheDir().toString() + "screenshot"+System.currentTimeMillis() + ".PNG";
//                Log.i("MSL", "onLongClick: " + ResultActivity.this.getCacheDir().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ScreenShot.shootLoacleView(activity, fileName);

            }
        }).start();

        oks.disableSSOWhenAuthorize();

        //权限检查与请求

        oks.setTitle("刚得一卦");

        oks.setText(str);

        oks.setImagePath(fileName);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeImp(activity));

        oks.show(activity);

    }

    @Override
    public void shareScrollViewToAllPlatform(Activity activity, final ScrollView scrollView, String str) {

        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (0 != RequestPermissionUtils.isPermissionComplete(activity, permissions).size()) {
            RequestPermissionUtils.requestPermission(activity, permissions,"分享动作需要获取以下权限");
            return;
        }

        OnekeyShare oks = new OnekeyShare();
        final String fileName = activity.getCacheDir().toString() + "screenshot"+System.currentTimeMillis() + ".PNG";
//                Log.i("MSL", "onLongClick: " + ResultActivity.this.getCacheDir().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                ScreenShot.shootScrollView(scrollView, fileName);

            }
        }).start();

        oks.disableSSOWhenAuthorize();

        //权限检查与请求

        oks.setTitle("");

        oks.setText(str);

        oks.setImagePath(fileName);

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeImp(activity));

        oks.show(activity);

    }
}
