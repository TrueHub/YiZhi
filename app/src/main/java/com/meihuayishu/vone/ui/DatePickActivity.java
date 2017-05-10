package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017-2-26.
 */
public class DatePickActivity extends Activity implements View.OnClickListener {
    private View repairDateOther;
    private Button repairDateSelCancel;
    private Button repairDateSelOk;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private String datestrold;
    private String datestr;
    private TextView mTextView;
    private boolean isBaziChooser = false;
    private Switch mSwitchMaleBazi;
    private String sex = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flags = getIntent().getFlags();
        if (flags == 1) {
            isBaziChooser = false;
            setContentView(R.layout.activity_datepicker);
        } else {
            isBaziChooser = true;//八字选择界面
            setContentView(R.layout.activity_bazi_info);

            getSex();

        }

        initView();

        init();

    }

    private void getSex() {
        mSwitchMaleBazi = (Switch) findViewById(R.id.switch_male_bazi);
        mSwitchMaleBazi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sex = "女";
                }else {
                    sex = "男";
                }
//                Log.e("MSL", "onCheckedChanged: " + sex);
            }
        });
    }

    private void init() {
        datePicker.setCalendarViewShown(false);
        timePicker.setIs24HourView(true);
        resizePikcer(datePicker);// 调整datepicker大小
        resizePikcer(timePicker);// 调整timepicker大小
        String str = getIntent().getStringExtra("date");
        int flags = getIntent().getFlags();
        if (flags == 1) {
            mTextView.setText("以时间起卦");
        }

        if (TextUtils.isEmpty(str)) {
//            System.out.println("isempty");
            datestrold = "";
            datestr = "";
        } else {
            datestr = str;
            datestrold = str;
        }
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(this, 45),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Util.dip2px(this, 5), 0, Util.dip2px(this, 5), 0);
        np.setLayoutParams(params);

    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }


    private String getDate() {
        StringBuilder str = new StringBuilder().append(datePicker.getYear())
                .append("-")
                .append((datePicker.getMonth() + 1) < 10 ? "0" + (datePicker.getMonth() + 1)
                        : (datePicker.getMonth() + 1))
                .append("-")
                .append((datePicker.getDayOfMonth() < 10) ? "0" + datePicker.getDayOfMonth()
                        : datePicker.getDayOfMonth())
                .append(" ")
                .append((timePicker.getCurrentHour() < 10) ? "0" + timePicker.getCurrentHour()
                        : timePicker.getCurrentHour())
                .append(":").append((timePicker.getCurrentMinute() < 10) ? "0" + timePicker.getCurrentMinute()
                        : timePicker.getCurrentMinute());

        return str.toString();
    }


    private void initView() {
        repairDateOther = findViewById(R.id.repair_date_other);
        repairDateSelCancel = (Button) findViewById(R.id.repair_date_sel_cancel);
        repairDateSelOk = (Button) findViewById(R.id.repair_date_sel_ok);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);

        repairDateSelCancel.setOnClickListener(this);
        repairDateSelOk.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_date_sel_cancel:
                back(true);
                break;
            case R.id.repair_date_sel_ok:
                back(false);
                break;
            case R.id.repair_date_other:
                back(false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back(true);
        super.onBackPressed();
    }

    /**
     * 关闭调用 old为true则不变，false则改变
     *
     * @param old 是否不变
     */
    private void back(boolean old) {
        // 获取时间选择
        Intent intent = new Intent();
        if (old) {
            intent.putExtra("date", datestrold);
        } else {
            datestr = getDate();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            Date date = null;
//            try {
//                date = sdf.parse(datestr);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
////            if (!compare(date))
////                return;
            if (isBaziChooser) {
                intent.putExtra("sex",sex);
            }
            intent.putExtra("isBazi",isBaziChooser);
            intent.putExtra("date", datestr);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

//    /**
//     *
//     * @param datestr "yyyy-MM-dd HH:mm"
//     * @return
//     */
//    public Date getDate(String datestr) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date date = null;
//        try {
//            date = sdf.parse(datestr);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
}
