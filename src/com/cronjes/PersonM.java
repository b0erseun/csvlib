package com.cronjes;

import com.cronjes.csvlib.annotaions.CsvColumn;

import java.time.LocalDate;

public class PersonM {
    private String name;

    private String surname;

    private String idNUmber;

    LocalDate dateOfBirth;

    public PersonM() {
    }

    public String getName() {
        return name;
    }

    @CsvColumn("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    @CsvColumn("surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNUmber() {
        return idNUmber;
    }

    @CsvColumn("idnumber")
    public void setIdNUmber(String idNUmber) {
        this.idNUmber = idNUmber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @CsvColumn("dob")
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "PersonM{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", idNUmber='" + idNUmber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
