package edu.dartmouth.nishacs.cs65project.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

import edu.dartmouth.nishacs.cs65project.Model.LOGTags;
import edu.dartmouth.nishacs.cs65project.Model.FaceData;
import edu.dartmouth.nishacs.cs65project.Model.FaceObject;
import edu.dartmouth.nishacs.cs65project.Model.ImageData;
import edu.dartmouth.nishacs.cs65project.Model.SafeFaceDetector;
import edu.dartmouth.nishacs.cs65project.database.DatabaseHandler;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by haris on 27/02/2016.
 */

/* This class is created for indexing all of the images
    Indexing entails
    1- Finding all images located in key locations of your phone
    2- Querying your DB for existence of index-record
    3- Performing analysis on Images
    4- Adding all details to DB

    // doInBackground does the searching of each location for images
    // First Argument is an ArrayList of strings providing locations you have to search for
    // Searching is recursive, and is done in all subdirectories

    // OnProgressUpdate tells how many locations have been scanned
    // Integer indicates which location is being scanned


*/

public class IndexingThread extends AsyncTask< String[] ,Integer,Integer> {

    private final int COLUMN_INDEX_NAME= 1;
    private final int COLUMN_INDEX_ID = 0;

    Context mContext;
    ArrayList<Integer> counter_new_images;

    FaceDetector mDetector;
    Detector<Face> mSafeDetector;

    DatabaseHandler mDBHandler;

    public IndexingThread(Context context)
    {
        super();
        mContext = context;

        mDetector = new FaceDetector.Builder(mContext)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // This is a temporary workaround for a bug in the face detector with respect to operating
        // on very small images.  This will be fixed in a future release.  But in the near term, use
        // of the SafeFaceDetector class will patch the issue.
        mSafeDetector = new SafeFaceDetector(mDetector);

        mDBHandler = new DatabaseHandler(mContext);

    }

    protected void onPreExecute()
    {
        // set notification bar over here


    }

