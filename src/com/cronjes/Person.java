package com.cronjes;

import com.cronjes.csvlib.annotaions.CsvColumn;

public class Person {

    @CsvColumn("name")
    private String name;

    @CsvColumn("surname")
    private String surname;

    @CsvColumn("idnumber")
    private String idNUmber;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNUmber() {
        return idNUmber;
    }

    public void setIdNUmber(String idNUmber) {
        this.idNUmber = idNUmber;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", idNUmber='" + idNUmber + '\'' +
                '}';
    }
}
