package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // POST /groups
    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    // GET /groups/{groupId}
    @GetMapping("/{groupId}")
    public Group getGroupById(@PathVariable String groupId) {
        return groupService.getGroupById(groupId);
    }

    // GET /groups
    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    // POST /groups/{groupId}/members
    @PostMapping("/{groupId}/members")
    public Group addMembersToGroup(@PathVariable String groupId,
                                   @RequestBody GroupMembersRequest request) {
        return groupService.addMembers(groupId, request.getMemberIds());
    }
}
