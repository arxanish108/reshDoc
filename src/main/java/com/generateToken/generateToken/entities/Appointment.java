package com.generateToken.generateToken.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.generateToken.generateToken.Enum.Gender;
import com.generateToken.generateToken.dto.AppointmentDTOs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "Appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String contact;
  private String abhaNumber;
  private int age;
  @Enumerated(EnumType.STRING)
  private Gender gender;
  private LocalDate appointmentDate;
  private LocalTime appointmentTime;
  private String clinicLocation;

  @JsonIgnore
  @ManyToOne()
  @JoinColumn(name = "clinicId")
  private Clinic clinic;

  @ManyToOne
  @JoinColumn(name = "doctorId")
  private Doctor doctor;

  public Appointment() {
  }

  @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
  private List<Prescription> prescriptionList = new ArrayList<>();

  public AppointmentDTOs getAppointmentDto() {
    AppointmentDTOs appointmentDTOs = new AppointmentDTOs();
    appointmentDTOs.setId(this.getId());
    appointmentDTOs.setName(this.getName());
    appointmentDTOs.setAbhaNumber(this.getAbhaNumber());
    appointmentDTOs.setContact(this.getContact());
    // appointmentDTOs.setContact_number(this.contact_number);
    appointmentDTOs.setAge(this.age);
    appointmentDTOs.setGender(this.gender);
    appointmentDTOs.setAppointmentDate(this.appointmentDate);
    appointmentDTOs.setAppointmentTime(this.appointmentTime);
    appointmentDTOs.setClinicLocation(this.clinicLocation);
    return appointmentDTOs;
  }

}
