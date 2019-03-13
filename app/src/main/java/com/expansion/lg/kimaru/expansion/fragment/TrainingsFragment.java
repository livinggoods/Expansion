package com.expansion.lg.kimaru.expansion.fragment;

/**
 * Created by kimaru on 3/11/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.dbhelpers.TrainingListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.Training;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.other.InternetCheck;
import com.expansion.lg.kimaru.expansion.sync.TrainingDataSync;
import com.expansion.lg.kimaru.expansion.tables.TrainingTable;


import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

// to show list in Gmail Mode

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView textshow;
    FloatingActionButton fab;

    private List<Training> trainings = new ArrayList<>();
    private RecyclerView recyclerView;
    private TrainingListAdapter rAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    SessionManagement session;


    // I cant seem to get the context working
    Context mContext = getContext();
    Activity activity = getActivity();



    public TrainingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainingsFragment newInstance(String param1, String param2) {
        TrainingsFragment fragment = new TrainingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v;
        //check if Recruitment is set
        MainActivity.CURRENT_TAG =MainActivity.TAG_EXAMS;
        MainActivity.backFragment = new HomeFragment();
        MainActivity.navItemIndex = 3;
        session = new SessionManagement(getContext());

        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_exams, container, false);
        textshow = (TextView) v.findViewById(R.id.textShow);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTrainings();
            }
        });
        rAdapter = new TrainingListAdapter(this.getContext(), trainings, new TrainingListAdapter.TrainingListAdapterListener() {
            @Override
            public void onIconClicked(int position) {
                Training training = trainings.get(position);
            }

            @Override
            public void onIconImportantClicked(int position) {
                Training training = trainings.get(position);
            }

            @Override
            public void onMessageRowClicked(int position) {
                Training training = trainings.get(position);
                TrainingKeViewFragment trainingKeViewFragment = new TrainingKeViewFragment();
                trainingKeViewFragment.training = training;
                Fragment fragment = trainingKeViewFragment;
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.TAG_NEW_RECRUITMENT);
                fragmentTransaction.commitAllowingStateLoss();

            }

            @Override
            public void onRowLongClicked(int position) {
                Training training = trainings.get(position);
            }

        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rAdapter);
        actionModeCallback = new ActionModeCallback();
        swipeRefreshLayout.post(
                new Runnable(){
                    @Override
                    public void run(){
                        getTrainings();
                    }
                }
        );

//        actionModeCallback = new ActionMode().Callback;


        //===========Gmail View Ends here ============================
        return v;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        rAdapter.toggleSelection(position);
        int count = rAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
    /*
    *  Choose a random
    *  Color
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            rAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    rAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        rAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                rAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            rAdapter.removeData(selectedItemPositions.get(i));
        }
        rAdapter.notifyDataSetChanged();
    }
    private void getTrainings() {
        swipeRefreshLayout.setRefreshing(true);
        trainings.clear();
        try {

            if (new InternetCheck(getContext()).isConnected()){
                // if there is internet, try loading the contents from the Cloud
                // then load the local content
                TrainingDataSync trainingDataSync = new TrainingDataSync(getContext());
                trainingDataSync.pollNewTrainings();
            }
            TrainingTable trainingTable = new TrainingTable(getContext());
            List<Training> trainingList = new ArrayList<>();

            trainingList = trainingTable.getTrainingByCountry(session.getUserDetails()
                    .get(SessionManagement.KEY_USER_COUNTRY));
            for (Training training:trainingList){
                training.setColor(getRandomMaterialColor("400"));
                trainings.add(training);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No Trainings found", Toast.LENGTH_SHORT).show();
            textshow.setText("Trainings");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Trainings");
        setHasOptionsMenu(false);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.registration_action_menu, menu);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //getSearchedRegistrations(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_export_scoring_tool:
                // export scoring tool
                break;
            // action with ID action_settings was selected
            case R.id.action_passed:
                // refresh the registrations
                break;

            case R.id.action_failed:
                // refresh the registrations
                break;

            case R.id.action_all:
                // refresh the registrations
                break;
            case R.id.action_new:

                break;

            default:
                break;
        }
        return true;
    }
}
