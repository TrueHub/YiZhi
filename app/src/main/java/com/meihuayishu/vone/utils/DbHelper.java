package com.meihuayishu.vone.utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.meihuayishu.vone.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dell on 2017-2-28.
 */
public class DbHelper extends SQLiteOpenHelper {

    private Context context;


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DbHelper(Context context, String name) {
        this(context, name, 1);
        this.context = context;

        /*File dbFile = new File(context.getFilesDir() + name);
        if (dbFile.exists()) {
            Log.i("MSL", "DbHelper: db文件已经存在" + dbFile.getName());
        } else {
        }*/
            copyDbFile(context,name);
    }

//    private void writeDb(String name) {
//
//        dbDir = context.getFilesDir().getName() + name;
//
//        Log.i("MSL", "writeDb: " + context.getFilesDir() + "------" + name);
//
//        FileOutputStream fos;
//        InputStream is;
//        try {
//            is = context.getResources().openRawResource(R.raw.jiegua);
//            fos = new FileOutputStream(new File(dbDir));
//            byte[] buffer = new byte[8912];
//            int len;
//            while ((len = is.read(buffer)) >0 ) {
//                fos.write(buffer, 0, len);
//            }
//            buffer = null ;
//            fos.flush();
//            fos.close();
//            is.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 将assets文件夹下/db的本地库拷贝到/data/data/下
     *
     * @param context
     * @param tab_name
     */
    public static void copyDbFile(Context context, String tab_name) {
        InputStream in = null;
        FileOutputStream out = null;
        /**data/data/路径*/
        String path = "/data/data/" + context.getPackageName() + "/databases";
        File file = new File(path,"jiegua.db");
        try {
            //创建文件夹
            File file_ = new File(path);
            if (!file_.exists())
                file_.mkdirs();
            if (file.exists())//删除已经存在的
                file.deleteOnExit();
            //创建新的文件
            if (!file.exists())
                file.createNewFile();
//            in = context.getAssets().open( tab_name); // 从assets目录下复制
            in = context.getResources().openRawResource(R.raw.jiegua);
            out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[8912];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
