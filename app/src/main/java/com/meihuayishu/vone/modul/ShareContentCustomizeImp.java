package com.meihuayishu.vone.modul;

import android.Manifest;
import android.app.Activity;

import com.meihuayishu.vone.utils.RequestPermissionUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by Dell on 2017-3-30.
 */
public class ShareContentCustomizeImp implements ShareContentCustomizeCallback {
    private Activity activity ;

    public ShareContentCustomizeImp(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

    }
}
