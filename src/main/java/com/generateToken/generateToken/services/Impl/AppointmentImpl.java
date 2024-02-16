package com.generateToken.generateToken.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.generateToken.generateToken.dto.AppointmentDTOs;
import com.generateToken.generateToken.entities.Appointment;
import com.generateToken.generateToken.entities.Clinic;
import com.generateToken.generateToken.entities.Doctor;
import com.generateToken.generateToken.repositories.AppointmentRepository;
import com.generateToken.generateToken.repositories.ClinicRepository;
import com.generateToken.generateToken.repositories.DoctorRepository;
import com.generateToken.generateToken.services.AppointmentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AppointmentImpl implements AppointmentService {
  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  private DoctorRepository doctorRepository;

  @Autowired
  private ClinicRepository clinicRepository;

  @Override
  public List<Appointment> getByAbhaNumber(String abhaNumber) {
    return appointmentRepository.findByAbhaNumber(abhaNumber);
  }

  public List<Appointment> getByContactNumber(String contactNumber) {
    return appointmentRepository.findByContact(contactNumber);
  }

  public AppointmentDTOs getById(Long appointmentId) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

    AppointmentDTOs appointmentDto = new AppointmentDTOs();
    appointmentDto.setId(appointment.getId());
    appointmentDto.setName(appointment.getName());
    appointmentDto.setAbhaNumber(appointment.getAbhaNumber());
    appointmentDto.setContact(appointment.getContact());
    appointmentDto.setAge(appointment.getAge());
    appointmentDto.setGender(appointment.getGender());
    appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
    appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
    appointmentDto.setClinicLocation(appointment.getClinicLocation());

    return appointmentDto;
  }

  @Override
  public AppointmentDTOs bookAppointment(String email, Long clinicId, AppointmentDTOs appointmentDto) {

    Clinic clinic = clinicRepository.findById(clinicId)
        .orElseThrow(() -> new EntityNotFoundException("Clinic not found"));

    Doctor doctor = doctorRepository.findFirstByEmail(email);

    Appointment patientAppointment = new Appointment();
    patientAppointment.setId(appointmentDto.getId());
    patientAppointment.setName(appointmentDto.getName());
    patientAppointment.setContact(appointmentDto.getContact());
    patientAppointment.setAbhaNumber(appointmentDto.getAbhaNumber());

    patientAppointment.setAge(appointmentDto.getAge());
    patientAppointment.setGender(appointmentDto.getGender());
    patientAppointment.setAppointmentDate(appointmentDto.getAppointmentDate());
    patientAppointment.setAppointmentTime(appointmentDto.getAppointmentTime());
    patientAppointment.setClinicLocation(appointmentDto.getClinicLocation());
    patientAppointment.setClinic(clinic);
    patientAppointment.setDoctor(doctor);

    patientAppointment = appointmentRepository.save(patientAppointment);

    AppointmentDTOs appointmentDto1 = new AppointmentDTOs();
    appointmentDto1.setId(patientAppointment.getId());
    appointmentDto1.setName(patientAppointment.getName());
    appointmentDto1.setAbhaNumber(patientAppointment.getAbhaNumber());
    appointmentDto1.setContact(patientAppointment.getContact());
    appointmentDto1.setAge(patientAppointment.getAge());
    appointmentDto1.setGender(patientAppointment.getGender());
    appointmentDto1.setAppointmentDate(patientAppointment.getAppointmentDate());
    appointmentDto1.setAppointmentTime(patientAppointment.getAppointmentTime());
    appointmentDto1.setClinicLocation(patientAppointment.getClinicLocation());

    clinic.addAppointmentPatient(patientAppointment);
    doctor.addAppointmentPatient1(patientAppointment);

    patientAppointment = appointmentRepository.save(patientAppointment);
    clinic = clinicRepository.save(clinic);
    doctor = doctorRepository.save(doctor);

    return appointmentDto1;
  }

}
