package com.doctor.controller;

import com.doctor.dao.DoctorRepository;
import com.doctor.dao.PatientRepository;
import com.doctor.dao.VisitRepository;
import com.doctor.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.doctor.model.Doctor;
import com.doctor.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    VisitRepository visitRepository;

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Patient patient, ModelMap modelMap) {
        patientRepository.save(patient);
        modelMap.addAttribute("patient", patient);
        return "add";
    }

    @PostMapping("/addconfirmdoct")
    public String addDoctorConfirmation(@ModelAttribute Doctor doctor) {
        doctorRepository.save(doctor);
        return "addconfirmdoct";
    }

    @GetMapping("/adddoctor")
    public String addDoctor() {
        return "adddoctor";
    }

    @GetMapping("/doctors")
    public String showDoctors(ModelMap modelMap) {
        modelMap.addAttribute("doctors", doctorRepository.findAll());
        return "doctors";
    }

    @GetMapping("doctor/patients/{id}")
    public String showDocPatients(@PathVariable Long id, ModelMap modelMap) {
        modelMap.addAttribute("patients", patientRepository.findByDoctor(doctorRepository.findById(id).get()));
        return "docpatients";
    }

    @GetMapping("doctor/visits/{id}")
    public String showDocVisits(@PathVariable Long id, ModelMap modelMap) {
        List<Patient> patientList = patientRepository.findByDoctor(doctorRepository.findById(id).get());
        List<Visit> visits = new ArrayList<>();
        for (Patient p : patientList) {
            visits.addAll(p.getVisits());
        }
        modelMap.addAttribute("visits", visits);
        return "docvisits";
    }

    @GetMapping("/patients")
    public String showPatients(ModelMap modelMap) {
        int size = 0;
        for (Patient p : patientRepository.findAll()) {
            size++;
        }
        if (size == 0) return "nopatients";

        modelMap.addAttribute("patients", patientRepository.findAll());

        return "patients";
    }

    @GetMapping("/addpatient")
    public String addPatient(ModelMap modelMap) {
        int size = 0;
        for (Doctor d : doctorRepository.findAll()) {
            size++;
        }
        if (size == 0) return "nodoctors";
        modelMap.addAttribute("doctors", doctorRepository.findAll());
        return "addpatient";
    }

    @GetMapping("/planvisit")
    public String planVisit(ModelMap modelMap) {
        int size = 0;
        for (Patient p : patientRepository.findAll()) {
            size++;
        }
        if (size == 0) return "nopatients";
        modelMap.addAttribute("patients", patientRepository.findAll());
        return "planvisit";
    }

    @PostMapping("/planvisitconfirmation")
    public String confirmVisit(@RequestParam int day, @RequestParam int month,@RequestParam int year, @RequestParam int hour, @RequestParam int minute, @RequestParam Long patient, ModelMap modelMap) {
        modelMap.addAttribute("patient", patientRepository.findById(patient).get());

        StringBuilder sb=new StringBuilder();

        sb.append(""+month+"/");
        sb.append(""+day+"/");
        sb.append(year);

        String date=sb.toString();

        sb=new StringBuilder();


        sb.append(""+hour+":");

        sb.append(""+minute);

        String time=sb.toString();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate visitDate = LocalDate.parse(date, dateFormat);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:m");
        LocalTime visitTime = LocalTime.parse(time, timeFormatter);


        Visit visit = new Visit(visitDate, visitTime, patientRepository.findById(patient).get());
        modelMap.addAttribute("visit", visit);
        visitRepository.save(visit);
        return "planvisitconf";
    }

    @GetMapping("/patient/visits/{id}")
    public String showVisits(@PathVariable Long id, ModelMap modelMap) {
        if (visitRepository.findByPatient(patientRepository.findById(id).get()).isEmpty()) return "novisits";
        modelMap.addAttribute("visits", visitRepository.findByPatient(patientRepository.findById(id).get()));
        return "showvisits";
    }

    @GetMapping("/patient/visits/delete/{id}")
    public String deleteVisit(@PathVariable Long id, ModelMap modelMap) {
        modelMap.addAttribute("visit", visitRepository.findById(id).get());
        visitRepository.deleteById(id);
        return "deletevisitconf";
    }

    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id, ModelMap modelMap) {
        modelMap.addAttribute("patient", patientRepository.findById(id).get());
        visitRepository.deleteAll(visitRepository.findByPatient(patientRepository.findById(id).get()));
        patientRepository.deleteById(id);
        return "deletepatientconf";
    }

}
