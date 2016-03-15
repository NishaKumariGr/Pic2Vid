package edu.dartmouth.nishacs.cs65project.Controller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.dartmouth.nishacs.cs65project.Model.CustomContentDialogClass;
import edu.dartmouth.nishacs.cs65project.Model.CustomDialogClass;
import edu.dartmouth.nishacs.cs65project.Model.Query;
import edu.dartmouth.nishacs.cs65project.R;


public class CreationFragment extends Fragment {


    // declare text label objects
    private TextView  mStartText, mEndText;
    private Button mCreateVideo, mStartBtn, mEndBtn, mLoctaionBtn;
    private EditText mDuration;
    private  Query mQuery =  new Query();
    private QueryController mQueryController;

    private static final Integer RADIUS = 5;

    public static final String IMAGES_FROM_QUERY="IMAGES_FROM_QUERY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_creation, container, false);

        mQueryController = new QueryController(getActivity());


        LinearLayout linear = (LinearLayout)rootView.findViewById(R.id.flipLayout);
        final CustomDialogClass cdd=new CustomDialogClass(getActivity());
        linear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cdd.show();
                Window window = cdd.getWindow();
                window.setLayout(600,900);

            }
        });

        LinearLayout linearCustom = (LinearLayout)rootView.findViewById(R.id.contentLayout);
        final CustomContentDialogClass ccd=new CustomContentDialogClass(getActivity());
        linearCustom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ccd.show();
                Window window = ccd.getWindow();
                window.setLayout(600,500);

            }
        });


        mCreateVideo = (Button) rootView.findViewById(R.id.btn_create_video);
        //setting create video button listener
        mCreateVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //settin values to the Query object
                mQuery.setmDuration(VideoActivity.INTERVAL.toString());
                // added checks here so that it performs automatic checking of non-initilaization of query element

                if (CustomContentDialogClass.mScenery != null){
                    mQuery.setmIsSceneryChecked(CustomContentDialogClass.mScenery.isChecked());
                    Log.d("CREATEFRAG","Scene check: "+CustomContentDialogClass.mScenery.isChecked());
                }

                if (CustomContentDialogClass.mPeople != null){
                    mQuery.setmIsPeopleChecked(CustomContentDialogClass.mPeople.isChecked());
                    Log.d("CREATEFRAG", "People check: " + CustomContentDialogClass.mPeople.isChecked());
                }

                if (CustomContentDialogClass.mSelfie != null){
                    mQuery.setmIsSelfieClicked(CustomContentDialogClass.mSelfie.isChecked());
                    Log.d("CREATEFRAG", "Selfie check: " + CustomContentDialogClass.mSelfie.isChecked());
                }

                Long time_start = new Long(0);
                Long time_end = Calendar.getInstance().getTimeInMillis();
                try {
                    Date date_start = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(CustomDialogClass.mStartText.getText().toString());
                    time_start = date_start.getTime();

                    Date date_end = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(CustomDialogClass.mEndText.getText().toString());
                    time_end = date_end.getTime();
                }
                catch(Exception e)
                {

                }

                mQuery.setmStartDate(time_start);
                mQuery.setmEnddate(time_end);
                mQuery.setmRadius(MapsActivity.radius);


                if (MapsActivity.mPosition != null) {
                    mQuery.setmLocation(new LatLng(MapsActivity.mPosition.latitude,
                            MapsActivity.mPosition.longitude));
                    Log.d("Creation fragemnt", MapsActivity.mPosition.latitude + "");
                    Log.d("Creation fragemnt", MapsActivity.mPosition.longitude + "");
                }
                else
                {
                    Log.d("CREATION FRAGMENT", "NO LOCATION SPECIFIED" );
                }

                   ArrayList<String> file_paths = mQueryController.HandleQuery(mQuery);

                Intent intent = new Intent(getActivity(),
                        VideoActivity.class);

                intent.putStringArrayListExtra(IMAGES_FROM_QUERY,file_paths);

                startActivity(intent);
            }
            });

    return rootView;
    }
}
