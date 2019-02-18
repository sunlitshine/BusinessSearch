package com.shansong.bussinesssearch.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.shansong.bussinesssearch.R;


public class BusinessProgressBar extends ProgressBar {

    public BusinessProgressBar(Context context) {
        super(context);
    }

    public BusinessProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BusinessProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawableProgress = DrawableCompat.wrap(getIndeterminateDrawable());
            DrawableCompat.setTint(drawableProgress, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));
        } else {
            getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        }
    }
}
