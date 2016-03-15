package edu.dartmouth.nishacs.cs65project.Controller;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import edu.dartmouth.nishacs.cs65project.Model.P2VVideoObject;
import edu.dartmouth.nishacs.cs65project.R;

/**
 * Created by nisha on 3/5/16.
 * This class serves as the adapter class for history fragment that displays a listView
 */
public class HistoryListAdapter extends ArrayAdapter<P2VVideoObject> {
    private final Context context;
    private final ArrayList<P2VVideoObject> all_videos;
    private LayoutInflater mInflater;

    public HistoryListAdapter(Context context, ArrayList<P2VVideoObject> all_videos) {

        super(context, R.layout.history_list_item, all_videos);

        this.context=context;
        this.all_videos = all_videos;
        mInflater = LayoutInflater.from(context);

    }


    public View getView(int position,View view,ViewGroup parent) {

        Log.d("HISTORY ADAPTED", "Calling with position:" + position);

        View rowView=mInflater.inflate(R.layout.history_list_item, null, true);

        P2VVideoObject cur_obj= all_videos.get(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_creation);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView author_txt = (TextView) rowView.findViewById(R.id.txt_author);
        TextView date_created_txt = (TextView) rowView.findViewById(R.id.txt_date_created);
        TextView date_last_edited_txt = (TextView) rowView.findViewById(R.id.txt_date_last_edited);
        TextView duration_txt = (TextView) rowView.findViewById(R.id.txt_total_frames);

        txtTitle.setText(cur_obj.getVideoTitle());
        //imageView.setImageResource(R.drawable.image3);
        if (cur_obj.getFile_paths().size() > 0) {
            imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(cur_obj.getFile_paths().get(0)), 225, 225));
        }
        author_txt.setText("Author: " + cur_obj.getAuthor());

        //formatting the date
        String dateInString = cur_obj.getDateCreated();

        DateFormat formatter = new SimpleDateFormat("h:mm a MMM d yyyy");

        long milliSeconds= Long.parseLong(dateInString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        date_created_txt.setText("Date Created: "+formatter.format(calendar.getTime()));

        //formatting the date

        String dateInStringEdited = cur_obj.getDateLastEdited();

        long milliSecondsEdited= Long.parseLong(dateInStringEdited);

        Calendar calendarEdited = Calendar.getInstance();
        calendarEdited.setTimeInMillis(milliSecondsEdited);
        date_last_edited_txt.setText("Date Last Edited: "+formatter.format(calendarEdited.getTime()));
        duration_txt.setText("Images: "+ cur_obj.getTotalImagesUsed());

        return rowView;
    }
}
