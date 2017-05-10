package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.beans.GuaBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuayaoChooseActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String[] names = new String[]{"乾1", "兑2", "离3", "震4", "巽5", "坎6", "艮7", "坤8"};
    private int[] guaxiang = new int[]{R.mipmap.xiang_1, R.mipmap.xiang_2, R.mipmap.xiang_3, R.mipmap.xiang_4,
            R.mipmap.xiang_5, R.mipmap.xiang_6, R.mipmap.xiang_7, R.mipmap.xiang_8,};
    private String[] yao = new String[]{"初爻1", "二爻2", "三爻3", "四爻4", "五爻5", "上爻6"};

    private Spinner mSpinnerShang;
    private Spinner mSpinnerXia;
    private Spinner mSpinnerYao;
    private Button mGuayaoOk;
    private Button mGuayaoCancel;

    private List<HashMap<String, Object>> list = new ArrayList<>();
    private SimpleAdapter adapter;
    private Intent intent;
    private GuaBean guaBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guayao_choose);
        initView();

        initData();

        intent = new Intent(this, ResultActivity.class);
        //默认初始上卦下挂动爻
        guaBean = new GuaBean(1, 1, 1);

        adapter = new SimpleAdapter(this, list, R.layout.item_spinner,
                new String[]{"text", "img"}, new int[]{R.id.item_tv, R.id.item_iv});

        mSpinnerShang.setAdapter(adapter);
        mSpinnerXia.setAdapter(adapter);
        mSpinnerYao.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item_center, yao){
            //让ArrayAdapter中文字居中
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }

            private View setCentered(View view)
            {
                TextView textView = (TextView)view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                return view;
            }
        });

        mSpinnerShang.setOnItemSelectedListener(this);
        mSpinnerXia.setOnItemSelectedListener(this);
        mSpinnerYao.setOnItemSelectedListener(this);
    }

    private void initData() {
        for (int i = 0; i < names.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("text", names[i]);
            map.put("img", guaxiang[i]);
            list.add(map);
        }
    }

    private void initView() {
        mSpinnerShang = (Spinner) findViewById(R.id.spinner_shang);
        mSpinnerXia = (Spinner) findViewById(R.id.spinner_xia);
        mSpinnerYao = (Spinner) findViewById(R.id.spinner_yao);
        mGuayaoOk = (Button) findViewById(R.id.guayao_ok);
        mGuayaoCancel = (Button) findViewById(R.id.guayao_cancel);
        mGuayaoOk.setOnClickListener(this);
        mGuayaoCancel.setOnClickListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i("MSL", "onItemSelected: " + parent.getId() + ",   " + position + ",  " + id);
        position ++ ;
        switch (parent.getId()) {
            case R.id.spinner_shang:
                Log.i("MSL", "onItemSelected: 上卦");
                guaBean.setShang(position);
                break;
            case R.id.spinner_xia:
                Log.i("MSL", "onItemSelected: 下卦");
                guaBean.setXia(position);
                break;
            case R.id.spinner_yao:
                Log.i("MSL", "onItemSelected: 动爻" + position);
                guaBean.setDong(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guayao_ok:
                Bundle bundle = new Bundle();
                bundle.putParcelable("gua",guaBean);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

            case R.id.guayao_cancel:
                intent = null ;
                finish();
                break;
        }
    }
}
