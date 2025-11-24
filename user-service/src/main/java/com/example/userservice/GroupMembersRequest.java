package com.example.userservice;

import java.util.List;

public class GroupMembersRequest {

    private List<String> memberIds;

    public GroupMembersRequest() {
    }

    public GroupMembersRequest(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
