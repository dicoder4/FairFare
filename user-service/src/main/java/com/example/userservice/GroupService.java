package com.example.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(Group group) {
        if (group.getMemberIds() == null) {
            group.setMemberIds(new ArrayList<>());
        }
        return groupRepository.insert(group);
    }

    public Group getGroupById(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Group addMembers(String groupId, List<String> newMemberIds) {
        Group group = getGroupById(groupId);
        List<String> existing = group.getMemberIds();
        if (existing == null) {
            existing = new ArrayList<>();
        }

        Set<String> memberSet = new HashSet<>(existing);
        if (newMemberIds != null) {
            memberSet.addAll(newMemberIds);
        }

        group.setMemberIds(new ArrayList<>(memberSet));
        return groupRepository.save(group);
    }
}
