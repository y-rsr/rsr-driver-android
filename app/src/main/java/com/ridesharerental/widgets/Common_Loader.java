package com.ridesharerental.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ridesharerental.app.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;
import com.wang.avi.indicators.BallPulseIndicator;
import com.wang.avi.indicators.BallScaleMultipleIndicator;

/**
 * Created by user65 on 12/12/2017.
 */

public class Common_Loader
{
    private Context mContext;
    private Dialog dialog;
    private View view;
    private AVLoadingIndicatorView indicatorView;
    private BallScaleMultipleIndicator indicator;
    private BallPulseIndicator another_indicator;
    Indicator india;


    public Common_Loader(Context context)
    {
        this.mContext = context;
        view = View.inflate(mContext, R.layout.progress, null);
        dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        try {


           indicator=new BallScaleMultipleIndicator();
            another_indicator=new BallPulseIndicator();

            indicatorView= (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
            //indicatorView.setIndicator(indicator);
            indicatorView.setIndicator(another_indicator);


        } catch (Exception e) {

        }


        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void dismiss() {
        dialog.dismiss();
    }

    public void show() {
        try {
            if(!((Activity) mContext).isFinishing())
            {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public void secCancellable(boolean isCancel)
    {
        dialog.setCancelable(isCancel);
    }
}
