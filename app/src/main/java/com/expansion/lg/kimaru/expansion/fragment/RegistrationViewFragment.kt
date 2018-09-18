package com.expansion.lg.kimaru.expansion.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement
import com.expansion.lg.kimaru.expansion.mzigos.Education
import com.expansion.lg.kimaru.expansion.mzigos.Exam
import com.expansion.lg.kimaru.expansion.mzigos.Interview
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Registration
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty
import com.expansion.lg.kimaru.expansion.other.DefaultListMenu
import com.expansion.lg.kimaru.expansion.other.DisplayDate
import com.expansion.lg.kimaru.expansion.other.DividerItemDecoration
import com.expansion.lg.kimaru.expansion.tables.EducationTable
import com.expansion.lg.kimaru.expansion.tables.ExamTable
import com.expansion.lg.kimaru.expansion.tables.InterviewTable
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter

import java.util.ArrayList

/**
 * Created by kimaru on 3/30/17.
 */

class RegistrationViewFragment : Fragment(), View.OnClickListener {

    private val mRecyclerView: RecyclerView? = null
    internal lateinit var sessionManagement: SessionManagement
    internal lateinit var registration: Registration
    internal lateinit var interviewResults: TextView
    internal lateinit var examResults: TextView
    internal lateinit var selectedStatus: TextView
    internal lateinit var selectionStatus: Spinner
    internal var selected: Int? = 0
    internal var examPass: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_view_registration, container, false)
        MainActivity.CURRENT_TAG = MainActivity.REGISTRATION_VIEW
        MainActivity.backFragment = RegistrationsFragment()
        // Retrieve saved details
        sessionManagement = SessionManagement(context!!)
        //get the details of the registration
        registration = sessionManagement.savedRegistration

        val profileName = v.findViewById<View>(R.id.user_profile_name) as TextView
        selectionStatus = v.findViewById<View>(R.id.editSelectedStatus) as Spinner


        profileName.text = registration.name + "( " + registration.gender + ")"
        //user_profile_short_bio
        val bio = v.findViewById<View>(R.id.user_profile_short_bio) as TextView
        //        bio.setText("Age: \t "+ registration.getAge()
        //                + " years.\n" +
        //                "Phone " + registration.getPhone()
        //            );

        val bioInfo = StringBuilder()
        bioInfo.append("Aged ").append(registration.age).append(" years\n")
        bioInfo.append("Phones:\n")
        val phones = registration.phone.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (phone in phones) {
            bioInfo.append("\t").append(phone).append("\n")
        }
        bio.text = bioInfo.toString()

        //location
        val viewlocation = v.findViewById<View>(R.id.viewlocation) as TextView
        viewlocation.text = "Lived at " + registration.village +
                " for  " + registration.dateMoved + " years"

        //education
        val educationLevel = v.findViewById<View>(R.id.educationLevel) as TextView
        val educationTable = EducationTable(context!!)
        val education = educationTable.getEducationById(Integer.valueOf(registration.education))
        if (education != null) {

            if (registration.country.equals("KE", ignoreCase = true)) {
                educationLevel.text = "Education Level: " + education.levelName + "\n\n" +
                        "Other Trainings\n" + registration.otherTrainings

            } else {
                educationLevel.text = "Education Level: " + education.levelName
            }
        } else {
            educationLevel.text = ""
        }


        //Occupation
        val occupation = v.findViewById<View>(R.id.occupation) as TextView
        occupation.text = "Current occupation: " + registration.occupation

        //brac
        val bracChp = v.findViewById<View>(R.id.bracChp) as TextView
        if (registration.country.equals("UG", ignoreCase = true)) {
            if (registration.brac == 1) {
                if (registration.bracChp == 1) {
                    bracChp.text = "Has worked as BRAC CHP"
                } else {
                    bracChp.text = "Has worked at BRAC"
                }
            } else {
                bracChp.text = "Hasn't worked at BRAC"
            }
        } else {
            var txt = ""
            txt += if (registration.isGokTrained) "GoK Trained\n" else "Not a GoK Trained\n"
            txt += if (registration.isChv) "Currently a CHV\n" else "Not a CHV\n"
            txt += "Households " + registration.noOfHouseholds + "\n"
            bracChp.text = txt

        }
        var exam: Exam? = Exam()
        val examTable = ExamTable(context!!)
        exam = examTable.getExamByRegistration(sessionManagement.savedRegistration.id)

        examResults = v.findViewById<View>(R.id.examResults) as TextView
        examResults.text = ""
        if (exam != null) {
            val total = exam.english!! + exam.math!! + exam.personality!!
            val ex = "Exam Results " + "\n\n" +
                    "English: " + exam.english + "\n" +
                    "Maths: " + exam.math + "\n" +
                    "About: " + exam.personality + "\n" +
                    "Total " + total + "\n\n" +
                    "Status " + if (exam.hasPassed()) "Passed" else "Failed"

            val regComment = registration.comment
            examResults.text = regComment + "\n\n\n" + ex
            examPass = exam.hasPassed()

        } else {
            //Allow adding new Exam
            examResults.text = "No Exam taken"
            examResults.setOnClickListener(this)
        }

        //Interviews
        var interview: Interview? = Interview()
        val interviewTable = InterviewTable(context!!)
        interview = interviewTable.getInterviewByRegistrationId(sessionManagement.savedRegistration.id)
        interviewResults = v.findViewById<View>(R.id.interviewResults) as TextView
        selectedStatus = v.findViewById<View>(R.id.selectedStatus) as TextView
        interviewResults.text = ""
        selectedStatus.text = ""
        selectedStatus.setOnClickListener(this)
        if (interview != null) {
            val interviewScores = "Interview Results \n\n" +
                    "Comments\n\n: " + interview.comment + "\n\n" +
                    "Motivation: " + interview.motivation + "\n" +
                    "Community engagement: " + interview.community + "\n" +
                    "Mentality: " + interview.mentality + "\n" +
                    "Selling Skills: " + interview.selling + "\n" +
                    "Interest in health: " + interview.health + "\n" +
                    "Investment: " + interview.investment + "\n" +
                    "Interpersonal Skills: " + interview.interpersonal + "\n" +
                    "Commitment: " + interview.commitment + "\n" +
                    "Selling Skills " + interview.selling + "\n\n" +
                    "Interview Status " + (if (interview.hasPassed()) "Passed" else "Failed") + "\n\n\n" +
                    "Overall Status " + if (interview.hasPassed()) "Passed" else "Failed"
            interviewResults.text = interviewScores
            if (interview.selected == 1) {
                selected = 1
                selectedStatus.text = "Selected for training"
            } else if (interview.selected == 0) {
                selected = 0
                selectedStatus.text = "Not selected for training"
            } else {
                selected = 2
                selectedStatus.text = "Waiting List"
            }
            selectionStatus.setSelection(interview.selected!!)
        } else {
            //Allow adding new Interview
            interviewResults.text = "No Interview. Click to interview"
            interviewResults.setOnClickListener(this)
        }
        selectionStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                toggleCandidateSelectionStatus(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        return v
    }

    fun formatEducation(educationLevel: Int): String {
        return if (educationLevel > 12) {
            "Year " + (educationLevel - 12)
        } else if (educationLevel > 8) {
            "Form " + (educationLevel - 8)
        } else {
            "Class $educationLevel"
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.title = registration.name
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // getMenuInflater().inflate(R.menu.registration_menu, menu);
        inflater!!.inflate(R.menu.registration_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (item!!.itemId) {
            // action with ID action_refresh was selected
            R.id.action_interview -> {
                // get teh interview so that users dont reenter new data
                val interviewTable = InterviewTable(context!!)
                val interview = interviewTable.getInterviewByRegistrationId(
                        sessionManagement.savedRegistration.id)
                val newInterviewFragment = NewInterviewFragment()
                newInterviewFragment.editingInterview = interview
                fragment = newInterviewFragment

                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW)
                fragmentTransaction.commitAllowingStateLoss()
            }
            // action with ID action_settings was selected
            R.id.action_exam -> {
                val examTable = ExamTable(context!!)
                val exam = examTable.getExamByRegistration(sessionManagement.savedRegistration.id)
                val newExamFragment = NewExamFragment()
                newExamFragment.editingExam = exam
                fragment = newExamFragment
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW)
                fragmentTransaction.commitAllowingStateLoss()
            }
            else -> {
            }
        }



        return true
    }

    override fun onClick(view: View) {
        val fragment: Fragment
        val fragmentTransaction: FragmentTransaction
        when (view.id) {
            R.id.examResults -> {
                fragment = NewExamFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.interviewResults -> {
                fragment = NewInterviewFragment()
                fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, MainActivity.REGISTRATION_VIEW)
                fragmentTransaction.commitAllowingStateLoss()
            }
            R.id.selectedStatus -> {
                //get the interview
                val interview: Interview?
                val interviewTable = InterviewTable(context!!)
                interview = interviewTable.getInterviewByRegistrationId(sessionManagement.savedRegistration.id)
                if (interview != null) {
                    if (selected == 1) {
                        selected = 0
                        interview.selected = 0
                        interviewTable.addData(interview)
                        selectedStatus.text = "Not selected for training \n Click to select"
                    } else {
                        selected = 1
                        interview.selected = 1
                        interviewTable.addData(interview)
                        selectedStatus.text = "Selected for training \n Click to deselect"
                    }
                }
            }
        }
    }

    fun toggleCandidateSelectionStatus(status: Int?) {
        val interview: Interview?
        val interviewTable = InterviewTable(context!!)
        interview = interviewTable.getInterviewByRegistrationId(sessionManagement.savedRegistration.id)
        if (interview != null) {
            if (status == 1) { // the person has been selected
                selected = 1
                interview.selected = 1
                interviewTable.addData(interview)
                selectedStatus.text = "Selected for Training"
            } else if (status == 0) {
                selected = 0
                interview.selected = 0
                interviewTable.addData(interview)
                selectedStatus.text = "Not selected for training"
            } else {
                selected = 2
                interview.selected = 2
                interviewTable.addData(interview)
                selectedStatus.text = "Waiting List"
            }
        }
    }


}
