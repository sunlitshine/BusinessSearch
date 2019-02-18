package com.shansong.bussinesssearch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AppUtils {

    /**
     * Hide system keyboard
     * @param activity
     */
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one,
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    /**
     * Get Set image for the ImageView with the image url
     * @param urlStr
     * @param imageView
     */
    public static void setImageView(final String urlStr, ImageView imageView){
        if(!TextUtils.isEmpty(urlStr)){
            Picasso.get()
                    .load(urlStr)
                    .resize(50, 50)
                    .centerCrop()
                    .into(imageView);
        }
    }

    public static AlertDialog getAlertDialog(final Activity activity, final String title,
                                              final String message){
      return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }

}
