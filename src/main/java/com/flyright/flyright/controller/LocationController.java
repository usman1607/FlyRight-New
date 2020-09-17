package com.flyright.flyright.controller;

import com.flyright.flyright.model.Aircraft;
import com.flyright.flyright.model.Location;
import com.flyright.flyright.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;


    @RequestMapping(value = "/locations/list", method = RequestMethod.GET)
    public String locations(Model model){
        model.addAttribute("locations", locationRepository.findAll());
        return "location/list";
    }

    @RequestMapping(value = "/locations/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "location/create";
    }

    @RequestMapping(value = "/locations/add", method = RequestMethod.POST)
    public String add(Model model, @RequestParam String code) {

        Location location = new Location(code);
        locationRepository.save(location);
        return "redirect:/locations/list";
    }

    @RequestMapping(value = "/locations/edit/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") int id, Model model) {

        model.addAttribute("location", locationRepository.findById(id).get());
        return "location/edit";
    }

    @RequestMapping(value = "/locations/update", method = RequestMethod.POST)
    public String updateCode(Model model, @RequestParam int id, @RequestParam String code) {

        Location location= locationRepository.findById(id).get();
        location.setCode(code);

        locationRepository.save(location);

        return "redirect:/locations/list";
    }

    @RequestMapping(value = "/locations/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable("id") int id, Model model) {

        Location location = locationRepository.findById(id).get();

        locationRepository.delete(location);
        return "redirect:/locations/list";
    }

    @RequestMapping(value = "locations/to-from", method = RequestMethod.GET)
    public String to_from(Model model){
        model.addAttribute("locations", locationRepository.findAll());
        return "/index";
    }

}
