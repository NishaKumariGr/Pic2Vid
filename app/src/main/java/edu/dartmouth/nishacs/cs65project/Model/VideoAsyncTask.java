package edu.dartmouth.nishacs.cs65project.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import edu.dartmouth.nishacs.cs65project.Controller.VideoActivity;

/**
 * Created by nisha on 2/26/16.
 */
public class VideoAsyncTask extends AsyncTask<String, Void, Void> {
    Context context;

    public VideoAsyncTask(Context context)
    {
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Pre execute", "Starting the animation");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {

        VideoActivity.mAnimation.start();
        return null;
    }
}

