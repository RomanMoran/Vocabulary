package com.example.roman.vocabulary.utilities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.roman.vocabulary.R;

import butterknife.ButterKnife;

/**
 * Created by roman on 01.09.2017.
 */

public class DialogUtility {

    public static Dialog getWaitDialog(Context context, String text, boolean isShow){
        return getWaitDialog(context,text,isShow,false);
    }

    private static Dialog getWaitDialog(Context context, String text, boolean isShow, boolean cancelable) {
        Dialog dialog = new Dialog(context, R.style.vocabularyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCancelable(cancelable);

        TextView textView = ButterKnife.findById(dialog,R.id.tvProgressBar);

        if (text != null && text.length() != 0){
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }else {
            textView.setVisibility(View.GONE);
        }
        try{
            if (isShow){
                try {
                    dialog.show();
                }catch (Exception ignored){

                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return dialog;
    }

    public static void closeDialog(Dialog dialog){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
