package com.doctor.dao;

import org.springframework.data.repository.CrudRepository;
import com.doctor.model.Patient;
import com.doctor.model.Visit;

import java.util.List;

public interface VisitRepository extends CrudRepository<Visit, Long> {
    List<Visit> findByPatient(Patient patient);
}
