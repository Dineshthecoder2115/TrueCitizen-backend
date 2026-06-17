package com.project.FixMyStreet.Repository;

import com.project.FixMyStreet.Models.ComplaintTimeline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintTimelineRepository
        extends MongoRepository<ComplaintTimeline, String> {

    List<ComplaintTimeline>
    findByComplaintIdOrderByCreatedAtAsc(
            String complaintId
    );
}