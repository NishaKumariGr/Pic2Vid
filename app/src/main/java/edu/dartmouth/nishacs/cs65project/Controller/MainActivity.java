package edu.dartmouth.nishacs.cs65project.Controller;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


import java.util.ArrayList;

import edu.dartmouth.nishacs.cs65project.Model.ActionTabsViewPagerAdapter;
import edu.dartmouth.nishacs.cs65project.Model.IndexingThread;
import edu.dartmouth.nishacs.cs65project.R;
import edu.dartmouth.nishacs.cs65project.Model.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {


    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;
    private CreationFragment mCreationFragment;
    private HistoryFragment mHistoryFragment;

    private static Integer runIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //changing the color of status bar
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.colorActionBar));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //to prevent keyboard from popping by default
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        update_local_photos();

        // Define SlidingTabLayout (shown at top)
        // and ViewPager (shown at bottom) in the layout.
        // Get their instances.
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // create a fragment list in order.
        fragments = new ArrayList<Fragment>();
        fragments.add(new CreationFragment());
        fragments.add(new HistoryFragment());

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together.
        myViewPageAdapter =new ActionTabsViewPagerAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(myViewPageAdapter);

        // make sure the tabs are equally spaced.
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    private void update_local_photos()
    {
        Resources resource = getResources();
        String[] locations_to_search = resource.getStringArray(R.array.locations_to_search);

        IndexingThread index_my_images = new IndexingThread(this);
        index_my_images.execute(locations_to_search);
    }

    public void setMapLocation(View view)
    {
        Intent locationIntent = new Intent(this,
                MapsActivity.class);

        startActivity(locationIntent);
    }


}

