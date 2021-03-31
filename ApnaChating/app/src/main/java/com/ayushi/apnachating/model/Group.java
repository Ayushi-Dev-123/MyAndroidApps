package com.ayushi.apnachating.model;

import java.io.Serializable;

public class Group implements Serializable {
    private String groupId;
    private String createdBy;
    private String createdAt;
    private String icon;
    private String description;
    private String groupName;

    public Group() {

    }

    public Group(String groupId, String createdBy, String createdAt, String icon, String description, String groupName) {
        this.groupId = groupId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.icon = icon;
        this.description = description;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