    private ImageData readExifParameters(ExifInterface exifInterface, String image_path)
    {
        ImageData img_data = new ImageData();
        img_data.setImg_location(image_path);

        String tag_datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        if (tag_datetime != null) {
            tag_datetime = tag_datetime.substring(0,11);
            try {
                Date date = new SimpleDateFormat("yyyy:MM:dd", Locale.ENGLISH).parse(tag_datetime);
                Log.d(LOGTags.INDEX_THREAD_PROGRESS,"I_TAG_DATETIME: Original: " + tag_datetime + " Parsed: "+ date.toString());
                // set the date time
                img_data.setTag_date_time_captured(date.getTime());
            }
            catch ( Exception e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            img_data.setTag_date_time_captured(new Long(0));
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_DATETIME: " + img_data.getTag_date_time_captured());

        String tag_exposure_time = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
        if (tag_exposure_time != null) {
            img_data.setTag_exposure_time(Double.parseDouble(exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)));
        }
            Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_EXPOSURETIME: " + img_data.getTag_exposure_time());

        String focal_length = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
        if (focal_length != null ) {
            String[] fl_params = focal_length.split("/");
            Double numerical_focal_length = Double.parseDouble(fl_params[0]) / Double.parseDouble(fl_params[1]);
            img_data.setTag_focal_length(numerical_focal_length);
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_FOCALLENGTH: " + img_data.getTag_focal_length());

        String tag_flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
        if (tag_flash != null) {
            img_data.setTag_flash(Long.parseLong(tag_flash));
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_FLASH: " + img_data.getTag_flash());

        String gps_altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
        String gps_latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String gps_longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String gps_provider = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
        if ( gps_altitude != null)
        {
            img_data.setTag_gps_latitude(convertToDegree(gps_altitude));

        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_GPSALTITUDE: " + img_data.getTag_gps_altitude());

        if (gps_latitude != null)
        {
            if ( exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF) .equals("N") )
            {
                img_data.setTag_gps_latitude(convertToDegree(gps_latitude));
            }
            else
            {
                img_data.setTag_gps_latitude( 0.0 - convertToDegree(gps_latitude));
            }
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_GPSLATITUDE: " + img_data.getTag_gps_latitude());

        if (gps_longitude !=null)
        {
            if ( exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF) .equals("E") )
            {
                img_data.setTag_gps_longitude( convertToDegree(gps_longitude) );
            }
            else
            {
                img_data.setTag_gps_longitude( 0.0 - convertToDegree(gps_longitude));
            }
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_GPSLATITUDE: " + img_data.getTag_gps_longitude());

        if (gps_provider != null)
        {
            img_data.setTag_gps_provider(gps_provider);
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_GPSPROVIDER: " + img_data.getTag_gps_provider());

        img_data.setTag_img_height(Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)));
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_IMG_HEIGHT: " + img_data.getTag_img_height());

        //Log.d(LOGTags.INDEX_THREAD_PROGRESS, "TAG_IMG_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
        img_data.setTag_img_width(Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)));
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_IMG_WIDTH: " + img_data.getTag_img_width());

        //Log.d(LOGTags.INDEX_THREAD_PROGRESS, "TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE));
        img_data.setTag_make(exifInterface.getAttribute(ExifInterface.TAG_MAKE));
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_MAKE: " + img_data.getTag_make());

        //Log.d(LOGTags.INDEX_THREAD_PROGRESS, "TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL));
        img_data.setTag_model(exifInterface.getAttribute(ExifInterface.TAG_MODEL));
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_MODEL: " + img_data.getTag_model());

        //Log.d(LOGTags.INDEX_THREAD_PROGRESS, "TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION));
        img_data.setTag_orientation(Integer.parseInt(exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION)));
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_ORIENTATION: " + img_data.getTag_orientation());

        String white_balance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
        //Log.d(LOGTags.INDEX_THREAD_PROGRESS,"TAG_WHITEBALANCE: " +  white_balance);
        if (white_balance != null) {
            img_data.setTag_white_balence(Double.parseDouble(exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE)));
        }
        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "I_TAG_WHITEBALANCE: " + img_data.getTag_white_balence());

        return img_data;
    }

    private FaceData getFaceDetection(String image_path)
    {
        FaceData faceData = new FaceData();

        Bitmap cur_bmp = BitmapFactory.decodeFile(image_path);

        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(cur_bmp).build();

        if ( mSafeDetector.isOperational() ) {
            SparseArray<Face> faces = mSafeDetector.detect(frame);

            if ( faces.size() > 0 ) {

                faceData.setFaces_count(faces.size());

                ArrayList<FaceObject> face_objects = new ArrayList<FaceObject>();

                for (int a_face = 0; a_face < faces.size(); a_face++) {
                    Face cur_face = faces.get(a_face);

                    FaceObject cur_face_object = new FaceObject();

                    cur_face_object.setFace_angle_y(new Double(cur_face.getEulerY()));
                    Log.d(LOGTags.FACE_DETECTION, "ANG-Y: " + cur_face_object.getFace_angle_y());

                    cur_face_object.setFace_angle_z(new Double(cur_face.getEulerZ()));
                    Log.d(LOGTags.FACE_DETECTION, "ANG-Z: " + cur_face_object.getFace_angle_z());

                    cur_face_object.setFace_height(new Float(cur_face.getHeight()).intValue());
                    Log.d(LOGTags.FACE_DETECTION, "HEIGHT: " + cur_face_object.getFace_height());

                    cur_face_object.setFace_width(new Float(cur_face.getWidth()).intValue());
                    Log.d(LOGTags.FACE_DETECTION, "WIDTH: " + cur_face_object.getFace_width());

                    if ( cur_face.getIsSmilingProbability() > 0 )
                        cur_face_object.setIs_smiling_probability(new Float(cur_face.getIsSmilingProbability()).doubleValue());
                    else
                        cur_face_object.setIs_smiling_probability(-1.0);
                    Log.d(LOGTags.FACE_DETECTION, "SMILE PROB: " + cur_face_object.getIs_smiling_probability());

                    if ( cur_face.getIsLeftEyeOpenProbability() > 0 )
                    cur_face_object.setLeft_eye_open_prob(new Double(cur_face.getIsLeftEyeOpenProbability()));
                    else
                        cur_face_object.setLeft_eye_open_prob(new Double(-1.0));
                    Log.d(LOGTags.FACE_DETECTION, "LEFT EYE OPEN PROB: " + cur_face_object.getLeft_eye_open_prob());

                    if ( cur_face.getIsRightEyeOpenProbability() > 0 )
                    cur_face_object.setRight_eye_open_prob(new Float(cur_face.getIsRightEyeOpenProbability()).doubleValue());
                    else
                    cur_face_object.setRight_eye_open_prob(-1.0);
                    Log.d(LOGTags.FACE_DETECTION, "RIGHT EYE OPEN PROB: " + cur_face_object.getRight_eye_open_prob());

                    cur_face_object.setFace_coord_row(new Float(cur_face.getPosition().x).intValue());
                    Log.d(LOGTags.FACE_DETECTION, "COORD ROW: " + cur_face_object.getFace_coord_row());

                    cur_face_object.setFace_coord_col(new Float(cur_face.getPosition().y).intValue());
                    Log.d(LOGTags.FACE_DETECTION, "COORD COL: " + cur_face_object.getFace_coord_col());

                    face_objects.add(cur_face_object);

                }

                faceData.setAll_faces(face_objects);
            }
            Log.d(LOGTags.FACE_DETECTION,"Total Faces found: " + faces.size());
        }


        return faceData;
    }

    @Override
    protected Integer doInBackground(String[]... params) {

        counter_new_images = new ArrayList<Integer>();

        Integer total_locations = params[0].length;
        Integer current_location = 0;

        Integer all_new_images_discovered = 0;
        int new_images_in_location = 0;

        // should check for isCancelled
        while( current_location < total_locations && !isCancelled() ) {

            // get list of images present in this location
            String[] full_file_paths = getFilesList( params[0][current_location] );

            if ( full_file_paths != null ) {

                int total_images_in_location = full_file_paths.length;

                new_images_in_location = 0;

                for (int an_image = 0; an_image < total_images_in_location; an_image++) {
                    Log.d(LOGTags.INDEX_THREAD_PROGRESS, "Looking at Image " + an_image + "/" + total_images_in_location + " from Location: " + params[0][current_location]);

                    // check if image is present in our DB
                    Boolean is_present = mDBHandler.checkImageExists(full_file_paths[an_image] );

                    // Extract details from the Image if the image has not been previously indexed
                    if ( is_present == false) {
                        try {
                            ExifInterface exifInterface = new ExifInterface(full_file_paths[an_image]);

                            // Create Image object and add details to it.
                            ImageData cur_img_data = readExifParameters(exifInterface, full_file_paths[an_image]);

                            // Run facial analysis on it and create a face object
                            FaceData faceData = getFaceDetection(full_file_paths[an_image]);

                            if (faceData.getFaces_count() > 0)
                            {
                                cur_img_data.setHas_face(true);
                                cur_img_data.setFaces_count(faceData.getFaces_count());

                                Long img_id_inserted = mDBHandler.AddImageData(cur_img_data);

                                if (img_id_inserted != null) {
                                    faceData.setImage_id(img_id_inserted);
                                    mDBHandler.AddFaceData(faceData);
                                }
                                else
                                {
                                    Log.d(LOGTags.INDEX_THREAD_PROGRESS,"FAILED TO ADD IMAGE AND CONSEQUENTLY FACES");
                                }
                            }
                            else
                            {
                                Long img_id_inserted = mDBHandler.AddImageData(cur_img_data);

                                if (img_id_inserted == null) {
                                    Log.d(LOGTags.INDEX_THREAD_PROGRESS,"NO FACES FOUND AND FAILED TO ADD IMAGE AND CONSEQUENTLY FACES");
                                }
                            }


                        } catch (Exception e) {
                            Log.d(LOGTags.INDEX_THREAD_PROGRESS, "ERROR while reading exif tag on Image " + an_image);
                            e.printStackTrace();
                        }

                    }
                    else {
                        Log.d(LOGTags.INDEX_THREAD_PROGRESS, "Image " + an_image + " has already been indexed");
                    }
                }

            }
            else
            {
                new_images_in_location = 0;
            }

            // add details to our local stats
            // how many new images discovered
            counter_new_images.add(current_location, new_images_in_location);


            // invokes publishProgress(Integer... params) to say stuff
            publishProgress(current_location);

            all_new_images_discovered = all_new_images_discovered + new_images_in_location;

            // increment current_location
            current_location++;
        }

        return all_new_images_discovered;
    }

    // returns list of files stored in a certain location
    private String[] getFilesList(String location) {

        String[] all_files;
        String base_path;


        switch (location) {

            case "Gallery": {

                File gallery = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                base_path = gallery.toString();
                Log.d(LOGTags.INDEX_THREAD_PROGRESS, "External Storage Directory is :" + gallery.toString());
                all_files = gallery.list();
                if ( all_files != null) {
                    Log.d(LOGTags.INDEX_THREAD_PROGRESS, "External Storage Directory contains :" + all_files.length);
                }
                else
                {
                    Log.d(LOGTags.INDEX_THREAD_PROGRESS, "External Storage Directory contains : 0 files");
                }
                break;

            }
            case "ExternalStorage": {
                File external_storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM +"/Camera/");
                base_path = external_storage.toString();
                Log.d(LOGTags.INDEX_THREAD_PROGRESS,"External Storage Directory is :" + external_storage.toString());
                all_files = external_storage.list();
                if ( all_files != null) {
                Log.d(LOGTags.INDEX_THREAD_PROGRESS,"External Storage Directory contains :" + all_files.length);
                }
                else
                {
                    Log.d(LOGTags.INDEX_THREAD_PROGRESS, "External Storage Directory contains : 0 files");
                }

                break;
            }
            default:
                all_files = null;
                base_path = "";
        }

        // add base path
        if ( all_files != null && all_files.length > 0)
        {
            for (int a_file = 0; a_file < all_files.length; a_file++)
            {
                all_files[a_file] = base_path +"/" + all_files[a_file];
            }
        }

        return all_files;
    }

    private Double convertToDegree(String stringDMS){
        Double result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = FloatD + (FloatM/60) + (FloatS/3600);

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... params)
    {
        Log.d(LOGTags.INDEX_THREAD_PROGRESS,"Finished Processing Location " + params[0]);
    }

    // OnPostExecute tells how many new images were added
    protected void onPostExecute(Integer... params)
    {
        // make a toast of some kind

        // remove notification bar over here


    }

    protected void onCancelled (Integer... params)
    {
        // what to do if the sync is cancelled

        // allow for faster search facility

        // remove notification bar here
    }



}
