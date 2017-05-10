package com.meihuayishu.vone.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.meihuayishu.vone.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Dell on 2017-3-29.
 */
public class ScreenShot {
    //截取当前屏幕
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        b = anyRatioCompressing(b, (float) 4 / 5, (float) 4 / 5);

        view.destroyDrawingCache();
//        Log.i("MSL", "takeScreenShot: bitmap  " + b.getByteCount());
        return b;
    }

    //截取ScrollView
    public static Bitmap getBitmapByView(final ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#e0edd1b0"));
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();

            final int finalI = i;
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
//        bitmap = drawBg4Bitmap(Color.parseColor("#e0edd1b0"),bitmap);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        scrollView.draw(canvas);

        bitmap = anyRatioCompressing(bitmap, (float) 4 / 5, (float) 4 / 5);
        return bitmap;
    }

    //改变bitmap的背景色
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }

    // 保存到sdcard
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
            }
                fos.close();
//            Log.i("MSL", "savePic: OK " + strFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 程序入口1 截取当前屏幕
    public static void shootLoacleView(Activity a, String picpath) {
        ScreenShot.savePic(takeScreenShot(a), picpath);
    }

    // 程序入口2 截取当前屏幕
    public static void shootScrollView(ScrollView s, String picpath) {
        ScreenShot.savePic(getBitmapByView(s), picpath);
    }

    /**
     * 将传入的Bitmap合理压缩后输出到系统截屏目录下
     * 命名格式为：Screenshot+时间戳+启信宝报名.jpg
     * 同时通知系统重新扫描系统文件
     */
/*
    public static void savingBitmapIntoFile(final Bitmap pic, final String strFileName,
                                            final BitmapAndFileCallBack callBack) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileReturnPath = "";
                // 获取内存路径
                // 设置图片路径+命名规范
                // 声明输出文件
                String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileTitle = "Screenshot_" + System.currentTimeMillis() + "_com.bertadata.qxb.biz_info.jpg";
                String filePath = storagePath + "/DCIM/";
                final String fileAbsolutePath = filePath + fileTitle;
                File file = new File(fileAbsolutePath);

                */
/**
                 * 质压与比压结合
                 * 分级压缩
                 * 输出文件
                 *//*

                if (pic != null) {
                    try {
                        // 首先，对原图进行一步质量压缩,形成初步文件
                        FileOutputStream fos = new FileOutputStream(file);
                        pic.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                        // 另建一个文件other_file预备输出
                        String other_fileTitle = "Screenshot_" + System.currentTimeMillis() + "_com.bertadata.qxb.jpg";
                        String other_fileAbsolutePath = filePath + other_fileTitle;
                        File other_file = new File(other_fileAbsolutePath);
                        FileOutputStream other_fos = new FileOutputStream(other_file);

                        // 其次，要判断质压之后的文件大小，按文件大小分级进行处理
                        long file_size = file.length() / 1024; // size of file(KB)
                        if (file_size < 0 || !(file.exists())) {
                            // 零级： 文件判空
                            throw new NullPointerException();
                        } else if (file_size > 0 && file_size <= 256) {
                            // 一级： 直接输出
                            deleteFile(other_file);
                            // 通知刷新文件系统，显示最新截取的图文件
                            fileReturnPath = fileAbsolutePath;
                        } else if (file_size > 256 && file_size <= 768) {
                            // 二级： 简单压缩:压缩为原比例的3/4，质压为50%
                            anyRatioCompressing(pic, (float) 3 / 4, (float) 3 / 4).compress(Bitmap.CompressFormat.JPEG, 40, other_fos);
                            deleteFile(file);
                            // 通知刷新文件系统，显示最新截取的图文件
                            fileReturnPath = other_fileAbsolutePath;
                        } else if (file_size > 768 && file_size <= 1280) {
                            // 三级： 中度压缩：压缩为原比例的1/2，质压为40%
                            anyRatioCompressing(pic, (float) 1 / 2, (float) 1 / 2).compress(Bitmap.CompressFormat.JPEG, 40, other_fos);
                            deleteFile(file);
                            // 通知刷新文件系统，显示最新截取的图文件
                            fileReturnPath = other_fileAbsolutePath;
                        } else if (file_size > 1280 && file_size <= 2048) {
                            // 四级： 大幅压缩：压缩为原比例的1/3，质压为40%
                            anyRatioCompressing(pic, (float) 1 / 3, (float) 1 / 3).compress(Bitmap.CompressFormat.JPEG, 40, other_fos);
                            deleteFile(file);
                            // 通知刷新文件系统，显示最新截取的图文件
                            fileReturnPath = other_fileAbsolutePath;
                        } else if (file_size > 2048) {
                            // 五级： 中度压缩：压缩为原比例的1/2，质压为40%
                            anyRatioCompressing(pic, (float) 1 / 2, (float) 1 / 2).compress(Bitmap.CompressFormat.JPEG, 40, other_fos);
                            deleteFile(file);
                            // 通知刷新文件系统，显示最新截取的图文件
                            fileReturnPath = other_fileAbsolutePath;
                        }

                        // 注销fos;
                        fos.flush();
                        other_fos.flush();
                        other_fos.close();
                        fos.close();
                        //callback用于回传保存成功的路径以及Bitmap
                        callBack.onSuccess(pic, fileReturnPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else callBack.onSuccess(null, "");
            }
        });
        thread.start();
    }
*/

    /**
     * 可实现任意宽高比例压缩（宽高压比可不同）的压缩方法（主要用于微压）
     *
     * @param bitmap       源图
     * @param width_ratio  宽压比（float）（0<&&<1)
     * @param height_ratio 高压比（float）（0<&&<1)
     * @return 目标图片
     * <p>
     */
    public static Bitmap anyRatioCompressing(Bitmap bitmap, float width_ratio, float height_ratio) {
        Matrix matrix = new Matrix();
        matrix.postScale(width_ratio, height_ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

/*    public static boolean deleteFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()){
            return false;
        }
        file.delete();
        return false;
    }*/
}
