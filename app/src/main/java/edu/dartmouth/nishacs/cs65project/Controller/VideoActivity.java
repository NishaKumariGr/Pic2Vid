package edu.dartmouth.nishacs.cs65project.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

import edu.dartmouth.nishacs.cs65project.Model.AnimationMusic;
import edu.dartmouth.nishacs.cs65project.Model.LOGTags;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoObject;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoSettings;
import edu.dartmouth.nishacs.cs65project.R;
import edu.dartmouth.nishacs.cs65project.Model.Video;
import edu.dartmouth.nishacs.cs65project.Model.VideoAsyncTask;
import edu.dartmouth.nishacs.cs65project.Model.VideoSingleton;
import edu.dartmouth.nishacs.cs65project.database.VideoWriter;

/*
* Created by Nisha
* This class encapsuolates all the functionality reqired by the video such as setting music, animation object,
* interval between images etc. Images are resized before being used in video to prevent latency in loading the i
* images
* */

public class VideoActivity extends AppCompatActivity {


    public static AnimationDrawable mAnimation;
    private AnimationMusic mMusic;
    private VideoAsyncTask mVideoAsyncTask;
    public static final Integer  INTERVAL = 2500;
    private boolean mVideoStarted = false;
    private static VideoSingleton mVideoSingleton = VideoSingleton.getInstance();
    private static Context mContext;
    private QueryController mQueryController;

    private final Integer DEFAULT_SOUND = R.raw.default_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //changing the color of status bar
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.colorActionBar));

        mContext = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_video_toolbar);
        setSupportActionBar(myToolbar);

        mQueryController = new QueryController(this);

        Intent launching_intent = getIntent();

        Integer sound_content = DEFAULT_SOUND;
        Integer custom_interval = INTERVAL;
        ArrayList<String> file_paths;

        if ( launching_intent.getLongExtra(HistoryFragment.LAUNCH_VIDEO,-1) != -1 )
        {
            P2VVideoObject to_load = mQueryController.getVideoById(launching_intent.getLongExtra(HistoryFragment.LAUNCH_VIDEO, -1));
            file_paths = to_load.getFile_paths();
            P2VVideoSettings vid_settings = to_load.getVideo_settings();

            custom_interval = vid_settings.getFrameRate();
            sound_content = Integer.parseInt( vid_settings.getAudioTrackLocation() );

        }
        else {
            file_paths = launching_intent.getStringArrayListExtra(CreationFragment.IMAGES_FROM_QUERY);
            if (file_paths != null) {
                Log.d(LOGTags.VIDEO_ACTIVITY, "Got a list of images which contains: " + file_paths.size());
            }

        }

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        Button btnSave = (Button) findViewById(R.id.btn_save);

        //setting the listener for the save button

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveToVideo(v);
                finish();
            }
        });


        final ImageView imgView = (ImageView)findViewById(R.id.img);

        mVideoSingleton.mVideo = new Video(file_paths, custom_interval, sound_content, "Your New Video");

        /*-----------Setting the listeners---------------------------------------*/

        // set the listener for the Start button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mVideoStarted) {
                    mVideoStarted = true;
                    // calling the mthod to start the animation
                    startAnimation();
                }
            }
        });

        // set the listener for the Stop button
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoStarted) {
                    mMusic.stopsound();
                    mAnimation.stop();
                    mVideoStarted = false;
                }
            }
        });


        //setting the image listener, does nothing as of now.
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static void saveToVideo(View view) {

        VideoWriter videoWriter = new VideoWriter(mContext);

        P2VVideoObject cur_video = new P2VVideoObject();

        cur_video.setAuthor("P2VApp");
        cur_video.setFile_paths(mVideoSingleton.mVideo.getSelectedImageList());
        cur_video.setDateCreated(new Long(Calendar.getInstance().getTimeInMillis()).toString());
        cur_video.setDateLastEdited(cur_video.getDateCreated());
        cur_video.setVideoTitle(mVideoSingleton.mVideo.name);

        P2VVideoSettings vid_settings = new P2VVideoSettings();
        vid_settings.setFrameRate(mVideoSingleton.mVideo.mInterval);

        if ( mVideoSingleton.mVideo.audio_file_path_ID == -1 )
        {
            vid_settings.setAudioTrackLocation(null);
            vid_settings.setAudioTrackAvailable(false);
        }
        else
        {
            vid_settings.setAudioTrackLocation(mVideoSingleton.mVideo.audio_file_path_ID.toString());
            vid_settings.setAudioTrackAvailable(true);
        }

        cur_video.setVideo_settings(vid_settings);

        videoWriter.execute(cur_video);
        
    }

    private Bitmap ResizeImage(Bitmap cur_bitmap)
    {
        Bitmap resizedBitmap = null;
        int originalWidth = cur_bitmap.getWidth();
        int originalHeight = cur_bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        if(originalHeight > originalWidth) {
            newHeight = 480;
            multFactor = (float) originalWidth/(float) originalHeight;
            newWidth = (int) (newHeight*multFactor);
        } else if(originalWidth > originalHeight) {
            newWidth = 480;
            multFactor = (float) originalHeight/ (float)originalWidth;
            newHeight = (int) (newWidth*multFactor);
        } else if(originalHeight == originalWidth) {
            newHeight = 480;
            newWidth = 480;
        }
        resizedBitmap = Bitmap.createScaledBitmap(cur_bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }


    // Function that starts the animation. It takes an object of class Animation
    private void startAnimation(){
        mAnimation = new AnimationDrawable();

        ArrayList<String> selectedImageList = mVideoSingleton.mVideo.getSelectedImageList();

        int size = selectedImageList.size();
        int interval = mVideoSingleton.mVideo.mInterval;

        //set the frames depending on the number of images in the arrayList
        for(int i=0;i<size;i++)
        {
            //int temp_id = getResources().getIdentifier(animate.mImageList.get(i), "drawable", getPackageName());
            //Drawable drawable = getResources().getDrawable(temp_id);

            Bitmap cur_bmp = BitmapFactory.decodeFile(selectedImageList.get(i));
            Drawable drawable = new BitmapDrawable(getResources(), ResizeImage(cur_bmp));

            mAnimation.addFrame(drawable, interval);
        }

        mMusic = new AnimationMusic(this, mVideoSingleton.mVideo.audio_file_path_ID);
        mMusic.startMusic();


        ImageView imageView = (ImageView) findViewById(R.id.img);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(580, 580);
        params.alignWithParent = true;
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        imageView.setLayoutParams(params);
        imageView.setImageDrawable(mAnimation);

        mVideoAsyncTask = new VideoAsyncTask(this);

        // calling the async task to start the animation
        mVideoAsyncTask.execute("execute async task");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            // stop the music if it started while playing video
            if(mVideoStarted==true)
                mMusic.stopsound();
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void onEditClicked(View view) {
        if (mVideoStarted == true) {
            mMusic.stopsound();
            mAnimation.stop();
        }

        Intent intent = new Intent(this, EditActivity.class);

        startActivity(intent);
    }

}
