package com.ghd.kg.ghd.service.interfaces;

import com.ghd.kg.ghd.common.BookingStatus;
import com.ghd.kg.ghd.db.entity.Bookings;
import com.ghd.kg.ghd.model.request.BookingRequest;

public interface IBookingService {
    Bookings addBooking(BookingRequest request);

    void updateBookingStatus(BookingStatus status, Long id);
}
