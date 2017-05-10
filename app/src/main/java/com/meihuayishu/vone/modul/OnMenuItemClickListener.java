package com.meihuayishu.vone.modul;

import android.content.Context;
import android.view.View;

/**
 * Menu item点击事件的接口
 * Created by Dell on 2017-2-24.
 */
public interface OnMenuItemClickListener {
    void itemClick(View view, int pos) ;
    void itemCenterClick(View view);
}
