package com.expansion.lg.kimaru.expansion.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.mzigos.Training;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee;
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer;
import com.expansion.lg.kimaru.expansion.tables.TrainingClassTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTraineeTable;
import com.expansion.lg.kimaru.expansion.tables.TrainingTrainersTable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainingKeViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainingKeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingKeViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Training training = null;
    List<TrainingTrainer> trainingTrainers = new ArrayList<TrainingTrainer>();
    List<TrainingTrainee> trainingTrainees = new ArrayList<TrainingTrainee>();
    List<TrainingClass> trainingClassesList = new ArrayList<TrainingClass>();
    int trainers = 0;
    int trainees = 0;
    int traingClasses = 0;

    public TrainingKeViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrainingKeViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainingKeViewFragment newInstance(String param1, String param2) {
        TrainingKeViewFragment fragment = new TrainingKeViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_training_ke_view, container, false);

        // get the trainers
        if (training != null){
            trainingTrainers =  new TrainingTrainersTable(getContext())
                    .getTrainingTrainersByTrainingId(training.getId());
            if (trainingTrainers != null){
                trainers = trainingTrainers.size();
            }

            trainingTrainees =  new TrainingTraineeTable(getContext())
                    .getTrainingByTraining(training.getId());
            if (trainingTrainees != null){
                trainees = trainingTrainees.size();
            }

            trainingClassesList =  new TrainingClassTable(getContext())
                    .getTrainingClassesByTraining(training.getId());
            if (trainingClassesList != null){
                traingClasses = trainingClassesList.size();
            }
        }

        TextView trainingTrainersTextView = (TextView) view.findViewById(R.id.trainingTrainers);
        TextView traineeSummaryTextView = (TextView) view.findViewById(R.id.traineeSummary);
        TextView trainingClassesTextView = (TextView) view.findViewById(R.id.classesSummary);

        trainingTrainersTextView.setText(trainers+" Trainers");
        traineeSummaryTextView.setText(trainees +" Trainees");
        trainingClassesTextView.setText(traingClasses +" Classes");
        return view;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (training != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(training.getTrainingName()+ " Training");
        }
    }
}
