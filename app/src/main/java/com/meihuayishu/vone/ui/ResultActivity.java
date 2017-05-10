package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.beans.GuaBean;
import com.meihuayishu.vone.modul.ShareThePageImp;
import com.meihuayishu.vone.utils.BAGUA;
import com.meihuayishu.vone.utils.DbHelper;
import com.meihuayishu.vone.utils.TianGanDiZhi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2017-2-28.
 */
public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private GuaBean guaBean;
    private int[] imgs = new int[]{
            R.mipmap.qian1,
            R.mipmap.dui2,
            R.mipmap.li3,
            R.mipmap.zhen4,
            R.mipmap.xun5,
            R.mipmap.kan6,
            R.mipmap.gen7,
            R.mipmap.kun8};

    private ImageView mHuShang;
    private ImageView mBianShang;
    private ImageView mBenXia;
    private ImageView mHuXia;
    private ImageView mBianXia;
    private ImageView mBenShang;
    private TextView mBenName, mHuName, mBianName, mTvBenshangG, mTvBenshangWuxing, mTvBenshangYishu,
            mTvBenshangFangwei, mTvBenxiaG, mTvBenxiaWuxing, mTvBenxiaYishu, mTvBenxiaFangwei, mTvCurrenttime,
            mTvHushangG, mTvHushangWuxing, mTvHushangYishu, mTvBianxiaYishu, mTvBianxiaFangwei,
            mTvHushangFangwei, mTvHuxiaG, mTvHuxiaWuxing, mTvHuxiaYishu, mTvHuxiaFangwei, mTvBianshangG,
            mTvBianshangWuxing, mTvBianshangYishu, mTvBianshangFangwei, mTvBianxiaG, mTvBianxiaWuxing;
    private String yishu_ben_up, yishu_hu_up, yishu_bian_up;
    private String yishu_ben_down, yishu_hu_down, yishu_bian_down;
    private FrameLayout mFramelayout;
    private FragmentManager manager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int shang, xia, huShang, huXia, bianXia, bianShang;
    private LinearLayout mBen;
    private LinearLayout mHu;
    private LinearLayout mBian;
    private Fragment lastFragment;
    private LinearLayout mPageResult;
    private ArrayList<LinearLayout> linearList = new ArrayList<>();
    private List<Drawable> drawableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result_gua);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        initView();

        guaBean = (GuaBean) getIntent().getParcelableExtra("gua");

//        Log.i("MSL", "onCreate: " + guaBean.toString());

        showTime();

        showResult(guaBean); //展示卦象結果

        explainResult(); //解釋卦象結果

        mBen.setBackground(getResources().getDrawable(R.drawable.bg_conner_top));
        mFramelayout.setBackground(getResources().getDrawable(R.drawable.bg_conner_not_lefttop));
        linearList.add(mBen);
        linearList.add(mHu);
        linearList.add(mBian);
        drawableList.add(getResources().getDrawable(R.drawable.bg_conner_not_lefttop));
        drawableList.add(getResources().getDrawable(R.drawable.bg_conner));
        drawableList.add(getResources().getDrawable(R.drawable.bg_conner_not_righttop));


//      分享出去的click
        mPageResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showMessageOKCancel(ResultActivity.this, "分享出去？",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ShareThePageImp().sharePageToAllPlatform(ResultActivity.this,"忽有所感，遂得一卦，解以推之，但求无咎。");
                            }
                        });
                return false;
            }
        });


    }


    private void showTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        long time = System.currentTimeMillis();

        String datestr = sdf.format(new Date(time));

