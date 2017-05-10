package com.meihuayishu.vone.modul;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.meihuayishu.vone.R;
import com.meihuayishu.vone.ui.DatePickActivity;
import com.meihuayishu.vone.ui.GuayaoChooseActivity;
import com.meihuayishu.vone.ui.MainActivity;
import com.meihuayishu.vone.ui.NumChooseActivity;

/**
 * Created by Dell on 2017-2-25.
 */
public class MenuItemClickImp implements OnMenuItemClickListener {

    private MainActivity context;

    public MenuItemClickImp(MainActivity context) {
        this.context = context;
    }

    @Override
    public void itemClick(View view, int pos) {
        switch (pos) {
            case 0://时间占卜
                Intent intent = new Intent(context, DatePickActivity.class);
                intent.setFlags(1);
                context.startActivityForResult(intent, 1);
                break;
            case 1://数字占卜
                context.startActivity(new Intent(context, NumChooseActivity.class));
                break;
            case 2://卦爻
                Log.i("MSL", "itemClick: 卦爻");
                //启动卦爻选择activity
                view.getContext().startActivity(new Intent(view.getContext(),
                        GuayaoChooseActivity.class));
                break;
            case 3://占字

//                Toast.makeText(view.getContext(),"占字",Toast.LENGTH_SHORT).show();
                break;
            default:

                break;

        }
    }


    @Override
    public void itemCenterClick(View view) {
//        Log.i("MSL", "itemCenterClick: ");
        Intent intent = new Intent(view.getContext(), DatePickActivity.class);
        intent.setFlags(0);
        context.startActivityForResult(intent, 0);
    }

    public static class ViewHolder {
        public View rootView;
        public EditText mEdit1;
        public EditText mEdit2;
        public Button mNumOk;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mEdit1 = (EditText) rootView.findViewById(R.id.edit1);
            this.mEdit2 = (EditText) rootView.findViewById(R.id.edit2);
            this.mNumOk = (Button) rootView.findViewById(R.id.num_ok);
        }

    }

    /**
     * 弹出popupwindow 选择两个自然数起卦
     */


}
