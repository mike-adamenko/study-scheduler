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
//package com.caresyntax.studyscheduler.web.controller;
//
//import static org.hamcrest.Matchers.hasProperty;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//
//import com.caresyntax.studyscheduler.dao.PatientRepository;
//import com.caresyntax.studyscheduler.model.Patient;
//import com.caresyntax.studyscheduler.web.controller.PatientController;
//import org.assertj.core.util.Lists;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
///**
// * Test class for {@link PatientController}
// *
// * @author Colin But
// */
//@RunWith(SpringRunner.class)
//@WebMvcTest(PatientController.class)
//public class PatientControllerTests {
//
//    private static final int TEST_OWNER_ID = 1;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PatientRepository patients;
//
//    private Patient george;
//
//    @Before
//    public void setup() {
//        george = new Patient();
//        george.setId(TEST_OWNER_ID);
//        george.setFirstName("George");
//        george.setLastName("Franklin");
//        george.setAddress("110 W. Liberty St.");
//        george.setCity("Madison");
//        george.setTelephone("6085551023");
//        given(this.patients.findById(TEST_OWNER_ID)).willReturn(george);
//    }
//
//    @Test
//    public void testInitCreationForm() throws Exception {
//        mockMvc.perform(get("/patients/new"))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("patient"))
//            .andExpect(view().name("patients/createOrUpdatePatientForm"));
//    }
//
//    @Test
//    public void testProcessCreationFormSuccess() throws Exception {
//        mockMvc.perform(post("/patients/new")
//            .param("firstName", "Joe")
//            .param("lastName", "Bloggs")
//            .param("address", "123 Caramel Street")
//            .param("city", "London")
//            .param("telephone", "01316761638")
//        )
//            .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    public void testProcessCreationFormHasErrors() throws Exception {
//        mockMvc.perform(post("/patients/new")
//            .param("firstName", "Joe")
//            .param("lastName", "Bloggs")
//            .param("city", "London")
//        )
//            .andExpect(status().isOk())
//            .andExpect(model().attributeHasErrors("patient"))
//            .andExpect(model().attributeHasFieldErrors("patient", "address"))
//            .andExpect(model().attributeHasFieldErrors("patient", "telephone"))
//            .andExpect(view().name("patients/createOrUpdatePatientForm"));
//    }
//
//    @Test
//    public void testInitFindForm() throws Exception {
//        mockMvc.perform(get("/patients/find"))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("patient"))
//            .andExpect(view().name("patients/findPatients"));
//    }
//
//    @Test
//    public void testProcessFindFormSuccess() throws Exception {
//        given(this.patients.findByLastName("")).willReturn(Lists.newArrayList(george, new Patient()));
//        mockMvc.perform(get("/patients"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("patients/patientsList"));
//    }
//
//    @Test
//    public void testProcessFindFormByLastName() throws Exception {
//        given(this.patients.findByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
//        mockMvc.perform(get("/patients")
//            .param("lastName", "Franklin")
//        )
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/patients/" + TEST_OWNER_ID));
//    }
//
//    @Test
//    public void testProcessFindFormNoPatientsFound() throws Exception {
//        mockMvc.perform(get("/patients")
//            .param("lastName", "Unknown Surname")
//        )
//            .andExpect(status().isOk())
//            .andExpect(model().attributeHasFieldErrors("patient", "lastName"))
//            .andExpect(model().attributeHasFieldErrorCode("patient", "lastName", "notFound"))
//            .andExpect(view().name("patients/findPatients"));
//    }
//
//    @Test
//    public void testInitUpdatePatientForm() throws Exception {
//        mockMvc.perform(get("/patients/{patientId}/edit", TEST_OWNER_ID))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("patient"))
//            .andExpect(model().attribute("patient", hasProperty("lastName", is("Franklin"))))
//            .andExpect(model().attribute("patient", hasProperty("firstName", is("George"))))
//            .andExpect(model().attribute("patient", hasProperty("address", is("110 W. Liberty St."))))
//            .andExpect(model().attribute("patient", hasProperty("city", is("Madison"))))
//            .andExpect(model().attribute("patient", hasProperty("telephone", is("6085551023"))))
//            .andExpect(view().name("patients/createOrUpdatePatientForm"));
//    }
//
//    @Test
//    public void testProcessUpdatePatientFormSuccess() throws Exception {
//        mockMvc.perform(post("/patients/{patientId}/edit", TEST_OWNER_ID)
//            .param("firstName", "Joe")
//            .param("lastName", "Bloggs")
//            .param("address", "123 Caramel Street")
//            .param("city", "London")
//            .param("telephone", "01616291589")
//        )
//            .andExpect(status().is3xxRedirection())
//            .andExpect(view().name("redirect:/patients/{patientId}"));
//    }
//
//    @Test
//    public void testProcessUpdatePatientFormHasErrors() throws Exception {
//        mockMvc.perform(post("/patients/{patientId}/edit", TEST_OWNER_ID)
//            .param("firstName", "Joe")
//            .param("lastName", "Bloggs")
//            .param("city", "London")
//        )
//            .andExpect(status().isOk())
//            .andExpect(model().attributeHasErrors("patient"))
//            .andExpect(model().attributeHasFieldErrors("patient", "address"))
//            .andExpect(model().attributeHasFieldErrors("patient", "telephone"))
//            .andExpect(view().name("patients/createOrUpdatePatientForm"));
//    }
//
//    @Test
//    public void testShowPatient() throws Exception {
//        mockMvc.perform(get("/patients/{patientId}", TEST_OWNER_ID))
//            .andExpect(status().isOk())
//            .andExpect(model().attribute("patient", hasProperty("lastName", is("Franklin"))))
//            .andExpect(model().attribute("patient", hasProperty("firstName", is("George"))))
//            .andExpect(model().attribute("patient", hasProperty("address", is("110 W. Liberty St."))))
//            .andExpect(model().attribute("patient", hasProperty("city", is("Madison"))))
//            .andExpect(model().attribute("patient", hasProperty("telephone", is("6085551023"))))
//            .andExpect(view().name("patients/ownerDetails"));
//    }
//
//}
