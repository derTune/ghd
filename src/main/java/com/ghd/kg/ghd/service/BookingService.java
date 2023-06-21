package com.ghd.kg.ghd.service;

import com.ghd.kg.ghd.common.BookingStatus;
import com.ghd.kg.ghd.db.entity.Bookings;
import com.ghd.kg.ghd.db.repo.BookingRepo;
import com.ghd.kg.ghd.model.request.BookingRequest;
import com.ghd.kg.ghd.service.interfaces.IBookingService;
import com.ghd.kg.ghd.telegram.BookingBot;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    final BookingRepo bookingRepo;
    @Lazy
    final BookingBot bot;

    @Override
    public Bookings addBooking(BookingRequest request) {
        Bookings booking = new Bookings();
        booking.setApplicantName(request.getApplicantName());
        booking.setArrivalDate(request.getArrivalDate());
        booking.setDepartureDate(request.getDepartureDate());
        booking.setAdultsCount(request.getAdultsCount());
        booking.setPhones(request.getPhones());
        booking.setState(BookingStatus.SENT);
        Bookings savedBooking = bookingRepo.save(booking);

        bot.sendNewBooking(savedBooking);
        return savedBooking;
    }

    @Transactional
    @Override
    public void updateBookingStatus(BookingStatus status, Long id) {
        bookingRepo.updateBookingStatus(status.ordinal(), id);
    }


}
