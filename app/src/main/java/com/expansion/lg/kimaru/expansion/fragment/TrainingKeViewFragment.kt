package com.expansion.lg.kimaru.expansion.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.mzigos.Training
import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee
import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer
import com.expansion.lg.kimaru.expansion.tables.TrainingClassTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTraineeTable
import com.expansion.lg.kimaru.expansion.tables.TrainingTrainersTable

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TrainingKeViewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TrainingKeViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class
TrainingKeViewFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    internal var training: Training? = null
    internal var trainingTrainers: List<TrainingTrainer>? = ArrayList()
    internal var trainingTrainees: List<TrainingTrainee>? = ArrayList()
    internal var trainingClassesList: List<TrainingClass>? = ArrayList()
    internal var trainers = 0
    internal var trainees = 0
    internal var traingClasses = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_training_ke_view, container, false)

        // get the trainers
        if (training != null) {
            trainingTrainers = TrainingTrainersTable(context!!)
                    .getTrainingTrainersByTrainingId(training!!.id)
            if (trainingTrainers != null) {
                trainers = trainingTrainers!!.size
            }

            trainingTrainees = TrainingTraineeTable(context!!)
                    .getTrainingByTraining(training!!.id)
            if (trainingTrainees != null) {
                trainees = trainingTrainees!!.size
            }

            trainingClassesList = TrainingClassTable(context!!)
                    .getTrainingClassesByTraining(training!!.id)
            if (trainingClassesList != null) {
                traingClasses = trainingClassesList!!.size
            }
        }

        val trainingTrainersTextView = view.findViewById<View>(R.id.trainingTrainers) as TextView
        val traineeSummaryTextView = view.findViewById<View>(R.id.traineeSummary) as TextView
        val trainingClassesTextView = view.findViewById<View>(R.id.classesSummary) as TextView

        trainingTrainersTextView.text = trainers.toString() + " Trainers"
        traineeSummaryTextView.text = trainees.toString() + " Trainees"
        trainingClassesTextView.text = traingClasses.toString() + " Classes"
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (training != null) {
            (activity as AppCompatActivity).supportActionBar!!.title = training!!.trainingName + " Training"
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrainingKeViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): TrainingKeViewFragment {
            val fragment = TrainingKeViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
