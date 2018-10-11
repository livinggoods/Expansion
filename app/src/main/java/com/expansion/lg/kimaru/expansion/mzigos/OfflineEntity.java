package com.expansion.lg.kimaru.expansion.mzigos;

public class OfflineEntity {

    String name;
    long lastSync;
    long allRecords;
    long pendingRecords;

    public OfflineEntity(String name, long lastSync, long allRecords, long pendingRecords) {
        this.name = name;
        this.lastSync = lastSync;
        this.allRecords = allRecords;
        this.pendingRecords = pendingRecords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public long getAllRecords() {
        return allRecords;
    }

    public void setAllRecords(int allRecords) {
        this.allRecords = allRecords;
    }

    public long getPendingRecords() {
        return pendingRecords;
    }

    public void setPendingRecords(int pendingRecords) {
        this.pendingRecords = pendingRecords;
    }
}
