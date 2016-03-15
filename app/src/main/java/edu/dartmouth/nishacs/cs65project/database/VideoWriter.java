package edu.dartmouth.nishacs.cs65project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import edu.dartmouth.nishacs.cs65project.Model.LOGTags;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoObject;
import edu.dartmouth.nishacs.cs65project.Model.P2VVideoSettings;

import java.util.ArrayList;

/**
 * Created by haris on 03/03/2016.
 */
public class VideoWriter extends AsyncTask<P2VVideoObject, Long, Long> {

    private SQLiteDatabase myImageDB;
    private DatabaseHelper myDBHelper;

    public VideoWriter(Context context)
    {
        myDBHelper = new DatabaseHelper(context);
    }

    // written as public
    protected void open()
    {
        myImageDB = myDBHelper.getWritableDatabase();
    }

    protected void close()
    {
        myImageDB.close();
    }

    @Override
    protected Long doInBackground(P2VVideoObject... params) {

        P2VVideoObject cur_video = params[0];

        // write video headers
        Long video_id  = writeVideo(cur_video);
        Log.d(LOGTags.DATABASE_OPS,"VIDEO: ID:" + video_id);


        if (video_id != null) {
            cur_video.setVid_id(video_id);

            // write video data
            writeVideoData(cur_video);

            // write video settings
            writeVideoSettings(cur_video);

            return video_id;
        }
        else {
            return new Long(-1);

        }
    }


    private void writeVideoSettings(P2VVideoObject a_video)
    {
        Long video_id = a_video.getVid_id();
        P2VVideoSettings vid_settings = a_video.getVideo_settings();

        ContentValues new_entry = new ContentValues();
        new_entry.put(DatabaseHelper.VIDEOSETTINGS_COL_VIDID, video_id);
        if ( vid_settings.getAudioTrackAvailable()==true )
            new_entry.put(DatabaseHelper.VIDEOSETTINGS_COL_AUDIOAVAILABLE, 1 );
        else
            new_entry.put(DatabaseHelper.VIDEOSETTINGS_COL_AUDIOAVAILABLE, 0 );

        new_entry.put(DatabaseHelper.VIDEOSETTINGS_COL_AUDIOTRACK, vid_settings.getAudioTrackLocation());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: SETTINGS: AUDIO TRACK:" + vid_settings.getAudioTrackLocation());


        new_entry.put(DatabaseHelper.VIDEOSETTINGS_COL_FRAMERATE, vid_settings.getFrameRate());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: SETTINGS: FRAME RATE:" + vid_settings.getFrameRate());

        open();
        myImageDB.insert(DatabaseHelper.TABLE_VIDEOSETTINGS, null, new_entry);
        close();

    }

    private Long writeVideo(P2VVideoObject a_video)
    {
        ContentValues new_entry = new ContentValues();

        new_entry.put(DatabaseHelper.VIDEOSCREATED_COL_AUTHOR, a_video.getAuthor());

        Log.d(LOGTags.DATABASE_OPS, "VIDEO: AUTHOR:" + a_video.getAuthor());

        new_entry.put(DatabaseHelper.VIDEOSCREATED_COL_DATECREATED, a_video.getDateCreated());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: CREATED:" + a_video.getDateCreated());

        new_entry.put(DatabaseHelper.VIDEOSCREATED_COL_DATELASTEDITED, a_video.getDateLastEdited());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: LAST EDITED:" + a_video.getDateLastEdited());

        new_entry.put(DatabaseHelper.VIDEOSCREATED_COL_TITLE, a_video.getVideoTitle());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: TITLE:" + a_video.getVideoTitle());

        new_entry.put(DatabaseHelper.VIDEOSCREATED_COL_TOTALIMAGESUSED, a_video.getTotalImagesUsed());
        Log.d(LOGTags.DATABASE_OPS, "VIDEO: IMAGE COUNT:" + a_video.getTotalImagesUsed());

        open();
        Long video_id = myImageDB.insert(DatabaseHelper.TABLE_VIDEOSCREATED, null, new_entry);
        close();

        return video_id;

    }

    private void writeVideoData(P2VVideoObject a_video)
    {
        Integer total_files = a_video.getTotalImagesUsed();
        Long video_id = a_video.getVid_id();
        ArrayList<String> file_paths = a_video.getFile_paths();

        open();
        for (int an_img =0; an_img < total_files; an_img++ )
        {
            ContentValues new_entry = new ContentValues();

            new_entry.put(DatabaseHelper.VIDEODATA_COL_VIDID, video_id);
            new_entry.put(DatabaseHelper.VIDEODATA_COL_IMGLOCATIONONDISK, file_paths.get(an_img));
            new_entry.put(DatabaseHelper.VIDEODATA_COL_LOCATIONINVIDEO, an_img);

            Log.d(LOGTags.DATABASE_OPS, "VIDEO DATA: ID:" + video_id + " loc: " + an_img + " path: " + file_paths.get(an_img));


            myImageDB.insert(DatabaseHelper.TABLE_VIDEODATA, null, new_entry);


        }
        close();


    }


}

