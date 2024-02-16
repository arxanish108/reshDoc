package com.generateToken.generateToken.dto;

import java.time.LocalTime;
import java.util.List;

import com.generateToken.generateToken.Enum.DAYS;
import com.generateToken.generateToken.entities.Doctor;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClinicDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private String incharge;
    private String clinicName;
    private Double fees;
    @Enumerated(EnumType.STRING)
    private List<DAYS> days;
    private Doctor doctor;
    private Integer pincode;
    private LocalTime startTime;
    private LocalTime endTime;
    private String clinicContact;

}
