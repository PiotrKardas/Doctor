package com.doctor.dao;

import org.springframework.data.repository.CrudRepository;
import com.doctor.model.Doctor;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
}
