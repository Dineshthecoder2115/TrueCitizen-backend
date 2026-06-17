package com.project.FixMyStreet.Models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "citizens")
public class CitizenProfile {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String Name;

    private String addressLine1;

    private String city;

    private String district;

    private String state;

    private String pincode;
}