//        Log.i("MSL", "showTime: " + datestr);

        String fourZ = TianGanDiZhi.exchangeGanZhi(datestr);

        mTvCurrenttime.setText(datestr + "  " + fourZ + "  ");

    }


    /**
     * 三个任务
     * 1.展示本卦，乾一兑二离三震四巽五坎六艮七坤八
     * 2.展示互卦，取本卦的五四三爻为上卦，四三二为下卦
     * 3.展示变卦，根据动爻变，1~6对应改变本卦初爻至上爻，阴阳互转
     */
    private void showResult(GuaBean guaBean) {
        shang = guaBean.getShang();
        xia = guaBean.getXia();
        int dong = guaBean.getDong();

        if (shang == 0) shang = 8;
        if (xia == 0) xia = 8;
        if (dong == 0) dong = 6;

//        Log.i("MSL", "showResult: " + shang +","+ xia + "," + dong);

        //显示本卦、互卦、变卦
        //显示本卦
        mBenShang.setImageResource(imgs[shang - 1]);
        mBenXia.setImageResource(imgs[xia - 1]);

        DbHelper dbHelper = new DbHelper(this, "jiegua.db");
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

//        Log.i("MSL", "showResult____" + "select * from chang where UPEARLYNUM ='" + shang + "' and DOWNEARLYNUM='" + xia + "'");
        Cursor cursor = sqLiteDatabase.rawQuery("select * from chang where UPEARLYNUM ='" + shang + "' and DOWNEARLYNUM='" + xia + "'", null);

        if (cursor.moveToFirst()) {
            mBenName.setText(cursor.getString(cursor.getColumnIndex("HEXANAME")));
            yishu_ben_up = cursor.getString(cursor.getColumnIndex("UPEARLYNUM")) + "," + cursor.getString(cursor.getColumnIndex("UPLATERNUM"));
            yishu_ben_down = cursor.getString(cursor.getColumnIndex("DOWNEARLYNUM")) + "," + cursor.getString(cursor.getColumnIndex("DOWNLATERNUM"));
        }
        cursor.close();

        //显示互卦
        huShang = getHuShang(shang, xia);
        huXia = getHuxia(shang, xia);
        mHuShang.setImageResource(imgs[huShang - 1]);
        mHuXia.setImageResource(imgs[huXia - 1]);

        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from chang where UPEARLYNUM ='" + huShang + "' and DOWNEARLYNUM='" + huXia + "'", null);
        if (cursor1.moveToFirst()) {
            mHuName.setText(cursor1.getString(cursor.getColumnIndex("HEXANAME")));
            yishu_hu_up = cursor1.getString(cursor1.getColumnIndex("UPEARLYNUM")) + "," + cursor1.getString(cursor1.getColumnIndex("UPLATERNUM"));
            yishu_hu_down = cursor1.getString(cursor1.getColumnIndex("DOWNEARLYNUM")) + "," + cursor1.getString(cursor1.getColumnIndex("DOWNLATERNUM"));
        }
        cursor1.close();

        //显示变卦
        int[] bian = getBianGua(shang, xia, dong);
        bianShang = bian[0];
        bianXia = bian[1];
        mBianShang.setImageResource(imgs[bianShang - 1]);
        mBianXia.setImageResource(imgs[bianXia - 1]);

        Cursor cursor2 = sqLiteDatabase.rawQuery("select * from chang where UPEARLYNUM ='" + bianShang + "' and DOWNEARLYNUM='" + bianXia + "'", null);
        if (cursor2.moveToFirst()) {
            mBianName.setText(cursor2.getString(cursor.getColumnIndex("HEXANAME")));
            yishu_bian_up = cursor2.getString(cursor2.getColumnIndex("UPEARLYNUM")) + "," + cursor2.getString(cursor2.getColumnIndex("UPLATERNUM"));
            yishu_bian_down = cursor2.getString(cursor2.getColumnIndex("DOWNEARLYNUM")) + "," + cursor2.getString(cursor2.getColumnIndex("DOWNLATERNUM"));
        }
        cursor2.close();

        //显示五行、易数、方位等信息
        mTvBenshangG.setText(BAGUA.GUAXIANG[shang - 1]);
        mTvBenshangWuxing.setText(BAGUA.FIVE_ELEMENTS[shang - 1]);
        mTvBenshangYishu.setText(yishu_ben_up);
        mTvBenshangFangwei.setText(BAGUA.POSITION[shang - 1]);

        mTvBenxiaG.setText(BAGUA.GUAXIANG[xia - 1]);
        mTvBenxiaWuxing.setText(BAGUA.FIVE_ELEMENTS[xia - 1]);
        mTvBenxiaYishu.setText(yishu_ben_down);
        mTvBenxiaFangwei.setText(BAGUA.POSITION[xia - 1]);


        mTvHushangG.setText(BAGUA.GUAXIANG[huShang - 1]);
        mTvHushangWuxing.setText(BAGUA.FIVE_ELEMENTS[huShang - 1]);
        mTvHushangYishu.setText(yishu_hu_up);
        mTvHushangFangwei.setText(BAGUA.POSITION[huShang - 1]);

        mTvHuxiaG.setText(BAGUA.GUAXIANG[huXia - 1]);
        mTvHuxiaWuxing.setText(BAGUA.FIVE_ELEMENTS[huXia - 1]);
        mTvHuxiaYishu.setText(yishu_hu_down);
        mTvHuxiaFangwei.setText(BAGUA.POSITION[huXia - 1]);


        mTvBianshangG.setText(BAGUA.GUAXIANG[bianShang - 1]);
        mTvBianshangWuxing.setText(BAGUA.FIVE_ELEMENTS[bianShang - 1]);
        mTvBianshangYishu.setText(yishu_bian_up);
        mTvBianshangFangwei.setText(BAGUA.POSITION[bianShang - 1]);

        mTvBianxiaG.setText(BAGUA.GUAXIANG[bianXia - 1]);
        mTvBianxiaWuxing.setText(BAGUA.FIVE_ELEMENTS[bianXia - 1]);
        mTvBianxiaYishu.setText(yishu_bian_down);
        mTvBianxiaFangwei.setText(BAGUA.POSITION[bianXia - 1]);
    }

    private void explainResult() {

        initFrags();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.framelayout, fragments.get(0)).commit();
        lastFragment = fragments.get(0);
    }

    private void initFrags() {
        Fragment benFragment = new GuaciFragment();
        Bundle argBen = new Bundle();
        argBen.putInt("up", shang);
        argBen.putInt("down", xia);
        argBen.putString("name", "〔本卦〕");
        benFragment.setArguments(argBen);
        fragments.add(benFragment);

        Fragment huFragment = new GuaciFragment();
        Bundle argHu = new Bundle();
        argHu.putInt("up", huShang);
        argHu.putInt("down", huXia);
        argHu.putString("name", "〔互卦〕");
        huFragment.setArguments(argHu);
        fragments.add(huFragment);

        Fragment bianFragment = new GuaciFragment();
        Bundle argBian = new Bundle();
        argBian.putInt("up", bianShang);
        argBian.putInt("down", bianXia);
        argBian.putString("name", "〔變卦〕");
        bianFragment.setArguments(argBian);
        fragments.add(bianFragment);
    }

    private void initView() {
        mBenName = (TextView) findViewById(R.id.ben_name);
        mHuName = (TextView) findViewById(R.id.hu_name);
        mBenShang = (ImageView) findViewById(R.id.ben_shang);
        mHuShang = (ImageView) findViewById(R.id.hu_shang);
        mBianShang = (ImageView) findViewById(R.id.bian_shang);
        mBenXia = (ImageView) findViewById(R.id.ben_xia);
        mHuXia = (ImageView) findViewById(R.id.hu_xia);
        mBianXia = (ImageView) findViewById(R.id.bian_xia);
        mBianName = (TextView) findViewById(R.id.bian_name);
        mTvBenshangG = (TextView) findViewById(R.id.tv_benshang_g);
        mTvBenshangWuxing = (TextView) findViewById(R.id.tv_benshang_wuxing);
        mTvBenshangYishu = (TextView) findViewById(R.id.tv_benshang_yishu);
        mTvBenshangFangwei = (TextView) findViewById(R.id.tv_benshang_fangwei);
        mTvBenxiaG = (TextView) findViewById(R.id.tv_benxia_g);
        mTvBenxiaWuxing = (TextView) findViewById(R.id.tv_benxia_wuxing);
        mTvBenxiaYishu = (TextView) findViewById(R.id.tv_benxia_yishu);
        mTvBenxiaFangwei = (TextView) findViewById(R.id.tv_benxia_fangwei);
        mTvHushangG = (TextView) findViewById(R.id.tv_hushang_g);
        mTvHushangWuxing = (TextView) findViewById(R.id.tv_hushang_wuxing);
        mTvHushangYishu = (TextView) findViewById(R.id.tv_hushang_yishu);
        mTvHushangFangwei = (TextView) findViewById(R.id.tv_hushang_fangwei);
        mTvHuxiaG = (TextView) findViewById(R.id.tv_huxia_g);
        mTvHuxiaWuxing = (TextView) findViewById(R.id.tv_huxia_wuxing);
        mTvHuxiaYishu = (TextView) findViewById(R.id.tv_huxia_yishu);
        mTvHuxiaFangwei = (TextView) findViewById(R.id.tv_huxia_fangwei);
        mTvBianshangG = (TextView) findViewById(R.id.tv_bianshang_g);
        mTvBianshangWuxing = (TextView) findViewById(R.id.tv_bianshang_wuxing);
        mTvBianshangYishu = (TextView) findViewById(R.id.tv_bianshang_yishu);
        mTvBianshangFangwei = (TextView) findViewById(R.id.tv_bianshang_fangwei);
        mTvBianxiaG = (TextView) findViewById(R.id.tv_bianxia_g);
        mTvBianxiaWuxing = (TextView) findViewById(R.id.tv_bianxia_wuxing);
        mTvBianxiaYishu = (TextView) findViewById(R.id.tv_bianxia_yishu);
        mTvBianxiaFangwei = (TextView) findViewById(R.id.tv_bianxia_fangwei);
        mTvCurrenttime = (TextView) findViewById(R.id.tv_currenttime);
        mFramelayout = (FrameLayout) findViewById(R.id.framelayout);
        mBen = (LinearLayout) findViewById(R.id.ben);
        mBen.setOnClickListener(this);
        mHu = (LinearLayout) findViewById(R.id.hu);
        mHu.setOnClickListener(this);
        mBian = (LinearLayout) findViewById(R.id.bian);
        mBian.setOnClickListener(this);
        mPageResult = (LinearLayout) findViewById(R.id.page_result);
        mPageResult.setOnClickListener(this);

    }

    private int getHuShang(int benShang, int benXia) {

        if (benShang == 1 || benShang == 2) {
            if (benXia % 2 == 0) {
                return 5;
            } else {
                return 1;
            }
        } else if (benShang == 3 || benShang == 4) {
            if (benXia % 2 == 0) {
                return 6;
            } else {
                return 2;
            }

        } else if (benShang == 5 || benShang == 6) {
            if (benXia % 2 == 0) {
                return 7;
            } else {
                return 3;
            }

        } else if (benShang == 7 || benShang == 8) {
            if (benXia % 2 == 0) {
                return 8;
            } else {
                return 4;
            }
        }
        return 0;
    }

    private int getHuxia(int benShang, int benXia) {
        if (benXia == 1 || benXia == 5) {
            if (benShang < 5) {
                return 1;
            } else {
                return 2;
            }
        } else if (benXia == 2 || benXia == 6) {
            if (benShang < 5) {
                return 3;
            } else {
                return 4;
            }
        } else if (benXia == 3 || benXia == 7) {
            if (benShang < 5) {
                return 5;
            } else {
                return 6;
            }
        } else if (benXia == 4 || benXia == 8) {
            if (benShang < 5) {
                return 7;
            } else {
                return 8;
            }
        }

        return 0;
    }

    private int[] getBianGua(int benShang, int benXia, int dong) {

        if (dong < 4) { //上卦不变，下卦变一爻
            int xia = benXia;

            switch (dong) {
                case 1:
                    if (benXia < 5) {
                        xia += 4;
                    } else {
                        xia -= 4;
                    }

                    break;
                case 2:
                    if (benXia == 1 || benXia == 2 || benXia == 5 || benXia == 6) {
                        xia += 2;
                    } else {
                        xia -= 2;
                    }
                    break;
                case 3:
                    if (benXia % 2 != 0) {
                        xia++;
                    } else {
                        xia--;
                    }
                    break;
            }
            return new int[]{benShang, xia};

        } else {//下卦不变，上卦变一爻
            int shang = benShang;

            switch (dong) {
                case 4:
                    if (benShang < 5) {
                        shang += 4;
                    } else {
                        shang -= 4;
                    }

                    break;
                case 5:
                    if (benShang == 1 || benShang == 2 || benShang == 5 || benShang == 6) {
                        shang += 2;
                    } else {
                        shang -= 2;
                    }
                    break;
                case 6:
                    if (benShang % 2 != 0) {
                        shang++;
                    } else {
                        shang--;
                    }
                    break;
            }
            return new int[]{shang, benXia};
        }

    }

    @Override
    public void onClick(View v) {


        if (v.getTag() == null) return;
        int tag = Integer.valueOf(v.getTag().toString());

        for (int i = 0; i < linearList.size(); i++) {
            if (i == tag) {
                linearList.get(i).setBackground(getResources().getDrawable(R.drawable.bg_conner_top));
                mFramelayout.setBackground(drawableList.get(i));
            }else {
                linearList.get(i).setBackgroundColor(0x00000000);
            }
        }

        if (fragments.get(tag).isAdded()) {
            manager.beginTransaction().show(fragments.get(tag)).commit();
        } else {
            manager.beginTransaction().add(R.id.framelayout, fragments.get(tag)).commit();
        }
        if (tag != fragments.indexOf(lastFragment)) {
            manager.beginTransaction().hide(lastFragment).commit();
        }
        lastFragment = fragments.get(tag);
    }

    private static void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("懂我", okListener)
                .setNegativeButton("算了", null)
                .create()
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }
}
