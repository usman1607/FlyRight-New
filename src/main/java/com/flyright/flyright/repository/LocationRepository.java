package com.flyright.flyright.repository;

import com.flyright.flyright.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Integer> {
    Location findByCode(String code);
}
