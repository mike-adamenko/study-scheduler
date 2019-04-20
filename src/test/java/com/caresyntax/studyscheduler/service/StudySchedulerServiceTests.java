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

package com.caresyntax.studyscheduler.service;

import com.caresyntax.studyscheduler.dao.PatientRepository;
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
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided by the Spring
 * TestContext Framework: </p> <ul> <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li> <li><strong>Dependency Injection</strong> of test fixture instances, meaning that
 * we don't need to perform application context lookups. See the use of {@link Autowired @Autowired} on the <code>{@link
 * StudySchedulerServiceTests#clinicService clinicService}</code> instance variable, which uses autowiring <em>by
 * type</em>. <li><strong>Transaction management</strong>, meaning each test method is executed in its own transaction,
 * which is automatically rolled back by default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script. <li> An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean lookup if necessary. </li> </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class StudySchedulerServiceTests {

    @Autowired
    protected PatientRepository patients;


    @Test
    public void shouldFindPatientsByName() {
        Collection<Patient> patients = this.patients.findByName("George Franklin");
        assertThat(patients.size()).isEqualTo(1);

        patients = this.patients.findByName("Daviss");
        assertThat(patients.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    public void shouldInsertPatient() {
        Collection<Patient> patients = this.patients.findByName("Schultz");
        int found = patients.size();

        Patient patient = new Patient();
        patient.setName("Sam");
        patient.setSex(Patient.SEX.male);
        patient.setBirthDate(LocalDate.now());
        this.patients.save(patient);
        assertThat(patient.getId().longValue()).isNotEqualTo(0);

        patients = this.patients.findByName("Schultz");
        assertThat(patients.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    public void shouldUpdatePatient() {
        Patient patient = this.patients.findById(1).get();
        String oldName = patient.getName();
        String newName = oldName + "X";

        patient.setName(newName);
        this.patients.save(patient);

        // retrieving new name from database
        patient = this.patients.findById(1).get();
        assertThat(patient.getName()).isEqualTo(newName);
    }

}
