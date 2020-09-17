package com.flyright.flyright.controller;

import com.flyright.flyright.model.Aircraft;
import com.flyright.flyright.model.Flight;
import com.flyright.flyright.repository.AircraftRepository;
import com.flyright.flyright.repository.FlightRepository;
import com.flyright.flyright.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Controller
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AircraftRepository aircraftRepository;
    @Autowired
    private LocationRepository locationRepository;

    @RequestMapping(value = "/flights/list", method = RequestMethod.GET)
    public String flights(Model model){
        model.addAttribute("flights", flightRepository.findAll());
        return "flight/list";
    }

    @RequestMapping(value = "/flights/availableFlights", method = RequestMethod.GET)
    public String availableFlights(Model model, @RequestParam String takeOffPoint, @RequestParam String destinationPoint, @RequestParam String departure) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date departDate = formatter.parse(departure);
        model.addAttribute("available_flights", flightRepository.searchAvailableFlights(takeOffPoint, destinationPoint, departDate));
        return "flight/availableFlights";
    }

    @RequestMapping(value = "/flights/create/{id}", method = RequestMethod.GET)
    public String create(@PathVariable("id") int id,  Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("aircraft", aircraftRepository.findById(id).get());
        return "flight/create";
    }

    @RequestMapping(value = "/flights/add", method = RequestMethod.POST)
    public String add(Model model, @RequestParam int id, @RequestParam String flightNumber, @RequestParam String takeOffTime, @RequestParam String landingTime, @RequestParam String takeOffPoint, @RequestParam String destinationPoint, @RequestParam double price) throws ParseException {

        Aircraft aircraft = aircraftRepository.findById(id).get();
        int availableSeats = aircraft.getCapacity();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        //System.out.println(takeOffTime);
        Date utilDate = formatter.parse(takeOffTime);

        Date utilDate2 = formatter.parse(landingTime);

        Flight flight = new Flight(flightNumber, aircraft, utilDate, utilDate2, takeOffPoint, destinationPoint, price, availableSeats);
        flightRepository.save(flight);
        return "redirect:/flights/list";
    }

    @RequestMapping(value = "/flights/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") int id, Model model) {

        model.addAttribute("flight", flightRepository.findById(id).get());
        return "flight/edit";
    }

    @RequestMapping(value = "/flights/update", method = RequestMethod.POST)
    public String updateFlight(Model model, @RequestParam int id, @RequestParam String flightNumber, @RequestParam String takeOffTime, @RequestParam String landingTime, @RequestParam String takeOffPoint, @RequestParam String destinationPoint, @RequestParam double price, @RequestParam int availableSeats) throws ParseException {

        Flight flight= flightRepository.findById(id).get();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        Date takeOff_Time = formatter.parse(takeOffTime);
        flight.setTakeOffTime(takeOff_Time);

        Date landing_Time = formatter.parse(landingTime);
        flight.setLandingTime(landing_Time);

        flight.setAvailableSeats(availableSeats);
        flight.setDestinationPoint(destinationPoint);
        flight.setFlightNumber(flightNumber);
        flight.setPrice(price);
        flight.setTakeOffPoint(takeOffPoint);

        flightRepository.save(flight);

        return "redirect:/flights/list";
    }

    @RequestMapping(value = "/flights/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id, Model model) {

        Flight flight = flightRepository.findById(id).get();

        flightRepository.delete(flight);
        return "redirect:/flights/list";
    }
}
