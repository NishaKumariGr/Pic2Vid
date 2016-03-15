package edu.dartmouth.nishacs.cs65project.Model;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import edu.dartmouth.nishacs.cs65project.R;


/**
 * Created by nisha on 3/7/16.
 * This class is used to create a custom dialog box used for the contents in the application
 */
public class CustomContentDialogClass extends Dialog {
    public Activity mActivity;
    public static CheckBox mScenery, mPeople, mSelfie;
    public static Button mOK, mCancel;


    public CustomContentDialogClass(Activity a) {
        super(a);
        this.mActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_content_dialog);
        mScenery = (CheckBox)findViewById(R.id.checkBox_Scenery);
        mPeople = (CheckBox)findViewById(R.id.checkBox_People);
        mSelfie = (CheckBox)findViewById(R.id.checkBox_Selfie);

        // setting listeners for OK and CANCEL button

        mOK = (Button)findViewById(R.id.btnOK);
        mCancel = (Button)findViewById(R.id.btnCancel);

        //dismissing dialog on cancel
        mCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        //dismissing dialog on cancel
        mOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }


}
