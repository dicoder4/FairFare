package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    // POST /groups
    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        // (Optional) you can validate that all memberIds exist in User collection
        // For now we just save the group as-is
        if (group.getMemberIds() == null) {
            group.setMemberIds(new ArrayList<>());
        }
        return groupRepository.insert(group);
    }

    // GET /groups/{groupId}
    @GetMapping("/{groupId}")
    public Group getGroupById(@PathVariable String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }

    // POST /groups/{groupId}/members  – add new members to a group
    @PostMapping("/{groupId}/members")
    public Group addMembersToGroup(@PathVariable String groupId,
                                   @RequestBody GroupMembersRequest request) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        List<String> existing = group.getMemberIds();
        if (existing == null) {
            existing = new ArrayList<>();
        }

        // avoid duplicates using a Set
        Set<String> memberSet = new HashSet<>(existing);
        if (request.getMemberIds() != null) {
            memberSet.addAll(request.getMemberIds());
        }

        group.setMemberIds(new ArrayList<>(memberSet));
        return groupRepository.save(group);
    }
}
