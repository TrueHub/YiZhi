package com.meihuayishu.vone.modul;

import android.app.Activity;
import android.widget.ScrollView;

/**
 * Created by Dell on 2017-3-30.
 */
public interface ShareThePageItfc {
    void sharePageToAllPlatform(Activity activity, String str);
    void shareScrollViewToAllPlatform(Activity activity, ScrollView scrollView,String str);
}
