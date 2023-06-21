package com.ghd.kg.ghd.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    String applicantName;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime arrivalDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime departureDate;
    Integer adultsCount;
    String phones;
}
