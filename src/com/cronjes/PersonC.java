package com.cronjes;

import com.cronjes.csvlib.annotaions.CsvColumn;

public class PersonC {
    private String name;

    private String surname;

    private String idNUmber;

    public PersonC(
            @CsvColumn("name") String name,
            @CsvColumn("surname") String surname,
            @CsvColumn("idnumber") String idNUmber) {
        this.name = name;
        this.surname = surname;
        this.idNUmber = idNUmber;
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
        return "PersonC{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", idNUmber='" + idNUmber + '\'' +
                '}';
    }

}
