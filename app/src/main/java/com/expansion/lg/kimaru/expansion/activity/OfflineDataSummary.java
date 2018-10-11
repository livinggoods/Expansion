package com.expansion.lg.kimaru.expansion.activity;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.dbhelpers.OfflineDataAdapter;
import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.OfflineEntity;
import com.expansion.lg.kimaru.expansion.service.RecruitmentsSyncServiceAdapter;
import com.expansion.lg.kimaru.expansion.tables.ChewReferralTable;
import com.expansion.lg.kimaru.expansion.tables.CommunityUnitTable;
import com.expansion.lg.kimaru.expansion.tables.ExamTable;
import com.expansion.lg.kimaru.expansion.tables.InterviewTable;
import com.expansion.lg.kimaru.expansion.tables.LinkFacilityTable;
import com.expansion.lg.kimaru.expansion.tables.MappingTable;
import com.expansion.lg.kimaru.expansion.tables.ParishTable;
import com.expansion.lg.kimaru.expansion.tables.PartnerActivityTable;
import com.expansion.lg.kimaru.expansion.tables.PartnersTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.RegistrationTable;
import com.expansion.lg.kimaru.expansion.tables.VillageTable;

import java.util.ArrayList;
import java.util.List;

public class OfflineDataSummary extends AppCompatActivity {

    OfflineDataAdapter adapter;

    Button btnSync;

    private Object mSyncObserverHandle = null;

    private SyncStatusObserver mSyncStatusObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data_summary);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_entities);
        adapter = new OfflineDataAdapter(this, getOfflineEntities());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSync = (Button) findViewById(R.id.btn_cta_sync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecruitmentsSyncServiceAdapter.syncImmediately(OfflineDataSummary.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Account account = RecruitmentsSyncServiceAdapter.getSyncAccount(this);

        boolean syncActive = ContentResolver.isSyncActive(
                account, getString(R.string.content_authority));
        boolean syncPending = ContentResolver.isSyncPending(
                account, getString(R.string.content_authority));

        if (syncActive || syncPending) {
            btnSync.setEnabled(false);
            btnSync.setText("SYNCING ...");
        } else {
            btnSync.setEnabled(true);
            btnSync.setText("SYNC");
        }

        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;

        mSyncStatusObserver = new SyncStatusObserver() {

            @Override
            public void onStatusChanged(int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (account == null) {
                            return;
                        }

                        boolean syncActive = ContentResolver.isSyncActive(
                                account, getString(R.string.content_authority));
                        boolean syncPending = ContentResolver.isSyncPending(
                                account, getString(R.string.content_authority));

                        if (syncActive || syncPending) {
                            btnSync.setEnabled(false);
                            btnSync.setText("SYNCING ...");
                        } else {
                            btnSync.setEnabled(true);
                            btnSync.setText("SYNC");

                            adapter.data = getOfflineEntities();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };

        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
            mSyncStatusObserver = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<OfflineEntity> getOfflineEntities() {
        List<OfflineEntity> data = new ArrayList<>();

        data.add(getRecruitments());
        data.add(getRegistrations());
        data.add(getExams());
        data.add(getInterviews());
        data.add(getCommunityUnits());
        data.add(getReferrals());
        data.add(getMapping());
        data.add(getLinkFacilities());
        data.add(getVillages());
        data.add(getParishes());
        data.add(getPartners());
        data.add(getPartnerActivities());

        return data;
    }

    public OfflineEntity getRecruitments() {
        RecruitmentTable table = new RecruitmentTable(this);
        OfflineEntity datum = new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getRecruitmentCount(), table.getPendingRecordCount());
        return datum;
    }

    public OfflineEntity getRegistrations() {
        RegistrationTable table = new RegistrationTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getRegistrationCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getExams() {
        ExamTable table = new ExamTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getExamCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getInterviews() {
        InterviewTable table = new InterviewTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getCommunityUnits() {
        CommunityUnitTable table = new CommunityUnitTable(this);
        return new OfflineEntity(table.CU_JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getReferrals() {
        ChewReferralTable table = new ChewReferralTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getMapping() {
        MappingTable table = new MappingTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getLinkFacilities() {
        LinkFacilityTable table = new LinkFacilityTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getVillages() {
        VillageTable table = new VillageTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getParishes() {
        ParishTable table = new ParishTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getPartners() {
        PartnersTable table = new PartnersTable(this);
        return new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
    }

    public OfflineEntity getPartnerActivities() {
        PartnerActivityTable table = new PartnerActivityTable(this);
        OfflineEntity datum = new OfflineEntity(table.JSON_ROOT, System.currentTimeMillis(), table.getAllRecordCount(), table.getPendingRecordCount());
        return datum;
    }
}
