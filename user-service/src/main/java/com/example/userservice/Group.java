package com.example.userservice;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "groups")
public class Group {

    @Id
    private String id;

    private String name;
    private String description;

    // list of User ids belonging to this group
    private List<String> memberIds = new ArrayList<>();

    public Group() {
    }

    public Group(String name, String description, List<String> memberIds) {
        this.name = name;
        this.description = description;
        if (memberIds != null) {
            this.memberIds = memberIds;
        }
    }

    // getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
