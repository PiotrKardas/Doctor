package com.doctor.dao;

import org.springframework.data.repository.CrudRepository;
import com.doctor.model.Doctor;
import com.doctor.model.Patient;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Long> {
    List<Patient> findByDoctor(Doctor doctor);
}
