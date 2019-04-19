///*
// * Copyright 2012-2019 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.caresyntax.studyscheduler.service;
//
//import com.caresyntax.studyscheduler.dao.PatientRepository;
//import com.caresyntax.studyscheduler.model.Patient;
//import com.caresyntax.studyscheduler.vet.VetRepository;
//import com.caresyntax.studyscheduler.visit.Visit;
//import com.caresyntax.studyscheduler.visit.VisitRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.samples.petclinic.patient.Pet;
//import org.springframework.samples.petclinic.patient.PetRepository;
//import org.springframework.samples.petclinic.patient.PetType;
//import org.springframework.samples.petclinic.vet.Vet;
//import org.springframework.stereotype.Service;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.Collection;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * Integration test of the Service and the Repository layer.
// * <p>
// * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided by the Spring
// * TestContext Framework: </p> <ul> <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
// * time between test execution.</li> <li><strong>Dependency Injection</strong> of test fixture instances, meaning that
// * we don't need to perform application context lookups. See the use of {@link Autowired @Autowired} on the <code>{@link
// * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses autowiring <em>by
// * type</em>. <li><strong>Transaction management</strong>, meaning each test method is executed in its own transaction,
// * which is automatically rolled back by default. Thus, even if tests insert or otherwise change database state, there
// * is no need for a teardown or cleanup script. <li> An {@link org.springframework.context.ApplicationContext
// * ApplicationContext} is also inherited and can be used for explicit bean lookup if necessary. </li> </ul>
// *
// * @author Ken Krebs
// * @author Rod Johnson
// * @author Juergen Hoeller
// * @author Sam Brannen
// * @author Michael Isvy
// * @author Dave Syer
// */
//
//@RunWith(SpringRunner.class)
//@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
//public class ClinicServiceTests {
//
//    @Autowired
//    protected PatientRepository patients;
//
//    @Autowired
//    protected PetRepository pets;
//
//    @Autowired
//    protected VisitRepository visits;
//
//    @Autowired
//    protected VetRepository vets;
//
//    @Test
//    public void shouldFindPatientsByLastName() {
//        Collection<Patient> patients = this.patients.findByLastName("Davis");
//        assertThat(patients.size()).isEqualTo(2);
//
//        patients = this.patients.findByLastName("Daviss");
//        assertThat(patients.isEmpty()).isTrue();
//    }
//
//    @Test
//    public void shouldFindSinglePatientWithPet() {
//        Patient patient = this.patients.findById(1);
//        assertThat(patient.getLastName()).startsWith("Franklin");
//        assertThat(patient.getPets().size()).isEqualTo(1);
//        assertThat(patient.getPets().get(0).getType()).isNotNull();
//        assertThat(patient.getPets().get(0).getType().getName()).isEqualTo("cat");
//    }
//
//    @Test
//    @Transactional
//    public void shouldInsertPatient() {
//        Collection<Patient> patients = this.patients.findByLastName("Schultz");
//        int found = patients.size();
//
//        Patient patient = new Patient();
//        patient.setFirstName("Sam");
//        patient.setLastName("Schultz");
//        patient.setAddress("4, Evans Street");
//        patient.setCity("Wollongong");
//        patient.setTelephone("4444444444");
//        this.patients.save(patient);
//        assertThat(patient.getId().longValue()).isNotEqualTo(0);
//
//        patients = this.patients.findByLastName("Schultz");
//        assertThat(patients.size()).isEqualTo(found + 1);
//    }
//
//    @Test
//    @Transactional
//    public void shouldUpdatePatient() {
//        Patient patient = this.patients.findById(1);
//        String oldLastName = patient.getLastName();
//        String newLastName = oldLastName + "X";
//
//        patient.setLastName(newLastName);
//        this.patients.save(patient);
//
//        // retrieving new name from database
//        patient = this.patients.findById(1);
//        assertThat(patient.getLastName()).isEqualTo(newLastName);
//    }
//
//    @Test
//    public void shouldFindPetWithCorrectId() {
//        Pet pet7 = this.pets.findById(7);
//        assertThat(pet7.getName()).startsWith("Samantha");
//        assertThat(pet7.getPatient().getFirstName()).isEqualTo("Jean");
//
//    }
//
//    @Test
//    public void shouldFindAllPetTypes() {
//        Collection<PetType> petTypes = this.pets.findPetTypes();
//
//        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
//        assertThat(petType1.getName()).isEqualTo("cat");
//        PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
//        assertThat(petType4.getName()).isEqualTo("snake");
//    }
//
//    @Test
//    @Transactional
//    public void shouldInsertPetIntoDatabaseAndGenerateId() {
//        Patient patient6 = this.patients.findById(6);
//        int found = patient6.getPets().size();
//
//        Pet pet = new Pet();
//        pet.setName("bowser");
//        Collection<PetType> types = this.pets.findPetTypes();
//        pet.setType(EntityUtils.getById(types, PetType.class, 2));
//        pet.setBirthDate(LocalDate.now());
//        patient6.addPet(pet);
//        assertThat(patient6.getPets().size()).isEqualTo(found + 1);
//
//        this.pets.save(pet);
//        this.patients.save(patient6);
//
//        patient6 = this.patients.findById(6);
//        assertThat(patient6.getPets().size()).isEqualTo(found + 1);
//        // checks that id has been generated
//        assertThat(pet.getId()).isNotNull();
//    }
//
//    @Test
//    @Transactional
//    public void shouldUpdatePetName() throws Exception {
//        Pet pet7 = this.pets.findById(7);
//        String oldName = pet7.getName();
//
//        String newName = oldName + "X";
//        pet7.setName(newName);
//        this.pets.save(pet7);
//
//        pet7 = this.pets.findById(7);
//        assertThat(pet7.getName()).isEqualTo(newName);
//    }
//
//    @Test
//    public void shouldFindVets() {
//        Collection<Vet> vets = this.vets.findAll();
//
//        Vet vet = EntityUtils.getById(vets, Vet.class, 3);
//        assertThat(vet.getLastName()).isEqualTo("Douglas");
//        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
//        assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
//        assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
//    }
//
//    @Test
//    @Transactional
//    public void shouldAddNewVisitForPet() {
//        Pet pet7 = this.pets.findById(7);
//        int found = pet7.getVisits().size();
//        Visit visit = new Visit();
//        pet7.addVisit(visit);
//        visit.setDescription("test");
//        this.visits.save(visit);
//        this.pets.save(pet7);
//
//        pet7 = this.pets.findById(7);
//        assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
//        assertThat(visit.getId()).isNotNull();
//    }
//
//    @Test
//    public void shouldFindVisitsByPetId() throws Exception {
//        Collection<Visit> visits = this.visits.findByPetId(7);
//        assertThat(visits.size()).isEqualTo(2);
//        Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
//        assertThat(visitArr[0].getDate()).isNotNull();
//        assertThat(visitArr[0].getPetId()).isEqualTo(7);
//    }
//
//}
