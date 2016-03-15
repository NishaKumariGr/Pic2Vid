package edu.dartmouth.nishacs.cs65project.Controller;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.dartmouth.nishacs.cs65project.Model.P2VVideoObject;
import edu.dartmouth.nishacs.cs65project.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    HistoryListAdapter historyListAdapter;
    ListView list;
    ArrayList<P2VVideoObject> all_videos;

    private static Context mContext;
    private QueryController mQueryController;
    private OnFragmentInteractionListener mListener;

    public static final String LAUNCH_VIDEO="LAUNCH_VIDEO";

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        mQueryController = new QueryController(mContext);
        all_videos = mQueryController.getAllVideos();
        Log.d("HISTORY FRAGMENT", "FOUND VIDEOS:" + all_videos.size());

        historyListAdapter = new HistoryListAdapter(mContext, all_videos);
      //  list.setAdapter(historyListAdapter);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        list = (ListView) rootView.findViewById(R.id.historyList);
        list.setAdapter(historyListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent show_video = new Intent(mContext,VideoActivity.class);
                show_video.putExtra(LAUNCH_VIDEO, all_videos.get(position).getVid_id());

                startActivity(show_video);

// TODO Auto-generated method stub
            }
        });
        return  rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
  public void onResume() {
            super.onResume();
        all_videos = mQueryController.getAllVideos();
        historyListAdapter.clear();
        historyListAdapter.addAll(all_videos);
        historyListAdapter.notifyDataSetChanged();
        Log.d("on resume","on resume");
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
