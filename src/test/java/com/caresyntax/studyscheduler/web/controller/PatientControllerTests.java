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

package com.caresyntax.studyscheduler.web.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.caresyntax.studyscheduler.dao.PatientRepository;
import com.caresyntax.studyscheduler.model.Patient;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Test class for {@link PatientController}
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PatientController.class)
public class PatientControllerTests {

    private static final int TEST_PATIENT_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientRepository patientRepository;

    private Patient george;

    @Before
    public void setup() {
        george = new Patient();
        george.setId(TEST_PATIENT_ID);
        george.setName("George");
        george.setSex(Patient.SEX.male);
        george.setBirthDate(LocalDate.of(2015, 12, 01));
        given(this.patientRepository.findById(TEST_PATIENT_ID)).willReturn(Optional.of(george));
    }

    @Test
    public void testGetNewPatientForm() throws Exception {
        mockMvc.perform(get("/patient/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("patient"))
            .andExpect(view().name(PatientController.CREATE_OR_UPDATE_PATIENT_FORM));
    }

    @Test
    public void testUpdateNewPatientForm() throws Exception {
        mockMvc.perform(post("/patient/new")
            .param("name", "Bloggs")
            .param("sex", "male")
            .param("birthDate", "2015-12-23")
        )
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testUpdateNewPatientFormHasErrors() throws Exception {
        mockMvc.perform(post("/patient/new")
            .param("sex", "male")
            .param("birthDate", "2015-12-23")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("patient"))
            .andExpect(model().attributeHasFieldErrors("patient", "name"))
            .andExpect(view().name("patient/createOrUpdatePatientForm"));
    }

    @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("patient"))
            .andExpect(view().name("patient/findPatients"));
    }

    @Test
    public void testSearchPatientSuccess() throws Exception {
        given(this.patientRepository.findByName("")).willReturn(Lists.newArrayList(george, george));
        mockMvc.perform(get("/patient"))
            .andExpect(status().isOk())
            .andExpect(view().name("patient/patientList"));
    }

    @Test
    public void testSearchPatientByName() throws Exception {
        given(this.patientRepository.findByName(george.getName())).willReturn(Lists.newArrayList(george));
        mockMvc.perform(get("/patient")
            .param("name", george.getName())
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/patient/" + TEST_PATIENT_ID));
    }

    @Test
    public void testSearchPatientByNameNoPatientsFound() throws Exception {
        mockMvc.perform(get("/patient")
            .param("name", "Unknown Surname")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("patient", "name"))
            .andExpect(model().attributeHasFieldErrorCode("patient", "name", "error.notFound"))
            .andExpect(view().name("patient/findPatients"));
    }

    @Test
    public void testGetPatientForm() throws Exception {
        mockMvc.perform(get("/patient/{patientId}/edit", TEST_PATIENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("patient"))
            .andExpect(model().attribute("patient", hasProperty("name", is("George"))))
            .andExpect(model().attribute("patient", hasProperty("sex", is(Patient.SEX.male))))
            .andExpect(model().attribute("patient", hasProperty("birthDate", is(LocalDate.of(2015, 12, 01)))))
            .andExpect(view().name("patient/createOrUpdatePatientForm"));
    }

    @Test
    public void testUpdatePatientFormSuccess() throws Exception {
        mockMvc.perform(post("/patient/{patientId}/edit", TEST_PATIENT_ID)
            .param("name", "Joe")
            .param("sex", "male")
            .param("birthDate", "2015-10-02")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/patient/{patientId}"));
    }

    @Test
    public void testUpdatePatientFormHasErrors() throws Exception {
        mockMvc.perform(post("/patient/{patientId}/edit", TEST_PATIENT_ID)
            .param("sex", "male")
            .param("birthDate", "2015-10-02")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("patient"))
            .andExpect(model().attributeHasFieldErrors("patient", "name"))
            .andExpect(view().name("patient/createOrUpdatePatientForm"));
    }

    @Test
    public void testShowPatient() throws Exception {
        mockMvc.perform(get("/patient/{patientId}", TEST_PATIENT_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("patient", hasProperty("name", is("George"))))
            .andExpect(model().attribute("patient", hasProperty("sex", is(Patient.SEX.male))))
            .andExpect(model().attribute("patient", hasProperty("birthDate", is(LocalDate.of(2015, 12, 01)))))
            .andExpect(view().name("patient/patientDetails"));
    }

}
