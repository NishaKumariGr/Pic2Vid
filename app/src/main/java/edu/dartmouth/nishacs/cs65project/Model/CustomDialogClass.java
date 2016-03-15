package edu.dartmouth.nishacs.cs65project.Model;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.dartmouth.nishacs.cs65project.R;

/**
 * Created by nisha on 3/6/16.
 *  This class is used tocreate a custom dialog box used for choosing the start and end dates
 */
public class CustomDialogClass extends Dialog {

    public Activity mActivity;
    public Dialog mDialog;
    public Button mStart, mEnd, mCancel, mOK;
    private Calendar mCalendar = Calendar.getInstance();
    public static TextView mStartText, mEndText;


    public CustomDialogClass(Activity a) {
        super(a);
        this.mActivity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_calender_dialog);
        mStart = (Button) findViewById(R.id.btnStart);
        mEnd = (Button) findViewById(R.id.btnEnd);
        mOK = (Button) findViewById(R.id.btnOK);
        mCancel = (Button) findViewById(R.id.btnCancel);



        //setting the editText and its listener to pop up a date picker dialog
           mStartText = (TextView)findViewById(R.id.txtStart);
        //Setting initial input type as null. We will display the date dialog
         mStart.setInputType(InputType.TYPE_NULL);
         mEndText = (TextView)findViewById(R.id.txtEnd);
        //Setting initial input type as null. We will display the date dialog
         mEnd.setInputType(InputType.TYPE_NULL);

          setCalenderDialog(mStart, mStartText);
           setCalenderDialog(mEnd, mEndText);

        //dismissing dialog on cancel
        mOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        //dismissing dialog on cancel
        mCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }


     private void setCalenderDialog(Button currentButton, TextView currentText) {

       final Button currentBtn = currentButton;
       final TextView currentTxt = currentText;

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(currentTxt);
            }

        };
        currentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mActivity, date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(TextView textView) {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(mCalendar.getTime()));

    }
}