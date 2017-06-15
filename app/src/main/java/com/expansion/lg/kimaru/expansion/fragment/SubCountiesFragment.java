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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;
import com.expansion.lg.kimaru.expansion.dbhelpers.SubCountyListAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration;
import com.expansion.lg.kimaru.expansion.tables.SubCountyTable;

import java.util.ArrayList;
import java.util.List;

// to show list in Gmail Mode

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubCountiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubCountiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCountiesFragment extends Fragment  {
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

    // to show list in Gmail Mode
    private List<SubCounty> subCounties = new ArrayList<>();
    private RecyclerView recyclerView;
    private SubCountyListAdapter rAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    SessionManagement sessionManagement;

    Mapping mapping;


    // I cant seem to get the context working
    Context mContext = getContext();
    Activity activity = getActivity();



    public SubCountiesFragment() {
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
    public static SubCountiesFragment newInstance(String param1, String param2) {
        SubCountiesFragment fragment = new SubCountiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_subcounties, container, false);
        textshow = (TextView) v.findViewById(R.id.textShow);

        sessionManagement = new SessionManagement(getContext());
        mapping = sessionManagement.getSavedMapping();
        MainActivity.CURRENT_TAG = MainActivity.TAG_SUBCOUNTIES;
        final Fragment fragment;
        if (sessionManagement.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("KE")){
            MainActivity.backFragment = new MapKeViewFragment();
            fragment = new NewSubCountyFragment();
            fab = (FloatingActionButton) v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "villages");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });
        }else{
            MainActivity.backFragment = new MapKeViewFragment();
            fab.hide();
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // onRefresh action here
                getSubCounties();
            }
        });
        rAdapter = new SubCountyListAdapter(this.getContext(), subCounties, new SubCountyListAdapter.SubCountyListAdapterListener() {
            @Override
            public void onIconClicked(int position) {
                if (actionMode == null) {
//                    actionMode = startSupportActionMode(actionModeCallback);
                }

//                toggleSelection(position);
            }

            @Override
            public void onIconImportantClicked(int position) {
                Toast.makeText(getContext(), "An iconImportant is clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMessageRowClicked(int position) {
                // read the message which removes bold from the row
                SubCounty subCounty = subCounties.get(position);

                subCounty.setRecommended(true);
                subCounties.set(position, subCounty);
                rAdapter.notifyDataSetChanged();

                //lets save the subcounty
                sessionManagement.saveSubCounty(subCounty);

                SubCountyViewFragment subCountyViewFragment = new SubCountyViewFragment();
                Fragment fragment = subCountyViewFragment;
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "mappingsview");
                fragmentTransaction.commitAllowingStateLoss();


            }

            @Override
            public void onRowLongClicked(int position) {
                // Need to edit the item
                SubCounty subCounty = subCounties.get(position);
                NewSubCountyFragment newSubCountyFragment = new NewSubCountyFragment();
                newSubCountyFragment.subCountyEditing = subCounty;
                Fragment fragment = newSubCountyFragment;
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "newSubCounty");
                fragmentTransaction.commitAllowingStateLoss();

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
                        getSubCounties();
                    }
                }
        );

        // getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        // try setting the title
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String county = mapping.getCounty();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(county + " - Sub Counties ");
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


    // ===================================== Gmail View Methods ====================================
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
    private void getSubCounties() {
        swipeRefreshLayout.setRefreshing(true);

        subCounties.clear();

        // clear the registrations
        try {
            // get the registrations
            SubCountyTable subCountyTable = new SubCountyTable(getContext());
            List<SubCounty> subCountyList = new ArrayList<>();

            subCountyList = subCountyTable.getSubCountiesByCounty(Integer.valueOf(mapping.getCounty()));
            for (SubCounty subCounty:subCountyList){
                subCounty.setColor(getRandomMaterialColor("400"));
                subCounties.add(subCounty);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), "No Subcounties", Toast.LENGTH_SHORT).show();
            textshow.setText("No sub counties added. Please create one");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

}
