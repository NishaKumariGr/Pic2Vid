package edu.dartmouth.nishacs.cs65project.Model;

/**
 * Created by pauldonnelly on 3/5/16.
 */
public class VideoSingleton {

    private static VideoSingleton videoSingleton = new VideoSingleton( );
    public static Video mVideo;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private VideoSingleton(){ }

    /* Static 'instance' method */
    public static VideoSingleton getInstance( ) {
        return videoSingleton;
    }

}
