package com.iv1201.recruitmentapplication.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@MappedSuperclass
public abstract class TimestampedTable {

    @Column(name = "created", nullable = false)
    private long created;

    @Column(name = "updated", nullable = false)
    private long updated;


    @Column(name = "version", nullable = false)
    private long version;

    @PrePersist
    protected void onCreate() {
        updated = created = Instant.now().toEpochMilli();
        version = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Instant.now().toEpochMilli();
        version = version + 1;
    }

    public long getVersion() {
        return version;
    }

    public long getCreated() {
        return created;
    }


    public long getUpdated() {
        return updated;
    }
}
