package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit
import com.expansion.lg.kimaru.expansion.mzigos.Mapping
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization
import com.expansion.lg.kimaru.expansion.mzigos.Parish
import com.expansion.lg.kimaru.expansion.mzigos.Village
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable
import com.expansion.lg.kimaru.expansion.tables.MappingTable
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable
import com.expansion.lg.kimaru.expansion.tables.ParishTable
import com.expansion.lg.kimaru.expansion.tables.UserTable
import com.expansion.lg.kimaru.expansion.tables.VillageTable

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.HashMap

/**
 * Created by kimaru on 8/22/17.
 */

class ExportDataToCsv(internal var context: Context) {
    internal var state: String

    init {
        state = Environment.getExternalStorageState()
    }

    fun exportChewReferral() {
        val chewReferralTable = ChewReferralTable(context)
        val chewReferrals = chewReferralTable.chewReferralData
        val headers = chewReferralTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("chew_referrals")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (p in chewReferrals) {
                val detailsLine = (p.id + ","
                        + p.name.replace(",".toRegex(), ";") + ","
                        + p.phone.replace(",".toRegex(), ";") + ","
                        + p.title.replace(",".toRegex(), ";") + ","
                        + p.country.replace(",".toRegex(), ";") + ","
                        + p.recruitmentId.replace(",".toRegex(), ";") + ","
                        + p.synced + ","
                        + p.county.replace(",".toRegex(), ";") + ","
                        + p.district.replace(",".toRegex(), ";") + ","
                        + p.subCounty.replace(",".toRegex(), ";") + ","
                        + p.communityUnit.replace(",".toRegex(), ";") + ","
                        + p.village.replace(",".toRegex(), ";") + ","
                        + p.mapping.replace(",".toRegex(), ";") + ","
                        + p.mobilization.replace(",".toRegex(), ";") + ","
                        + p.lon.replace(",".toRegex(), ";") + ","
                        + p.lat.replace(",".toRegex(), ";"))
                printWriter.println(detailsLine)
            }
            printWriter?.close()

        }
    }


    fun exportCommunityUnit() {
        val communityUnitTable = CommunityUnitTable(context)
        val communityUnits = communityUnitTable.communityUnitData
        val headers = communityUnitTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("community_units")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (cm in communityUnits) {
                val detailsLine = (cm.id + ","
                        + (if (cm.communityUnitName == null) "" else cm.communityUnitName.replace(",".toRegex(), ";")) + ","
                        + (if (cm.mappingId == null) "" else cm.mappingId.replace(",".toRegex(), ";")) + ","
                        + (if (cm.lat == null) "" else cm.lat!!.toString()) + ","
                        + (if (cm.lon == null) "" else cm.lon!!.toString()) + ","
                        + (if (cm.country == null) "" else cm.country.replace(",".toRegex(), ";")) + ","
                        + (if (cm.subCountyId == null) "" else cm.subCountyId.replace(",".toRegex(), ";")) + ","
                        + (if (cm.linkFacilityId == null) "" else cm.linkFacilityId.replace(",".toRegex(), ";")) + ","
                        + (if (cm.areaChiefName == null) "" else cm.areaChiefName.replace(",".toRegex(), ";")) + ","
                        + (if (cm.areaChiefPhone == null) "" else cm.areaChiefPhone.replace(",".toRegex(), ";")) + ","
                        + (if (cm.ward == null) "" else cm.ward.replace(",".toRegex(), ";")) + ","
                        + (if (cm.economicStatus == null) "" else cm.economicStatus.replace(",".toRegex(), ";")) + ","
                        + (if (cm.privateFacilityForAct == null) "" else cm.privateFacilityForAct.replace(",".toRegex(), ";")) + ","
                        + (if (cm.privateFacilityForMrdt == null) "" else cm.privateFacilityForMrdt.replace(",".toRegex(), ";")) + ","
                        + (if (cm.nameOfNgoDoingIccm == null) "" else cm.nameOfNgoDoingIccm.replace(",".toRegex(), ";")) + ","
                        + (if (cm.nameOfNgoDoingMhealth == null) "" else cm.nameOfNgoDoingMhealth.replace(",".toRegex(), ";")) + ","
                        + cm.dateAdded + ","
                        + UserTable(context).getUserById(Integer.valueOf(cm.addedBy.toString()))!!.name + ","
                        + cm.numberOfChvs + ","
                        + cm.householdPerChv + ","
                        + cm.numberOfVillages + ","
                        + cm.distanceToBranch + ","
                        + cm.transportCost + ","
                        + cm.distanceTOMainRoad + ","
                        + cm.noOfHouseholds + ","
                        + cm.mohPoplationDensity + ","
                        + cm.estimatedPopulationDensity + ","
                        + cm.distanceTONearestHealthFacility + ","
                        + cm.actLevels + ","
                        + cm.actPrice + ","
                        + cm.mrdtLevels + ","
                        + cm.mrdtPrice + ","
                        + cm.noOfDistibutors + ","
                        + cm.isChvsTrained + ","
                        + cm.isPresenceOfEstates + ","
                        + cm.isPresenceOfFactories + ","
                        + cm.isPresenceOfHostels + ","
                        + cm.isTraderMarket + ","
                        + cm.isLargeSupermarket + ","
                        + cm.isNgosGivingFreeDrugs + ","
                        + cm.isNgoDoingIccm + ","
                        + cm.isNgoDoingMhealth)
                printWriter.println(detailsLine)
            }
            printWriter?.close()

        }
    }


    fun exportMobilization() {
        val mobilizationTable = MobilizationTable(context)
        val mobilizations = mobilizationTable.mobilizationData
        val headers = mobilizationTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("mobilization")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (m in mobilizations) {
                val detailsLine = (m.id + ","
                        + m.name.replace(",".toRegex(), ";") + ","
                        + m.mappingId.replace(",".toRegex(), ";") + ","
                        + m.country.replace(",".toRegex(), ";") + ","
                        + UserTable(context).getUserById(m.addedBy!!)!!.name + ","
                        + m.comment.replace(",".toRegex(), ";") + ","
                        + m.dateAdded + ","
                        + m.synced + ","
                        + m.district.replace(",".toRegex(), ";") + ","
                        + m.county.replace(",".toRegex(), ";") + ","
                        + m.subCounty.replace(",".toRegex(), ";") + ","
                        + m.parish.replace(",".toRegex(), ";"))
                printWriter.println(detailsLine)
            }
            printWriter?.close()

        }
    }


    fun exportVillage() {
        val villageTable = VillageTable(context)
        val villages = villageTable.villageData
        val headers = VillageTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("villages")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (m in villages) {
                val detailsLine = (m.id + ","
                        + m.villageName.replace(",".toRegex(), ";") + ","
                        + m.mappingId.replace(",".toRegex(), ";") + ","
                        + m.lat + ","
                        + m.lon + ","
                        + m.country.replace(",".toRegex(), ";") + ","
                        + m.district.replace(",".toRegex(), ";") + ","
                        + m.county.replace(",".toRegex(), ";") + ","
                        + m.subCountyId.replace(",".toRegex(), ";") + ","
                        + m.parish.replace(",".toRegex(), ";") + ","
                        + m.communityUnit.replace(",".toRegex(), ";") + ","
                        + m.ward.replace(",".toRegex(), ";") + ","
                        + m.linkFacilityId.replace(",".toRegex(), ";") + ","
                        + m.areaChiefName.replace(",".toRegex(), ";") + ","
                        + m.areaChiefPhone.replace(",".toRegex(), ";") + ","
                        + m.distanceToBranch + ","
                        + m.transportCost + ","
                        + m.distanceToMainRoad + ","
                        + m.noOfHouseholds + ","
                        + m.mohPoplationDensity + ","
                        + m.estimatedPopulationDensity + ","
                        + m.economicStatus + ","
                        + m.distanceToNearestHealthFacility + ","
                        + m.actLevels + ","
                        + m.actPrice + ","
                        + m.mrdtLevels + ","
                        + m.mrdtPrice + ","
                        + m.presenceOfHostels + ","
                        + m.presenceOfEstates + ","
                        + m.numberOfFactories + ","
                        + m.presenceOfDistributors + ","
                        + m.distributorsInTheArea + ","
                        + m.traderMarket + ","
                        + m.largeSupermarket + ","
                        + m.ngosGivingFreeDrugs + ","
                        + m.ngoDoingIccm + ","
                        + m.ngoDoingMhealth + ","
                        + m.nameOfNgoDoingIccm.replace(",".toRegex(), ";") + ","
                        + m.nameOfNgoDoingMhealth.replace(",".toRegex(), ";") + ","
                        + m.privateFacilityForAct.replace(",".toRegex(), ";") + ","
                        + m.privateFacilityForMrdt.replace(",".toRegex(), ";") + ","
                        + m.dateAdded + ","
                        + m.comment.replace(",".toRegex(), ";") + ","
                        + m.isSynced + ","
                        + m.chvsTrained + ","
                        + m.isBracOperating + ","
                        + m.safaricomSignalStrength + ","
                        + m.mtnSignalStrength + ","
                        + m.airtelSignalStrength + ","
                        + m.orangeSignalStrength + ","
                        + m.actStock)
                printWriter.println(detailsLine)
            }
            printWriter?.close()
        }

    }

    fun exportParish() {
        val parishTable = ParishTable(context)
        val parishes = parishTable.parishData
        val headers = parishTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("parish")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (p in parishes) {
                val detailsLine = (p.id + ","
                        + p.name.replace(",".toRegex(), ";") + ","
                        + p.country.replace(",".toRegex(), ";") + ","
                        + p.parent.replace(",".toRegex(), ";") + ","
                        + p.mapping.replace(",".toRegex(), ";") + ","
                        + UserTable(context).getUserById(p.addedBy!!)!!.name + ","
                        + p.contactPerson.replace(",".toRegex(), ";") + ","
                        + p.contactPersonPhone.replace(",".toRegex(), ";") + ","
                        + p.comment.replace(",".toRegex(), ";") + ","
                        + p.synced + ","
                        + p.dateAdded)
                printWriter.println(detailsLine)
            }
            printWriter?.close()

        }
    }

    fun exportMapping() {
        val mappingTable = MappingTable(context)
        val mappings = mappingTable.mappingData
        val headers = mappingTable.columns
        if (Environment.MEDIA_MOUNTED != state) {
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show()
            return
        } else {
            val printWriter = exportCsv("mapping")
            val csvHeader = TextUtils.join(",", headers)
            printWriter!!.println(csvHeader)
            for (m in mappings) {
                val detailsLine = (m.id + ","
                        + m.mappingName.replace(",".toRegex(), ";") + ","
                        + m.country.replace(",".toRegex(), ";") + ","
                        + m.county.replace(",".toRegex(), ";") + ","
                        + UserTable(context).getUserById(m.addedBy!!)!!.name + ","
                        + m.contactPerson.replace(",".toRegex(), ";") + ","
                        + m.contactPersonPhone.replace(",".toRegex(), ";") + ","
                        + m.comment.replace(",".toRegex(), ";") + ","
                        + m.dateAdded + ","
                        + m.isSynced + ","
                        + m.district.replace(",".toRegex(), ";") + ","
                        + m.subCounty.replace(",".toRegex(), ";"))
                printWriter.println(detailsLine)
            }
            printWriter?.close()

        }

    }

    private fun exportCsv(dataToExport: String): PrintWriter? {
        val exportDir = File(Environment.getExternalStorageDirectory().toString() + "/Download/tremap/")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file: File
        var printWriter: PrintWriter? = null
        file = File(exportDir, "$dataToExport.csv")
        try {
            file.createNewFile()
            printWriter = PrintWriter(FileWriter(file))

        } catch (e: Exception) {
        }

        return printWriter
    }
}
