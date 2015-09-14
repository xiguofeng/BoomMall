
package com.plmt.boommall.utils.image;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {

    /**
     * png后缀
     */
    public static final String PNG_SUFFIX = ".png";

    /**
     * jpg后缀
     */
    public static final String JPG_SUFFIX = ".jpg";

    /**
     * jpeg后缀
     */
    public static final String JPEG_SUFFIX = ".jpeg";



    public static String getFileSuffix(String pathName) {
        String suffix = "";

        if (null != pathName) {
            int lastIndexOf = pathName.lastIndexOf(".");
            if (-1 != lastIndexOf) {
                suffix = pathName.substring(lastIndexOf);
            }
        }

        return suffix;
    }

    public static Bitmap getBitmapByAssets(Context context, String fileName,
            int maxWidth, int maxHeight) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, opts);

            // close stream.
            is.close();

            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int destWidth;
            int destHeight;

            // 缩放的比例
            int ratio;

            // 按比例计算缩放后的图片大小
            int widthRatio = srcWidth / maxWidth;
            int heightRatio = srcHeight / maxHeight;
            ratio = Math.max(widthRatio, heightRatio);

            destWidth = (ratio < 1) ? srcWidth : (srcWidth / ratio);
            destHeight = (ratio < 1) ? srcHeight : (srcHeight / ratio);

            // 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
            BitmapFactory.Options newOpts = new BitmapFactory.Options();

            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = 1;

            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;

            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;

            // re-get input stream !
            is = context.getAssets().open(fileName);
            Bitmap destBitmap = BitmapFactory.decodeStream(is, null, newOpts);

            return destBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
