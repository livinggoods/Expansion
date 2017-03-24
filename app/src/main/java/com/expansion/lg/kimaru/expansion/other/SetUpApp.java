package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;

import com.expansion.lg.kimaru.expansion.tables.EducationTable;

/**
 * Created by kimaru on 3/22/17.
 */

public class SetUpApp {

    public void setUpEducation(Context context){
        EducationTable educationTable = new EducationTable(context);
        educationTable.createEducationLevels();
    }
}
