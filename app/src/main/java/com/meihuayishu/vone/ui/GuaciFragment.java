package com.meihuayishu.vone.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.utils.BAGUA;
import com.meihuayishu.vone.utils.DbHelper;


public class GuaciFragment extends Fragment {
    private TextView mTvOrder;
    private TextView mTvName;
    private TextView mTvStru;
    private TextView mTvYong;
    private TextView mTvLine6Name;
    private ImageView mIvYao6;
    private TextView mTvLine6Content;
    private TextView mTvLine5Name;
    private ImageView mIvYao5;
    private TextView mTvLine5Content;
    private TextView mTvLine4Name;
    private ImageView mIvYao4;
    private TextView mTvLine4Content;
    private TextView mTvLine3Name;
    private ImageView mIvYao3;
    private TextView mTvLine3Content;
    private TextView mTvLine2Name;
    private ImageView mIvYao2;
    private TextView mTvLine2Content;
    private TextView mTvLine1Name;
    private ImageView mIvYao1;
    private TextView mTvLine1Content;
    private TextView mTvTuan;
    private TextView mTvXiang;

    private int up, down ;
    private String order;

    public GuaciFragment(){};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_guaci, container, false);

        up = getArguments().getInt("up");
        down = getArguments().getInt("down");
        order = getArguments().getString("name");

        initView(view);

        showGuaCi();

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        return view;
    }

    private void showGuaCi() {

        if (1 == up  && 1 == down ) {
            mTvYong.setVisibility(View.VISIBLE);
        }
        if (8 == up  && 8 == down ) {
            mTvYong.setVisibility(View.VISIBLE);
            mTvYong.setText("用六，利永貞。");
        }


        mTvOrder.setText(order);
        mTvStru.setText("〔" + BAGUA.GUAXIANG[down -1 ] + "下" +BAGUA.GUAXIANG[up -1] + "上〕");

        if (up >= 1 && up <=4) {
            mTvLine4Name.setText("九四");
            mIvYao4.setImageResource(R.mipmap.yang);
        }else {
            mTvLine4Name.setText("六四");
            mIvYao4.setImageResource(R.mipmap.yin);
        }
        if (up == 1 || up ==2 || up ==5 || up ==6) {
            mTvLine5Name.setText("九五");
            mIvYao5.setImageResource(R.mipmap.yang);
        }else {
            mTvLine5Name.setText("六五");
            mIvYao5.setImageResource(R.mipmap.yin);
        }
        if (up % 2 != 0) {
            mTvLine6Name.setText("上九");
            mIvYao6.setImageResource(R.mipmap.yang);
        }else {
            mTvLine6Name.setText("上六");
            mIvYao6.setImageResource(R.mipmap.yin);
        }

        if (down >= 1 && down <=4) {
            mTvLine1Name.setText("初九");
            mIvYao1.setImageResource(R.mipmap.yang);
        }else {
            mTvLine1Name.setText("初六");
            mIvYao1.setImageResource(R.mipmap.yin);
        }
        if (down == 1 || down ==2 || down ==5 || down ==6) {
            mTvLine2Name.setText("九二");
            mIvYao2.setImageResource(R.mipmap.yang);
        }else {
            mTvLine2Name.setText("六二");
            mIvYao2.setImageResource(R.mipmap.yin);
        }
        if (down % 2 != 0) {
            mTvLine3Name.setText("九三");
            mIvYao3.setImageResource(R.mipmap.yang);
        }else {
            mTvLine3Name.setText("六三");
            mIvYao3.setImageResource(R.mipmap.yin);
        }


        DbHelper dbHelper = new DbHelper(getActivity(), "jiegua.db");
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from hexanote where UPEARLYNUM ='" + up + "' and DOWNEARLYNUM='" + down + "'", null);
        StringBuffer xiang = new StringBuffer();
        if (cursor.moveToFirst()) {
            mTvName.setText(cursor.getString(cursor.getColumnIndex("HEXANAME")));
            mTvLine1Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE1")));
            mTvLine2Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE2")));
            mTvLine3Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE3")));
            mTvLine4Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE4")));
            mTvLine5Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE5")));
            mTvLine6Content.setText(cursor.getString(cursor.getColumnIndex("LINENOTE6")));
            mTvTuan.setText(cursor.getString(cursor.getColumnIndex("EXPLANATION")));

            xiang.append(cursor.getString(cursor.getColumnIndex("PHENOMENON")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN1")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN2")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN3")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN4")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN5")))
                    .append(cursor.getString(cursor.getColumnIndex("LINEPHEN6")));
            if (1 == up  && 1 == down ) {
                xiang.append("用九，天德不可為首也");
            }
            if (8 == up  && 8 == down ) {
                xiang.append("用六，永貞，以大終也");
            }
            mTvXiang.setText(xiang);
        }
        cursor.close();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        mTvOrder = (TextView) view.findViewById(R.id.tv_order);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvStru = (TextView) view.findViewById(R.id.tv_stru);
        mTvYong = (TextView) view.findViewById(R.id.tv_yong);
        mTvLine6Name = (TextView) view.findViewById(R.id.tv_line6_name);
        mIvYao6 = (ImageView) view.findViewById(R.id.iv_yao6);
        mTvLine6Content = (TextView) view.findViewById(R.id.tv_line6_content);
        mTvLine5Name = (TextView) view.findViewById(R.id.tv_line5_name);
        mIvYao5 = (ImageView) view.findViewById(R.id.iv_yao5);
        mTvLine5Content = (TextView) view.findViewById(R.id.tv_line5_content);
        mTvLine4Name = (TextView) view.findViewById(R.id.tv_line4_name);
        mIvYao4 = (ImageView) view.findViewById(R.id.iv_yao4);
        mTvLine4Content = (TextView) view.findViewById(R.id.tv_line4_content);
        mTvLine3Name = (TextView) view.findViewById(R.id.tv_line3_name);
        mIvYao3 = (ImageView) view.findViewById(R.id.iv_yao3);
        mTvLine3Content = (TextView) view.findViewById(R.id.tv_line3_content);
        mTvLine2Name = (TextView) view.findViewById(R.id.tv_line2_name);
        mIvYao2 = (ImageView) view.findViewById(R.id.iv_yao2);
        mTvLine2Content = (TextView) view.findViewById(R.id.tv_line2_content);
        mTvLine1Name = (TextView) view.findViewById(R.id.tv_line1_name);
        mIvYao1 = (ImageView) view.findViewById(R.id.iv_yao1);
        mTvLine1Content = (TextView) view.findViewById(R.id.tv_line1_content);
        mTvTuan = (TextView) view.findViewById(R.id.tv_tuan);
        mTvXiang = (TextView) view.findViewById(R.id.tv_xiang);
    }
}
