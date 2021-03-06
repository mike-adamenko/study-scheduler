/*
 * Copyright 2019-present Mike Adamenko (mnadamenko@gmail.com)
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
package com.caresyntax.studyscheduler.dao;

import com.caresyntax.studyscheduler.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DAO tests
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class StudySchedulerDaoTests {

    @Autowired
    protected PatientRepository patientRepository;


    @Test
    public void shouldFindPatientsByName() {
        Collection<Patient> patients = this.patientRepository.findByName("George Franklin");
        assertThat(patients.size()).isEqualTo(1);

        patients = this.patientRepository.findByName("Daviss");
        assertThat(patients.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    public void shouldInsertPatient() {
        Collection<Patient> patients = this.patientRepository.findByName("Schultz");
        int found = patients.size();

        Patient patient = new Patient();
        patient.setName("Schultz");
        patient.setSex(Patient.SEX.male);
        patient.setBirthDate(LocalDate.now());
        this.patientRepository.save(patient);
        assertThat(patient.isNew()).isFalse();

        patients = this.patientRepository.findByName("Schultz");
        assertThat(patients.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    public void shouldUpdatePatient() {
        Patient patient = this.patientRepository.findById(1).get();
        String oldName = patient.getName();
        String newName = oldName + "X";

        patient.setName(newName);
        this.patientRepository.save(patient);

        // retrieving new name from database
        patient = this.patientRepository.findById(1).get();
        assertThat(patient.getName()).isEqualTo(newName);
    }

}
