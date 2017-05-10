package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.beans.GuaBean;
import com.meihuayishu.vone.modul.MenuItemClickImp;
import com.meihuayishu.vone.modul.OnMenuItemClickListener;
import com.meihuayishu.vone.utils.TianGanDiZhi;
import com.meihuayishu.vone.view.CircleMenuLayout;

import java.util.Arrays;

import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends AppCompatActivity {

    private CircleMenuLayout mCircleMenuLayout;
    private MenuItemClickImp menuItemClickImp;

    private Toast toast;

    private int[] mItemImages = new int[]{R.mipmap.time, R.mipmap.number, R.mipmap.guayao, R.mipmap.hanzi};

    private String[] mItemTexts =
            null;
    //            new String[]{"时卜", "数卜", "卦爻", "占字"};
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        ShareSDK.initSDK(this);//初始化ShareSDK

        getSupportActionBar().hide();

        toast = Toast.makeText(getApplicationContext(), "再次点击退出易之", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        context = getApplicationContext();
        menuItemClickImp = new MenuItemClickImp(this);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImages, mItemTexts);

        mCircleMenuLayout.setmOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                menuItemClickImp.itemClick(view,pos);
            }

            @Override
            public void itemCenterClick(View view) {
                menuItemClickImp.itemCenterClick(view);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.i("MSL", "onActivityResult: " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0:
                Intent intent = new Intent(MainActivity.this, BaZiActivity.class);
                intent.putExtra("date", data.getStringExtra("date"));
                if (data.getBooleanExtra("isBazi",false)) {
                    intent.putExtra("sex",data.getStringExtra("sex"));
                }
                startActivity(intent);
                break;
            case 1:
                //根据所选时间成卦
                String date = data.getStringExtra("date");
                String mDate = date.split(" ")[0], mTime = date.split(" ")[1];
                String[] mDates = mDate.split("-");
                String[] mTimes = mTime.split(":");
                int month = Integer.valueOf(mDates[1]);
                int day = Integer.valueOf(mDates[2]);
                int hour = Integer.valueOf(mTimes[0]);

                String siZhu = TianGanDiZhi.exchangeGanZhi(date);
                int diZhiIndex = TianGanDiZhi.DIZHI_STR.indexOf(siZhu.toCharArray()[1]);

                Log.e("MSL", "onActivityResult: " + siZhu +"," +  siZhu.toCharArray()[1] +"," + diZhiIndex );
                GuaBean guaBean = new GuaBean();
                guaBean.setShang((diZhiIndex + month + day) % 8);
                guaBean.setXia((diZhiIndex + month + day + hour) % 8);
                guaBean.setDong((diZhiIndex + month + day + hour) % 6);

//                Log.e("MSL", "onActivityResult: " + diZhiIndex +","+ month +","+ day +","+ guaBean.getShang());

                Intent intent1 = new Intent(MainActivity.this,ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("gua",guaBean);
                intent1.putExtras(bundle);
                startActivity(intent1);

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (toast.getView().getParent() != null) {
            toast.cancel();
            super.onBackPressed();
        } else {
            toast.show();
        }
    }
}
