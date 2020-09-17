package com.flyright.flyright.repository;

import com.flyright.flyright.model.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
    Flight findFlightByFlightNumber(String flightNumber);

    @Query(value = "SELECT * FROM flight f WHERE f.take_off_point = :takeOffPoint and f.destination_point = :destinationPoint and date(f.take_off_time) = date(:takeOffTime) and f.available_seats > 0", nativeQuery = true)
    List<Flight> searchAvailableFlights(String takeOffPoint, String destinationPoint, Date takeOffTime);
}
