package com.yshare.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtils {

    /**
     * 固定宽高缩放图片
     *
     * @param primitive 原始Bitmap
     * @param newW      新的宽
     * @param newH      新的高
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap primitive, int newW, int newH) {

        if (primitive == null) {
            return null;
        }
        int w = primitive.getWidth();
        int h = primitive.getHeight();
        float scaleW = (float) newW / w;
        float scaleH = (float) newH / h;

        Matrix matrix = new Matrix();
//        matrix.postScale(scaleW, scaleH);
        matrix.preScale(scaleW, scaleH);
        Bitmap scaleBitmap = Bitmap.createBitmap(primitive, 0, 0, w, h, matrix, false);
        if (!primitive.isRecycled()) {
            primitive.recycle();
        }
        return scaleBitmap;
    }




    /**
     * 通过比例缩放缩放图片
     *
     * @param primitive    原始Bitmap
     * @param inSampleSize 采样率
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap primitive, int inSampleSize) {
        if (primitive == null) {
            return null;
        }
        int w = primitive.getWidth();
        int h = primitive.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(inSampleSize, inSampleSize);
        Bitmap scaleBitmap = Bitmap.createBitmap(primitive, 0, 0, w, h, matrix, false);
        if (!primitive.isRecycled()) {
            primitive.recycle();
        }
        return scaleBitmap;
    }

/**
 *
 */
    /**
     *根据View的大小缩放Bitmap的方法
     * BitmapFactory.Options介绍
     * BitmapFactory提供了多种方式根据不同的图片源来创建Bitmap对象：
     *         （1）Bitmap BitmapFactory.decodeFile(String pathName, Options opts)： 读取SD卡上的图片。
     *         （2）Bitmap BitmapFactory.decodeResource(Resources res, int id, Options opts) ： 读取网络上的图片。
     *         （3）Bitmap BitmapFactory.decodeResource(Resources res, int id, Options opts) ： 读取资源文件中的图片。
     *     这三个函数默认会直接为bitmap分配内存，我们通过第二个参数Options来控制，让它不分配内存，同时可以得到图片的相关信息。具体参考：
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }
    /**
     * 计算图片压缩比例（采样率）
     * 通常如果图片是针对某种分辨率设计的，直接decode图片不会有什么问题。但是，如果图片不是特定设计，且比较大的话，容易造成OOM。
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 计算图片压缩比例（采样率）
     * inSampleSize只能是2的整数次幂，如果不是的话，向下取得最大的2的整数次幂。因为按照2的次方进行压缩会比较高效和方便。 方法一计算出来的inSampleSize值可能不是2的整数次幂
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
