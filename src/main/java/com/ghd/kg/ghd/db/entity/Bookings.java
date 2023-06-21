package com.ghd.kg.ghd.db.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ghd.kg.ghd.common.BookingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking", schema = "core")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "rec_time")
    LocalDateTime recTime = LocalDateTime.now();

    @Column(name = "is_deleted")
    boolean isDeleted = false;

    @Column(name = "applicant_name")
    String applicantName;

    @Column(name = "arrival_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime arrivalDate;

    @Column(name = "departure_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime departureDate;

    @Column(name = "adults_count")
    Integer adultsCount;

    @Column(name = "children_count")
    Integer childrenCount;

    String phones;

    @Enumerated(EnumType.ORDINAL)
    BookingStatus state;
}
