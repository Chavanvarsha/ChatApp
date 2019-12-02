package com.chatapp.model;

import java.util.List;

public class GroupMetadata {

    private String groupname;
    private List<String> useridlist;
    private String grouppic_url;
    private String description;

    public List<String> getUseridlist() {
        return useridlist;
    }

    public void setUseridlist(List<String> useridlist) {
        this.useridlist = useridlist;
    }

    public String getGrouppic_url() {
        return grouppic_url;
    }

    public void setGrouppic_url(String grouppic_url) {
        this.grouppic_url = grouppic_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
