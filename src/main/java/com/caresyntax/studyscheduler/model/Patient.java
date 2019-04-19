/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.caresyntax.studyscheduler.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 * Simple JavaBean domain object representing a Patient.
 *
 * @author Mihail Adamenko
 */
@Entity
@Table
public class Patient extends NamedEntity {

    public enum SEX {
        unknown, male, female
    }
    @Column
    private SEX sex;


    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient", fetch = FetchType.EAGER)
    private List<Study> studies;

    public SEX getSex() {
        return sex;
    }

    public void setSex(SEX sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    @Override
    public String toString() {
        return "Patient{" +
            "sex=" + sex +
            ", birthDate=" + birthDate +
            '}';
    }
}
