package com.event.sourcing.model.permission;

import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Permission {

    public enum Action {
        CREATE,
        RETRIEVE,
        UPDATE,
        DELETE
    }

    public enum Resource {
        USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Action action;

    @Column
    private Resource resource;

    public Permission() {
        //jpa constructor
    }

    public long getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(final Action action) {
        this.action = action;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(final Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("action", action)
                .add("resource", resource)
                .toString();
    }
}
