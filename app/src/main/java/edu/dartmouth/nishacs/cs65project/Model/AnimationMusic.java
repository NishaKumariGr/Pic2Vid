package edu.dartmouth.nishacs.cs65project.Model;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by nisha on 2/27/16.
 * This class handles the sounds during animation of images
 */
public class AnimationMusic {
    MediaPlayer mSound;

    public AnimationMusic(Context context, int id) {

       // Creates an instance of the mediaplayer
       mSound = MediaPlayer.create(context, id);
    }

    // This method plays the sound in loop, until we choose to stop it by calling stopMusic.
    public void startMusic() {
        mSound.start();
        mSound.setLooping(true);
    }

    // Stops the music when called

    public void stopsound() {
        if (mSound != null) {
            if (mSound.isPlaying()) {
                mSound.stop();
                mSound.setLooping(false);
            }
        }

        //Clear the mediaPlayer object when we no longer deal with music
       // mSound.release();
        mSound = null;
    }

}
