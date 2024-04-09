package com.technogise.leavemanagement.dtos;

public class TimesheetRequest {
    private String begin;
    private String end;
    private String project;
    private String activity;
    private String description;
    private String user;
    private String tags;
    private String exported;
    private String billable;

    public String getBegin() {
        return begin;
    }
    public void setBegin(String begin) {
        this.begin = begin;
    }
    public String getEnd() {
        return end;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public String getProject() {
        return project;
    }
    public void setProject(String project) {
        this.project = project;
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getExported() {
        return exported;
    }
    public void setExported(String exported) {
        this.exported = exported;
    }
    public String getBillable() {
        return billable;
    }
    public void setBillable(String billable) {
        this.billable = billable;
    }    
}