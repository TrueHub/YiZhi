package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.modul.ShareThePageImp;
import com.meihuayishu.vone.utils.RequestPermissionUtils;
import com.meihuayishu.vone.utils.TianGanDiZhi;
import com.meihuayishu.vone.utils.bazi.Lvhehun;
import com.meihuayishu.vone.utils.bazi.PaipanClass;

import java.util.Iterator;
import java.util.Map;

public class BaZiActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private ActionBar actionBar;
    private MenuItem mSave;
    private MenuItem mShare;
    private String time;
    private String sex;
    private Lvhehun.sex isman;

    private TextView mTvBirthYang, mTvBirthYin, mTvAnimal, mTvSexBazi;
    private TextView mTvYearshenBazi, mTvMonthshenBazi, mTvHourshenBazi;
    private TextView mTvYearGan, mTvMonthGan, mTvDayGan, mTvHourGan;
    private TextView mTvYearZhi, mTvMonthZhi, mTvDayZhi, mTvHourZhi, mTvKongwuBazi;
    private TextView mTvWangshuai0, mTvWangshuai1, mTvWangshuai2, mTvWangshuai3,
            mTvWangshuai4, mTvWangshuai5, mTvWangshuai6, mTvWangshuai7;
    private TextView mTvNayin0, mTvNayin1, mTvNayin2, mTvNayin3, mTvNayin4, mTvNayin5, mTvNayin6, mTvNayin7;
    private TextView mTvShishen0, mTvShishen1, mTvShishen2, mTvShishen3,
            mTvShishen4, mTvShishen5, mTvShishen6, mTvShishen7;
    private TextView mTvDayun0, mTvDayun1, mTvDayun2, mTvDayun3, mTvDayun4, mTvDayun5, mTvDayun6, mTvDayun7;
    private TextView mTvStartage0, mTvStartage1, mTvStartage2, mTvStartage3, mTvStartage4,
            mTvStartage5, mTvStartage6, mTvStartage7, mTvStartyear0;
    private TextView mTvStartyear1, mTvStartyear2, mTvStartyear3, mTvStartyear4,
            mTvStartyear5, mTvStartyear6, mTvStartyear7;
    private TextView mTvEndyear0, mTvEndyear1, mTvEndyear2, mTvEndyear3,
            mTvEndyear4, mTvEndyear5, mTvEndyear6, mTvEndyear7;
    private TextView mTvTaohua, mTvHunyinshensha;
    private LinearLayout mLlLiunian0, mLlLiunian1, mLlLiunian2, mLlLiunian3,
            mLlLiunian4, mLlLiunian5, mLlLiunian6, mLlLiunian7;
    private LinearLayout mLlYearZanggan, mLlMonthZanggan, mLlDayZanggan, mLlHourZanggan;
    private LinearLayout mLlYearShenshea, mLlMonthShenshea, mLlDayShenshea, mLlHourShenshea;
    private ScrollView scrollView;
    private PaipanClass paipan;
    private long age;
    private TextView mTvWuxingnayinYear;
    private TextView mTvWuxingnayinMonth;
    private TextView mTvWuxingnayinDay;
    private TextView mTvWuxingnayinHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ba_zi);

        initView();

        actionBar = getSupportActionBar();
        actionBar.setTitle("八字排盘结果");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        time = date.substring(0, date.lastIndexOf(":"));
        sex = intent.getStringExtra("sex");
        if (sex.equals("男")) {
            isman = Lvhehun.sex.man;
            mTvSexBazi.setText("乾造:");
        } else if (sex.equals("女")) {
            isman = Lvhehun.sex.woman;
            mTvSexBazi.setText("坤造:");
        }

        showResult();

    }

    private void showResult() {
        paipan = new PaipanClass(time, isman);

        showBaseInfo();//展示生日信息

        showBazi();//展示干支八字，干支十神

        showSizhuNayin();//展示四柱五行纳音

        showZangGan();//展示藏干

        showShenSha();//展示神煞

        showWangShuai();//展示生旺死衰

        showDayunAndNayin();//展示大运和它的纳音

        showDayunShishen();//展示大运十神

        showLiuNian();//展示流年

        mTvTaohua.setText(paipan.getTaohua());//展示桃花位

        showHunyinShensha();//展示婚姻神煞

    }

    private void showSizhuNayin() {
        String[] sizhuNayin = paipan.getSiZhuSound();

        mTvWuxingnayinYear.setText("["+ sizhuNayin[0] + "]");
        mTvWuxingnayinMonth.setText("[" + sizhuNayin[1] + "]");
        mTvWuxingnayinDay.setText("[" + sizhuNayin[2] + "]");
        mTvWuxingnayinHour.setText("[" + sizhuNayin[3] + "]");

    }

    private void showBaseInfo() {
        String mDate = time.split(" ")[0];
        String[] mDates = mDate.split("-");
        int year = Integer.valueOf(mDates[0]);
        int month = Integer.valueOf(mDates[1]);
        int day = Integer.valueOf(mDates[2]);
        int hour = Integer.valueOf(time.split(" ")[1]);

        mTvBirthYang.setText(year + "年" + month + "月" + day + "日" + hour + "时");
        mTvBirthYin.setText(paipan.getLunarDate() + TianGanDiZhi.DIZHI[((hour + 3) / 2) % 12] + "时");
        mTvAnimal.setText(paipan.getAnimal());
    }

    private void showHunyinShensha() {
        String hunyin = paipan.getHunyinShenSha();
        mTvHunyinshensha.setText(hunyin);

    }

    private void showLiuNian() {
        String[] liunian = paipan.getLiunian();
        int a = liunian.length % 10 == 0 ? 1 : 2;
        Integer[] nian = new Integer[liunian.length / 10 + a];
        String[] ganzhi = new String[liunian.length];
        String[] shensha = new String[liunian.length];

//        Log.e("MSL", "showLiuNian: " + liunian.length + "," + liunian[0]);

        for (int i = 0; i < liunian.length; i++) {
            String[] str = liunian[i].trim().split(",");
//            Log.e("MSL", "showLiuNian: " + str[0]);
            if (i % 10 == 0) {
                nian[i / 10] = Integer.parseInt(str[0].trim());
            }
            ganzhi[i] = str[1].trim();
            if (str.length >= 4) shensha[i] = str[3].trim();

        }


        mTvStartyear0.setText(nian[0] + (int) age + "");
        mTvStartyear1.setText(nian[1] + (int) age + "");
        mTvStartyear2.setText(nian[2] + (int) age + "");
        mTvStartyear3.setText(nian[3] + (int) age + "");
        mTvStartyear4.setText(nian[4] + (int) age + "");
        mTvStartyear5.setText(nian[5] + (int) age + "");
        mTvStartyear6.setText(nian[6] + (int) age + "");
        mTvStartyear7.setText(nian[7] + (int) age + "");

        mTvEndyear0.setText(nian[0] + (int) age + 10 + "");
        mTvEndyear1.setText(nian[1] + (int) age + 10 + "");
        mTvEndyear2.setText(nian[2] + (int) age + 10 + "");
        mTvEndyear3.setText(nian[3] + (int) age + 10 + "");
        mTvEndyear4.setText(nian[4] + (int) age + 10 + "");
        mTvEndyear5.setText(nian[5] + (int) age + 10 + "");
        mTvEndyear6.setText(nian[6] + (int) age + 10 + "");
        mTvEndyear7.setText(nian[7] + (int) age + 10 + "");

        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + (int) age]);

            textView.setGravity(Gravity.CENTER);

            mLlLiunian0.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 10 + (int) age]);

            textView.setGravity(Gravity.CENTER);

            mLlLiunian1.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 20 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian2.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 30 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian3.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 40 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian4.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 50 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian5.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 60 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian6.addView(textView);
        }
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setText(ganzhi[i + 70 + (int) age]);

            textView.setGravity(Gravity.CENTER);


            mLlLiunian7.addView(textView);
        }


    }

    private void showDayunAndNayin() {
        String[] dayun = paipan.DayunArray;
        mTvDayun0.setText(dayun[0]);
        mTvDayun1.setText(dayun[1]);
        mTvDayun2.setText(dayun[2]);
        mTvDayun3.setText(dayun[3]);
        mTvDayun4.setText(dayun[4]);
        mTvDayun5.setText(dayun[5]);
        mTvDayun6.setText(dayun[6]);
        mTvDayun7.setText(dayun[7]);

        mTvNayin0.setText("[" + paipan.getGanZhiSound(dayun[0]) + "]");
        mTvNayin1.setText("[" + paipan.getGanZhiSound(dayun[1]) + "]");
        mTvNayin2.setText("[" + paipan.getGanZhiSound(dayun[2]) + "]");
        mTvNayin3.setText("[" + paipan.getGanZhiSound(dayun[3]) + "]");
        mTvNayin4.setText("[" + paipan.getGanZhiSound(dayun[4]) + "]");
        mTvNayin5.setText("[" + paipan.getGanZhiSound(dayun[5]) + "]");
        mTvNayin6.setText("[" + paipan.getGanZhiSound(dayun[6]) + "]");
        mTvNayin7.setText("[" + paipan.getGanZhiSound(dayun[7]) + "]");

        age = paipan.getDayunStartAge();
        mTvStartage0.setText(1 + age + "岁");
        mTvStartage1.setText(11 + age + "岁");
        mTvStartage2.setText(21 + age + "岁");
        mTvStartage3.setText(31 + age + "岁");
        mTvStartage4.setText(41 + age + "岁");
        mTvStartage5.setText(51 + age + "岁");
        mTvStartage6.setText(61 + age + "岁");
        mTvStartage7.setText(71 + age + "岁");


    }

    private void showDayunShishen() {
        String[] shishen = paipan.getDayunShishen();
        mTvShishen0.setText(shishen[0]);
        mTvShishen1.setText(shishen[1]);
        mTvShishen2.setText(shishen[2]);
        mTvShishen3.setText(shishen[3]);
        mTvShishen4.setText(shishen[4]);
        mTvShishen5.setText(shishen[5]);
        mTvShishen6.setText(shishen[6]);
        mTvShishen7.setText(shishen[7]);
    }

    private void showWangShuai() {
        String[] wangshuai = paipan.getDayunShengsi();
        mTvWangshuai0.setText(wangshuai[0]);
        mTvWangshuai1.setText(wangshuai[1]);
        mTvWangshuai2.setText(wangshuai[2]);
        mTvWangshuai3.setText(wangshuai[3]);
        mTvWangshuai4.setText(wangshuai[4]);
        mTvWangshuai5.setText(wangshuai[5]);
        mTvWangshuai6.setText(wangshuai[6]);
        mTvWangshuai7.setText(wangshuai[7]);

    }

    private void showShenSha() {
        String shensha = paipan.getShenSha();
        String[] shenshas = shensha.split("\n");


    }

    private void showBazi() {

        String[] ganzhishishen = paipan.getGanZhiShiShen();
        mTvYearshenBazi.setText(ganzhishishen[0]);
        mTvMonthshenBazi.setText(ganzhishishen[1]);
        mTvHourshenBazi.setText(ganzhishishen[2]);

        String[] bazi = paipan.bazi;
        mTvYearGan.setText(bazi[1]);
        mTvYearZhi.setText(bazi[2]);
        mTvMonthGan.setText(bazi[3]);
        mTvMonthZhi.setText(bazi[4]);
        mTvDayGan.setText(bazi[5]);
        mTvDayZhi.setText(bazi[6]);
        mTvHourGan.setText(bazi[7]);
        mTvHourZhi.setText(bazi[8]);

        int zhi = TianGanDiZhi.DIZHI_STR.indexOf((String) mTvDayZhi.getText());
        int gan = TianGanDiZhi.TIANGAN_STR.indexOf((String) mTvDayGan.getText());

        if (zhi <= gan) zhi += 12;
        int i2 = zhi - gan;
        int i1;
        if (i2 == 12) i2 = 0;
        if (i2 == 0) i1 = 11;
        else i1 = i2 - 1;

        mTvKongwuBazi.setText(TianGanDiZhi.DIZHI[i1] + TianGanDiZhi.DIZHI[i2] + "空");
    }

    private void showZangGan() {
        //年藏干
        Map<String, String> year = paipan.getYearZangGan();

        Iterator iteratorY = year.entrySet().iterator();
        while (iteratorY.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorY.next();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            textView2.setTextColor(getResources().getColor(R.color.mention));

            textView1.setText((String) entry.getKey());
            textView2.setText((String) entry.getValue());

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            mLlYearZanggan.addView(linearLayout);
        }

        //月藏干
        Map<String, String> month = paipan.getMonthZanggan();

        Iterator iteratorM = month.entrySet().iterator();
        while (iteratorM.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorM.next();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            textView2.setTextColor(getResources().getColor(R.color.mention));

            textView1.setText((String) entry.getKey());
            textView2.setText((String) entry.getValue());

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            mLlMonthZanggan.addView(linearLayout);
        }

        //日藏干
        Map<String, String> day = paipan.getDayZanggan();

        Iterator iteratorD = day.entrySet().iterator();
        while (iteratorD.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorD.next();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            textView2.setTextColor(getResources().getColor(R.color.mention));

            textView1.setText((String) entry.getKey());
            textView2.setText((String) entry.getValue());

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            mLlDayZanggan.addView(linearLayout);
        }

        //时藏干
        Map<String, String> hour = paipan.getHourZanggan();

        Iterator iteratorH = hour.entrySet().iterator();
        while (iteratorH.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorH.next();

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            textView2.setTextColor(getResources().getColor(R.color.mention));

            textView1.setText((String) entry.getKey());
            textView2.setText((String) entry.getValue());

            linearLayout.addView(textView1);
            linearLayout.addView(textView2);
            mLlHourZanggan.addView(linearLayout);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        mSave = menu.findItem(R.id.save);
        mShare = menu.findItem(R.id.share);

        mSave.setOnMenuItemClickListener(this);
        mShare.setOnMenuItemClickListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                //需要文件读写权限
//                RequestPermissionUtils.requestPermission(this,new String[] {},"存储到SD卡需要获取以下权限");

                


                break;
            case R.id.share:

                showMessageOKCancel(BaZiActivity.this, "确定把自己的八字信息暴露出去？\n这很危险哦！",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ShareThePageImp().shareScrollViewToAllPlatform(BaZiActivity.this, scrollView, "刚排出来一个八字，大家来猜猜看是谁的？");
                            }
                        });

                break;

            default:

                break;
        }

        return false;
    }

    private static void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("不怕，勇敢的晒出去", okListener)
                .setNegativeButton("算了，这是我的隐私", null)
                .create()
                .show();
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_bazi);
        mTvBirthYang = (TextView) findViewById(R.id.tv_Birth_yang);
        mTvBirthYin = (TextView) findViewById(R.id.tv_Birth_yin);
        mTvAnimal = (TextView) findViewById(R.id.tv_animal);
        mTvSexBazi = (TextView) findViewById(R.id.tv_sex_bazi);
        mTvYearshenBazi = (TextView) findViewById(R.id.tv_yearshen_bazi);
        mTvMonthshenBazi = (TextView) findViewById(R.id.tv_monthshen_bazi);
        mTvHourshenBazi = (TextView) findViewById(R.id.tv_hourshen_bazi);
        mTvYearGan = (TextView) findViewById(R.id.tv_year_gan);
        mTvMonthGan = (TextView) findViewById(R.id.tv_month_gan);
        mTvDayGan = (TextView) findViewById(R.id.tv_day_gan);
        mTvHourGan = (TextView) findViewById(R.id.tv_hour_gan);
        mTvYearZhi = (TextView) findViewById(R.id.tv_year_zhi);
        mTvMonthZhi = (TextView) findViewById(R.id.tv_month_zhi);
        mTvDayZhi = (TextView) findViewById(R.id.tv_day_zhi);
        mTvHourZhi = (TextView) findViewById(R.id.tv_hour_zhi);
        mTvKongwuBazi = (TextView) findViewById(R.id.tv_kongwu_bazi);
        mLlYearZanggan = (LinearLayout) findViewById(R.id.ll_year_zanggan);
        mLlMonthZanggan = (LinearLayout) findViewById(R.id.ll_month_zanggan);
        mLlDayZanggan = (LinearLayout) findViewById(R.id.ll_day_zanggan);
        mLlHourZanggan = (LinearLayout) findViewById(R.id.ll_hour_zanggan);
        mLlYearShenshea = (LinearLayout) findViewById(R.id.ll_year_shenshea);
        mLlMonthShenshea = (LinearLayout) findViewById(R.id.ll_month_shenshea);
        mLlDayShenshea = (LinearLayout) findViewById(R.id.ll_day_shenshea);
        mLlHourShenshea = (LinearLayout) findViewById(R.id.ll_hour_shenshea);
        mTvWangshuai0 = (TextView) findViewById(R.id.tv_wangshuai_0);
        mTvWangshuai1 = (TextView) findViewById(R.id.tv_wangshuai_1);
        mTvWangshuai2 = (TextView) findViewById(R.id.tv_wangshuai_2);
        mTvWangshuai3 = (TextView) findViewById(R.id.tv_wangshuai_3);
        mTvWangshuai4 = (TextView) findViewById(R.id.tv_wangshuai_4);
        mTvWangshuai5 = (TextView) findViewById(R.id.tv_wangshuai_5);
        mTvWangshuai6 = (TextView) findViewById(R.id.tv_wangshuai_6);
        mTvWangshuai7 = (TextView) findViewById(R.id.tv_wangshuai_7);
        mTvNayin0 = (TextView) findViewById(R.id.tv_nayin_0);
        mTvNayin1 = (TextView) findViewById(R.id.tv_nayin_1);
        mTvNayin2 = (TextView) findViewById(R.id.tv_nayin_2);
        mTvNayin3 = (TextView) findViewById(R.id.tv_nayin_3);
        mTvNayin4 = (TextView) findViewById(R.id.tv_nayin_4);
        mTvNayin5 = (TextView) findViewById(R.id.tv_nayin_5);
        mTvNayin6 = (TextView) findViewById(R.id.tv_nayin_6);
        mTvNayin7 = (TextView) findViewById(R.id.tv_nayin_7);
        mTvShishen0 = (TextView) findViewById(R.id.tv_shishen_0);
        mTvShishen1 = (TextView) findViewById(R.id.tv_shishen_1);
        mTvShishen2 = (TextView) findViewById(R.id.tv_shishen_2);
        mTvShishen3 = (TextView) findViewById(R.id.tv_shishen_3);
        mTvShishen4 = (TextView) findViewById(R.id.tv_shishen_4);
        mTvShishen5 = (TextView) findViewById(R.id.tv_shishen_5);
        mTvShishen6 = (TextView) findViewById(R.id.tv_shishen_6);
        mTvShishen7 = (TextView) findViewById(R.id.tv_shishen_7);
        mTvDayun0 = (TextView) findViewById(R.id.tv_dayun_0);
        mTvDayun1 = (TextView) findViewById(R.id.tv_dayun_1);
        mTvDayun2 = (TextView) findViewById(R.id.tv_dayun_2);
        mTvDayun3 = (TextView) findViewById(R.id.tv_dayun_3);
        mTvDayun4 = (TextView) findViewById(R.id.tv_dayun_4);
        mTvDayun5 = (TextView) findViewById(R.id.tv_dayun_5);
        mTvDayun6 = (TextView) findViewById(R.id.tv_dayun_6);
        mTvDayun7 = (TextView) findViewById(R.id.tv_dayun_7);
        mTvStartage0 = (TextView) findViewById(R.id.tv_startage_0);
        mTvStartage1 = (TextView) findViewById(R.id.tv_startage_1);
        mTvStartage2 = (TextView) findViewById(R.id.tv_startage_2);
        mTvStartage3 = (TextView) findViewById(R.id.tv_startage_3);
        mTvStartage4 = (TextView) findViewById(R.id.tv_startage_4);
        mTvStartage5 = (TextView) findViewById(R.id.tv_startage_5);
        mTvStartage6 = (TextView) findViewById(R.id.tv_startage_6);
        mTvStartage7 = (TextView) findViewById(R.id.tv_startage_7);
        mTvStartyear0 = (TextView) findViewById(R.id.tv_startyear_0);
        mTvStartyear1 = (TextView) findViewById(R.id.tv_startyear_1);
        mTvStartyear2 = (TextView) findViewById(R.id.tv_startyear_2);
        mTvStartyear3 = (TextView) findViewById(R.id.tv_startyear_3);
        mTvStartyear4 = (TextView) findViewById(R.id.tv_startyear_4);
        mTvStartyear5 = (TextView) findViewById(R.id.tv_startyear_5);
        mTvStartyear6 = (TextView) findViewById(R.id.tv_startyear_6);
        mTvStartyear7 = (TextView) findViewById(R.id.tv_startyear_7);
        mLlLiunian0 = (LinearLayout) findViewById(R.id.ll_liunian_0);
        mLlLiunian1 = (LinearLayout) findViewById(R.id.ll_liunian_1);
        mLlLiunian2 = (LinearLayout) findViewById(R.id.ll_liunian_2);
        mLlLiunian3 = (LinearLayout) findViewById(R.id.ll_liunian_3);
        mLlLiunian4 = (LinearLayout) findViewById(R.id.ll_liunian_4);
        mLlLiunian5 = (LinearLayout) findViewById(R.id.ll_liunian_5);
        mLlLiunian6 = (LinearLayout) findViewById(R.id.ll_liunian_6);
        mLlLiunian7 = (LinearLayout) findViewById(R.id.ll_liunian_7);
        mTvEndyear0 = (TextView) findViewById(R.id.tv_endyear_0);
        mTvEndyear1 = (TextView) findViewById(R.id.tv_endyear_1);
        mTvEndyear2 = (TextView) findViewById(R.id.tv_endyear_2);
        mTvEndyear3 = (TextView) findViewById(R.id.tv_endyear_3);
        mTvEndyear4 = (TextView) findViewById(R.id.tv_endyear_4);
        mTvEndyear5 = (TextView) findViewById(R.id.tv_endyear_5);
        mTvEndyear6 = (TextView) findViewById(R.id.tv_endyear_6);
        mTvEndyear7 = (TextView) findViewById(R.id.tv_endyear_7);
        mTvTaohua = (TextView) findViewById(R.id.tv_taohua);
        mTvHunyinshensha = (TextView) findViewById(R.id.tv_hunyinshensha);
        mTvWuxingnayinYear = (TextView) findViewById(R.id.tv_wuxingnayin_year);
        mTvWuxingnayinMonth = (TextView) findViewById(R.id.tv_wuxingnayin_month);
        mTvWuxingnayinDay = (TextView) findViewById(R.id.tv_wuxingnayin_day);
        mTvWuxingnayinHour = (TextView) findViewById(R.id.tv_wuxingnayin_hour);
    }

    @Override
    public void onClick(View v) {

    }
}
