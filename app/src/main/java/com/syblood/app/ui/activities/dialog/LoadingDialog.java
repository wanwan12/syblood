package com.syblood.app.ui.activities.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.syblood.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 表示当前正在处理中的dialog
 * <p>
 * Created by cjj on 2016-7-19.
 */
public class LoadingDialog extends Dialog
{
//    private Context mContext;

    @BindView(R.id.imgV_loading)
    ImageView imgV_loading;

    /**
     * 构造函数
     *
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, R.style.DialogBaseStyle_Loading);
//        this.mContext = context;
        this.setContentView(R.layout.lib_loading_dialog_layout);
        ButterKnife.bind(this);
        rotateAnim(context);
        this.setCancelable(false);
    }

    /**
     * 旋转动画
     */
    private void rotateAnim(Context context) {
        Animation rotateAnim = AnimationUtils.loadAnimation(context, R.anim.lib_rotate_anim);
        rotateAnim.setInterpolator(new LinearInterpolator());
        imgV_loading.startAnimation(rotateAnim);
    }


}
