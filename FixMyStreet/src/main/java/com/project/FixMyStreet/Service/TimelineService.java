package com.project.FixMyStreet.Service;
import com.project.FixMyStreet.Models.ComplaintTimeline;
import com.project.FixMyStreet.Repository.ComplaintTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineService {

    @Autowired
    private ComplaintTimelineRepository timelineRepository;

    public void addTimelineEntry(
            String complaintId,
            String status,
            String title,
            String description){

        ComplaintTimeline timeline =
                ComplaintTimeline.builder()
                        .complaintId(complaintId)
                        .status(status)
                        .title(title)
                        .description(description)
                        .createdAt(LocalDateTime.now())
                        .build();

        timelineRepository.save(timeline);
    }

    // NEW METHOD
    public List<ComplaintTimeline> getTimeline(
            String complaintId) {

        return timelineRepository
                .findByComplaintIdOrderByCreatedAtAsc(
                        complaintId
                );
    }
}