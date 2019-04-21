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

import com.caresyntax.studyscheduler.dao.DoctorRepository;
import com.caresyntax.studyscheduler.dao.PatientRepository;
import com.caresyntax.studyscheduler.dao.RoomRepository;
import com.caresyntax.studyscheduler.dao.StudyRepository;
import com.caresyntax.studyscheduler.model.Patient;
import com.caresyntax.studyscheduler.model.Study;
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

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link StudyController}
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */
@RunWith(SpringRunner.class)
@WebMvcTest(StudyController.class)
public class StudyControllerTest {

    private static final int TEST_STUDY_ID = 1;
    private static final int TEST_PATIENT_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyRepository studyRepository;
    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private DoctorRepository doctorRepository;
    @MockBean
    private RoomRepository roomRepository;

    private Study study1;

    private Patient george;

    @Before
    public void setup() {

        george = new Patient();
        george.setId(TEST_PATIENT_ID);
        george.setName("George");
        george.setSex(Patient.SEX.male);
        george.setBirthDate(LocalDate.of(2015, 12, 01));
        given(this.patientRepository.findById(TEST_PATIENT_ID)).willReturn(Optional.of(george));

        study1 = new Study();
        study1.setDescription("study1");
        study1.setPatient(george);
        given(this.studyRepository.findById(TEST_STUDY_ID)).willReturn(Optional.of(study1));
    }

    @Test
    public void testGetStudyListForm() throws Exception {
        mockMvc.perform(get("/studies"))
            .andExpect(status().isOk())
            .andExpect(view().name("study/studyList"));
    }

    @Test
    public void testGetNewStudyForm() throws Exception {
        mockMvc.perform(post("/patient/{patientId}/study/new", TEST_STUDY_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("study"))
            .andExpect(view().name(StudyController.CREATE_OR_UPDATE_STUDY_FORM));
    }

    @Test
    public void testGetStudyForm() throws Exception {
        mockMvc.perform(get("/patient/{patientId}/study/{studyId}/edit", TEST_PATIENT_ID, TEST_STUDY_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("study"))
            .andExpect(model().attribute("study", hasProperty("description", is("study1"))))
            .andExpect(view().name(StudyController.CREATE_OR_UPDATE_STUDY_FORM));
    }

    @Test
    public void testUpdateNewStudyForm() throws Exception {
        mockMvc.perform(post("/patient/{patientId}/study/new", TEST_PATIENT_ID)
            .param("description", "desc1")
            .param("startTime", "2019-04-21 18:25")
            .param("status", "planned")
        )
            .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testUpdateNewStudyFormIsIntersect() throws Exception {
        given(this.studyRepository.isExistIntersectingStudies(notNull())).willReturn(true);
        mockMvc.perform(post("/patient/{patientId}/study/new", TEST_PATIENT_ID)
            .param("description", "desc1")
            .param("startTime", "2019-04-21 18:25")
            .param("status", "planned")
        )
            .andExpect(status().isOk())
            .andExpect(view().name(StudyController.CREATE_OR_UPDATE_STUDY_FORM));
    }


}

