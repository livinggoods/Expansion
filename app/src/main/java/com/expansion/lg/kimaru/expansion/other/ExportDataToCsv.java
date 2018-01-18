package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.MobilizationTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kimaru on 8/22/17.
 */

public class ExportDataToCsv {
    Context context;
    String state;

    public ExportDataToCsv(Context context) {
        this.context = context;
        state = Environment.getExternalStorageState();
    }

    public void exportChewReferral(){
        ChewReferralTable chewReferralTable = new ChewReferralTable(context);
        List<ChewReferral> chewReferrals = chewReferralTable.getChewReferralData();
        String[] headers = chewReferralTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("chew_referrals");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (ChewReferral p:chewReferrals){
                String detailsLine = p.getId()+","
                        + p.getName().replaceAll(",", ";")+","
                        + p.getPhone().replaceAll(",", ";")+","
                        + p.getTitle().replaceAll(",", ";")+","
                        + p.getCountry().replaceAll(",", ";")+","
                        + p.getRecruitmentId().replaceAll(",", ";")+","
                        + p.getSynced()+","
                        + p.getCounty().replaceAll(",", ";")+","
                        + p.getDistrict().replaceAll(",", ";")+","
                        + p.getSubCounty().replaceAll(",", ";")+","
                        + p.getCommunityUnit().replaceAll(",", ";")+","
                        + p.getVillage().replaceAll(",", ";")+","
                        + p.getMapping().replaceAll(",", ";")+","
                        + p.getMobilization().replaceAll(",", ";")+","
                        + p.getLon().replaceAll(",", ";")+","
                        + p.getLat().replaceAll(",", ";");
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();

        }
    }


    public void exportCommunityUnit(){
        CommunityUnitTable communityUnitTable = new CommunityUnitTable(context);
        List<CommunityUnit> communityUnits = communityUnitTable.getCommunityUnitData();
        String[] headers = communityUnitTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("community_units");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (CommunityUnit cm:communityUnits){
                String detailsLine = cm.getId()+","
                        + (cm.getCommunityUnitName() == null ? "" : cm.getCommunityUnitName().replaceAll(",", ";"))+","
                        + (cm.getMappingId() == null ? "" : cm.getMappingId().replaceAll(",", ";"))+","
                        + (cm.getLat() == null ? "" : cm.getLat().toString())+","
                        + (cm.getLon() == null ? "" : cm.getLon().toString())+","
                        + (cm.getCountry() == null ? "" : cm.getCountry().replaceAll(",",";"))+","
                        + (cm.getSubCountyId() == null ? "" : cm.getSubCountyId().replaceAll(",",";"))+","
                        + (cm.getLinkFacilityId() == null ? "" : cm.getLinkFacilityId().replaceAll(",",";"))+","
                        + (cm.getAreaChiefName() == null ? "" : cm.getAreaChiefName().replaceAll(",",";"))+","
                        + (cm.getAreaChiefPhone() == null ? "" : cm.getAreaChiefPhone().replaceAll(",",";"))+","
                        + (cm.getWard() == null ? "" : cm.getWard().replaceAll(",",";"))+","
                        + (cm.getEconomicStatus() == null ? "" : cm.getEconomicStatus().replaceAll(",",";"))+","
                        + (cm.getPrivateFacilityForAct() == null ? "" : cm.getPrivateFacilityForAct().replaceAll(",",";"))+","
                        + (cm.getPrivateFacilityForMrdt() == null ? "" : cm.getPrivateFacilityForMrdt().replaceAll(",",";"))+","
                        + (cm.getNameOfNgoDoingIccm() == null ? "" : cm.getNameOfNgoDoingIccm().replaceAll(",",";"))+","
                        + (cm.getNameOfNgoDoingMhealth() == null ? "" : cm.getNameOfNgoDoingMhealth().replaceAll(",",";"))+","
                        + cm.getDateAdded()+","
                        + new UserTable(context).getUserById(Integer.valueOf(String.valueOf(cm.getAddedBy()))).getName()+","
                        + cm.getNumberOfChvs()+","
                        + cm.getHouseholdPerChv()+","
                        + cm.getNumberOfVillages()+","
                        + cm.getDistanceToBranch()+","
                        + cm.getTransportCost()+","
                        + cm.getDistanceTOMainRoad()+","
                        + cm.getNoOfHouseholds()+","
                        + cm.getMohPoplationDensity()+","
                        + cm.getEstimatedPopulationDensity()+","
                        + cm.getDistanceTONearestHealthFacility()+","
                        + cm.getActLevels()+","
                        + cm.getActPrice()+","
                        + cm.getMrdtLevels()+","
                        + cm.getMrdtPrice()+","
                        + cm.getNoOfDistibutors()+","
                        + cm.isChvsTrained()+","
                        + cm.isPresenceOfEstates()+","
                        + cm.isPresenceOfFactories()+","
                        + cm.isPresenceOfHostels()+","
                        + cm.isTraderMarket()+","
                        + cm.isLargeSupermarket()+","
                        + cm.isNgosGivingFreeDrugs()+","
                        + cm.isNgoDoingIccm()+","
                        + cm.isNgoDoingMhealth();
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();

        }
    }


    public void exportMobilization(){
        MobilizationTable mobilizationTable = new MobilizationTable(context);
        List<Mobilization> mobilizations = mobilizationTable.getMobilizationData();
        String[] headers = mobilizationTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("mobilization");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (Mobilization m:mobilizations){
                String detailsLine = m.getId()+","
                        + m.getName().replaceAll(",", ";")+","
                        + m.getMappingId().replaceAll(",", ";")+","
                        + m.getCountry().replaceAll(",", ";")+","
                        + new UserTable(context).getUserById(m.getAddedBy()).getName()+","
                        + m.getComment().replaceAll(",", ";")+","
                        + m.getDateAdded()+","
                        + m.getSynced()+","
                        + m.getDistrict().replaceAll(",", ";")+","
                        + m.getCounty().replaceAll(",", ";")+","
                        + m.getSubCounty().replaceAll(",", ";")+","
                        + m.getParish().replaceAll(",", ";");
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();

        }
    }


