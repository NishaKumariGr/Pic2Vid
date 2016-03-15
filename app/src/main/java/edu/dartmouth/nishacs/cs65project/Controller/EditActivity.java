package edu.dartmouth.nishacs.cs65project.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import edu.dartmouth.nishacs.cs65project.R;
import edu.dartmouth.nishacs.cs65project.Model.VideoSingleton;

//this code is influenced from: https://vikaskanani.wordpress.com/2011/07/20/android-custom-image-gallery-with-checkbox-in-grid-to-select-multiple/
//this site explains how to make a gridview of photos with checkbox selections over each one

public class EditActivity extends AppCompatActivity  {
    private int count;
    private Bitmap[] thumbnails;
    private String[] arrPath;
    private ImageAdapter imageAdapter;
    private static VideoSingleton mVideoSingleton = VideoSingleton.getInstance();

    static final int PICK_SOUND_REQUEST = 0;

    MyLoader gridLoader;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //changing the color of status bar
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.colorActionBar));

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        this.count =  mVideoSingleton.mVideo.mImageList.size();
        this.thumbnails = new Bitmap[this.count];
        this.arrPath = new String[this.count];

        TextView title = (TextView) findViewById(R.id.textInputType);
        title.setText(mVideoSingleton.mVideo.name);

        //set the frames depending on the number of images in the arrayList
        gridLoader = new MyLoader();

        Thread t = new Thread(gridLoader);
        t.start();

        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
    }

    class MyLoader implements Runnable {

        public MyLoader()
        {

        }

        @Override
        public void run() {

            for (int i = 0; i < count; i++) {
                arrPath[i] = mVideoSingleton.mVideo.mImageList.get(i);

                //this.thumbnails[i] = BitmapFactory.decodeFile(mVideo.mImageList.get(i));

                Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mVideoSingleton.mVideo.mImageList.get(i)), 225, 225);
                thumbnails[i] = resized;
                Log.d("EDIT", "RUN AT" + i);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDataset();
                    }
                });

            }





        }

        public void updateDataset()
        {
            if (imageAdapter != null)
            {
                imageAdapter.notifyDataSetChanged();
            }
        }

    }



    public void onSaveClicked(View view){
        VideoActivity.saveToVideo(view);
        Intent new_intent = new Intent(this,MainActivity.class);
        new_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(new_intent);
    }

    public void onWatchVideoClicked(View view) {
        finish();
    }

    public void onEditTitleClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                TextView title = (TextView) findViewById(R.id.textInputType);
                String name = input.getText().toString();
                title.setText(name);
                mVideoSingleton.mVideo.name = name;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void onMusicChangeClicked(View view) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), PICK_SOUND_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == PICK_SOUND_REQUEST) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
                Bundle mBundle = data.getExtras();
                //startActivity(new Intent(Intent.ACTION_VIEW, data));
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkbox.setId(position);
            holder.imageview.setId(position);
            holder.checkbox.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (!mVideoSingleton.mVideo.deselectedVideos[id]){
                        cb.setChecked(false);
                        mVideoSingleton.mVideo.deselectedVideos[id] = true;
                    } else {
                        cb.setChecked(true);
                        mVideoSingleton.mVideo.deselectedVideos[id] = false;
                    }
                }
            });
            holder.imageview.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int id = v.getId();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("file://" + arrPath[id]), "image/*");
                    startActivity(intent);
                }
            });
            holder.imageview.setImageBitmap(thumbnails[position]);
            holder.checkbox.setChecked(!mVideoSingleton.mVideo.deselectedVideos[position]);
            holder.id = position;
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;
        CheckBox checkbox;
        int id;
    }
}