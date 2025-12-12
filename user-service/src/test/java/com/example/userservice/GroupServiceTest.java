package com.example.userservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    private Group group;

    @BeforeEach
    void setUp() {
        group = new Group();
        group.setId("101");
        group.setName("Trip");
        group.setMemberIds(Arrays.asList("1", "2"));
    }

    @Test
    void createGroup_ShouldInitializeMembersAndSave() {
        // Given
        Group newGroup = new Group();
        newGroup.setName("Empty Group");
        // memberIds null initially
        
        when(groupRepository.insert(any(Group.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Group saved = groupService.createGroup(newGroup);

        // Then
        assertThat(saved.getMemberIds()).isNotNull(); // Should have initialized empty list
        verify(groupRepository).insert(newGroup);
    }

    @Test
    void getGroupById_WhenExists_ShouldReturnGroup() {
        // Given
        when(groupRepository.findById("101")).thenReturn(Optional.of(group));

        // When
        Group found = groupService.getGroupById("101");

        // Then
        assertThat(found.getName()).isEqualTo("Trip");
    }

    @Test
    void getGroupById_WhenMissing_ShouldThrowException() {
        // Given
        when(groupRepository.findById("99")).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> groupService.getGroupById("99"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void addMembers_ShouldMergeAndSave() {
        // Given
        when(groupRepository.findById("101")).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenAnswer(i -> i.getArguments()[0]);

        List<String> newMembers = Collections.singletonList("3");

        // When
        Group updated = groupService.addMembers("101", newMembers);

        // Then
        assertThat(updated.getMemberIds()).contains("1", "2", "3");
        verify(groupRepository).save(group);
    }
}