    public void exportVillage(){
        VillageTable villageTable = new VillageTable(context);
        List<Village> villages = villageTable.getVillageData();
        String[] headers = villageTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("villages");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (Village m:villages){
                String detailsLine = m.getId()+","
                        + m.getVillageName().replaceAll(",", ";")+","
                        + m.getMappingId().replaceAll(",", ";")+","
                        + m.getLat()+","
                        + m.getLon()+","
                        + m.getCountry().replaceAll(",", ";")+","
                        + m.getDistrict().replaceAll(",", ";")+","
                        + m.getCounty().replaceAll(",", ";")+","
                        + m.getSubCountyId().replaceAll(",", ";")+","
                        + m.getParish().replaceAll(",", ";")+","
                        + m.getCommunityUnit().replaceAll(",", ";")+","
                        + m.getWard().replaceAll(",", ";")+","
                        + m.getLinkFacilityId().replaceAll(",", ";")+","
                        + m.getAreaChiefName().replaceAll(",", ";")+","
                        + m.getAreaChiefPhone().replaceAll(",", ";")+","
                        + m.getDistanceToBranch()+","
                        + m.getTransportCost()+","
                        + m.getDistanceToMainRoad()+","
                        + m.getNoOfHouseholds()+","
                        + m.getMohPoplationDensity()+","
                        + m.getEstimatedPopulationDensity()+","
                        + m.getEconomicStatus()+","
                        + m.getDistanceToNearestHealthFacility()+","
                        + m.getActLevels()+","
                        + m.getActPrice()+","
                        + m.getMrdtLevels()+","
                        + m.getMrdtPrice()+","
                        + m.getPresenceOfHostels()+","
                        + m.getPresenceOfEstates()+","
                        + m.getNumberOfFactories()+","
                        + m.getPresenceOfDistributors()+","
                        + m.getDistributorsInTheArea()+","
                        + m.getTraderMarket()+","
                        + m.getLargeSupermarket()+","
                        + m.getNgosGivingFreeDrugs()+","
                        + m.getNgoDoingIccm()+","
                        + m.getNgoDoingMhealth()+","
                        + m.getNameOfNgoDoingIccm().replaceAll(",",";")+","
                        + m.getNameOfNgoDoingMhealth().replaceAll(",",";")+","
                        + m.getPrivateFacilityForAct().replaceAll(",",";")+","
                        + m.getPrivateFacilityForMrdt().replaceAll(",",";")+","
                        + m.getDateAdded()+","
                        + m.getComment().replaceAll(",",";")+","
                        + m.isSynced()+","
                        + m.getChvsTrained()+","
                        + m.isBracOperating()+","
                        + m.getSafaricomSignalStrength()+","
                        + m.getMtnSignalStrength()+","
                        + m.getAirtelSignalStrength()+","
                        + m.getOrangeSignalStrength()+","
                        + m.getActStock();
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();
        }

    }
    public void exportParish(){
        ParishTable parishTable = new ParishTable(context);
        List<Parish> parishes = parishTable.getParishData();
        String[] headers = parishTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("parish");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (Parish p:parishes){
                String detailsLine = p.getId()+","
                        + p.getName().replaceAll(",", ";")+","
                        + p.getCountry().replaceAll(",", ";")+","
                        + p.getParent().replaceAll(",", ";")+","
                        + p.getMapping().replaceAll(",", ";")+","
                        + new UserTable(context).getUserById(p.getAddedBy()).getName()+","
                        + p.getContactPerson().replaceAll(",", ";")+","
                        + p.getContactPersonPhone().replaceAll(",", ";")+","
                        + p.getComment().replaceAll(",", ";")+","
                        + p.getSynced()+","
                        + p.getDateAdded();
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();

        }
    }
    public void exportMapping(){
        MappingTable mappingTable = new MappingTable(context);
        List<Mapping> mappings = mappingTable.getMappingData();
        String[] headers = mappingTable.columns;
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Media is not mounted.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PrintWriter printWriter = exportCsv("mapping");
            String csvHeader = TextUtils.join(",", headers);
            printWriter.println(csvHeader);
            for (Mapping m:mappings){
                String detailsLine = m.getId()+","
                        + m.getMappingName().replaceAll(",", ";")+","
                        + m.getCountry().replaceAll(",", ";")+","
                        + m.getCounty().replaceAll(",", ";")+","
                        + new UserTable(context).getUserById(m.getAddedBy()).getName()+","
                        + m.getContactPerson().replaceAll(",", ";")+","
                        + m.getContactPersonPhone().replaceAll(",", ";")+","
                        + m.getComment().replaceAll(",", ";")+","
                        + m.getDateAdded()+","
                        + m.isSynced()+","
                        + m.getDistrict().replaceAll(",", ";")+","
                        + m.getSubCounty().replaceAll(",", ";");
                printWriter.println(detailsLine);
            }
            if(printWriter != null) printWriter.close();

        }

    }

    private PrintWriter exportCsv(String dataToExport){
        File exportDir = new File(Environment.getExternalStorageDirectory() + "/Download/tremap/");
        if (!exportDir.exists()){
            exportDir.mkdirs();
        }
        File file;
        PrintWriter printWriter = null;
        file = new File(exportDir, dataToExport+".csv");
        try {
            file.createNewFile();
            printWriter = new PrintWriter(new FileWriter(file));

        }catch(Exception e){}
        return printWriter;
    }
}
