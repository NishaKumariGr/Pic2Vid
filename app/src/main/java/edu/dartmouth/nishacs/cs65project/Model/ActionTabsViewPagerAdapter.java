package edu.dartmouth.nishacs.cs65project.Model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by nisha on 1/14/16.
 * This class serves as the adapter for the view pager of action tabs
 */
public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> fragments;

    public static final int CREATION = 0;
    public static final int HISTORY = 1;
    public static final String UI_TAB_CREATION = "CREATION";
    public static final String UI_TAB_HISTORY = "HISTORY";


    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case CREATION:
                return UI_TAB_CREATION;
            case HISTORY:
                return UI_TAB_HISTORY;
             default:
                break;
        }
        return null;
    }


}
