package com.ghd.kg.ghd.controller;

import com.ghd.kg.ghd.common.RestResponse;
import com.ghd.kg.ghd.model.request.BookingRequest;
import com.ghd.kg.ghd.service.interfaces.IBookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingController {
    final IBookingService bookingService;
    @PostMapping("/booking")
    public RestResponse addBooking(@RequestBody BookingRequest request) {
        return RestResponse.success(bookingService.addBooking(request));
    }
}
