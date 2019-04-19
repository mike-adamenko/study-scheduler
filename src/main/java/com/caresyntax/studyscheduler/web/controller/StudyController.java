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
package com.caresyntax.studyscheduler.web.controller;

import com.caresyntax.studyscheduler.dao.DoctorRepository;
import com.caresyntax.studyscheduler.dao.PatientRepository;
import com.caresyntax.studyscheduler.dao.RoomRepository;
import com.caresyntax.studyscheduler.dao.StudyRepository;
import com.caresyntax.studyscheduler.model.Doctor;
import com.caresyntax.studyscheduler.model.Patient;
import com.caresyntax.studyscheduler.model.Room;
import com.caresyntax.studyscheduler.model.Study;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * @author Mihail Adamenko
 */
@Controller
class StudyController {

    private final PatientRepository patientRepository;
    private final StudyRepository studyRepository;
    private final RoomRepository roomRepository;
    private final DoctorRepository doctorRepository;


    public StudyController(PatientRepository patientRepository, StudyRepository studyRepository, RoomRepository roomRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.studyRepository = studyRepository;
        this.roomRepository = roomRepository;
        this.doctorRepository = doctorRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Patient object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param patientId
     * @return Patient
     */
    @ModelAttribute("study")
    public Study loadPatientWithStudys(@PathVariable("patientId") int patientId, Map<String, Object> model) {
        Patient patient = patientRepository.findById(patientId).get();
        Study study = new Study();
        study.setPatient(patient);
        patient.getStudies().add(study);
        model.put("patient", patient);
        return study;
    }

    @ModelAttribute("rooms")
    public Collection<Room> populateRooms() {
        return this.roomRepository.findAll();
    }

    @ModelAttribute("doctors")
    public Collection<Doctor> populateDoctors() {
        return this.doctorRepository.findAll();
    }

    // Spring MVC calls method loadPatientWithStudy(...) before initNewStudyForm is called
    @GetMapping("/patient/{patientId}/study/new")
    public String initNewStudyForm(@PathVariable("patientId") int patientId, Map<String, Object> model) {
        return "study/createOrUpdateStudyForm";
    }

    // Spring MVC calls method loadPatientWithStudy(...) before processNewStudyForm is called
    @PostMapping("/patient/{patientId}/study/new")
    public String processNewStudyForm(@Valid Study study, BindingResult result) {
        if (result.hasErrors()) {
            return "study/createOrUpdateStudyForm";
        } else {
            this.studyRepository.save(study);
            return "redirect:/patient/{patientId}";
        }
    }

}
