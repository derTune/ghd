package com.ghd.kg.ghd.db.repo;

import com.ghd.kg.ghd.db.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Bookings, Long> {
    @Modifying
    @Query("update Bookings b set b.state = :status where b.id = :id")
    void updateBookingStatus(int status, Long id);
}
