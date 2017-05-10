package com.meihuayishu.vone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.beans.GuaBean;

public class NumChooseActivity extends Activity implements View.OnClickListener {

    private EditText mEdit1;
    private EditText mEdit2;
    private Button mNumOk;
    private int edit1String = -1, edit2String = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_choose);
        initView();
    }

    private void initView() {
        mEdit1 = (EditText) findViewById(R.id.edit1);
        mEdit2 = (EditText) findViewById(R.id.edit2);
        mNumOk = (Button) findViewById(R.id.num_ok);

        mNumOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num_ok:
                submit();
                if (-1 == edit1String || -1 == edit2String) {
                    return;
                }
                GuaBean guaBean = new GuaBean();
                guaBean.setShang(edit1String % 8);
                guaBean.setXia(edit2String % 8);
                guaBean.setDong((edit1String + edit2String) % 6);

                if (guaBean.getShang() == 0 ) {
                    guaBean.setShang(8);
                }
                if (guaBean.getXia() == 0) {
                    guaBean.setXia(8);
                }
                if (0 == guaBean.getDong()) {
                    guaBean.setDong(6);
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("gua", guaBean);
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        if ((mEdit1.getText().toString().trim().isEmpty())) {
            Toast.makeText(this, "第一个数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        edit1String = Integer.valueOf(mEdit1.getText().toString().trim());

        if (mEdit2.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "第二个数也不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        edit2String = Integer.valueOf(mEdit2.getText().toString().trim());

        // TODO validate success, do something


    }
}
