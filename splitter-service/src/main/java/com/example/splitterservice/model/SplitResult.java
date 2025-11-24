package com.example.splitterservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "splits")
public class SplitResult {

    @Id
    private String id;

    private String billId;
    private String groupId;

    private List<UserShare> userShares;

    public SplitResult() {}

    public SplitResult(String billId, String groupId, List<UserShare> userShares) {
        this.billId = billId;
        this.groupId = groupId;
        this.userShares = userShares;
    }

    public String getId() {
        return id;
    }

    public String getBillId() {
        return billId;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<UserShare> getUserShares() {
        return userShares;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setUserShares(List<UserShare> userShares) {
        this.userShares = userShares;
    }
}
