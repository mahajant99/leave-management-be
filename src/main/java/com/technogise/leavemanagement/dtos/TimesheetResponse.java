package com.technogise.leavemanagement.dtos;

import java.util.List;

public class TimesheetResponse {
    private int activity;
    private int project;
    private int user;
    private List<String> tags;
    private int id;
    private String begin;
    private String end;
    private int duration;
    private String description;
    private double rate;
    private double internalRate;
    private Double fixedRate;
    private double hourlyRate;
    private boolean exported;
    private boolean billable;
    private List<MetaField> metaFields;

    public int getActivity() {
        return activity;
    }
    public void setActivity(int activity) {
        this.activity = activity;
    }
    public int getProject() {
        return project;
    }
    public void setProject(int project) {
        this.project = project;
    }
    public int getUser() {
        return user;
    }
    public void setUser(int user) {
        this.user = user;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
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
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public double getInternalRate() {
        return internalRate;
    }
    public void setInternalRate(double internalRate) {
        this.internalRate = internalRate;
    }
    public Double getFixedRate() {
        return fixedRate;
    }
    public void setFixedRate(Double fixedRate) {
        this.fixedRate = fixedRate;
    }
    public double getHourlyRate() {
        return hourlyRate;
    }
    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    public boolean isExported() {
        return exported;
    }
    public void setExported(boolean exported) {
        this.exported = exported;
    }
    public boolean isBillable() {
        return billable;
    }
    public void setBillable(boolean billable) {
        this.billable = billable;
    }
    public List<MetaField> getMetaFields() {
        return metaFields;
    }
    public void setMetaFields(List<MetaField> metaFields) {
        this.metaFields = metaFields;
    }
    
    public static class MetaField {
        private String name;
        private String value;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
}