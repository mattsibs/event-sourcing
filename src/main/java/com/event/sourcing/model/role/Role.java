package com.event.sourcing.model.role;

import com.event.sourcing.model.permission.Permission;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "log_id")
    private String logId;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Permission> permissions = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(final String logId) {
        this.logId = logId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("logId", logId)
                .add("name", name)
                .add("permissions", permissions)
                .toString();
    }
}
