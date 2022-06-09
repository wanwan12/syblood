package com.syblood.app.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

import java.io.File;

/**
 * Android各版本的兼容方法
 * <p>
 * Created by xw on 2016-8-1.
 */
public class MethodsCompatUtil
{
    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     *
     * @param activity
     * @param enter_anim
     * @param exit_anim
     */
    @TargetApi(5)
    public static void overridePendingTransition(Activity activity, int enter_anim, int exit_anim) {
        activity.overridePendingTransition(enter_anim, exit_anim);
    }

    @TargetApi(7)
    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
        return MediaStore.Images.Thumbnails.getThumbnail(cr,origId,kind, options);
    }

    @TargetApi(8)
    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    @TargetApi(11)
    public static void recreate(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity.recreate();
        }
    }

    @TargetApi(11)
    public static void setLayerType(View view, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(layerType, paint);
        }
    }

    @TargetApi(14)
    public static void setUiOptions(Window window, int uiOptions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            window.setUiOptions(uiOptions);
        }
    }
}
