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

import com.caresyntax.studyscheduler.dao.PatientRepository;
import com.caresyntax.studyscheduler.dao.StudyRepository;
import com.caresyntax.studyscheduler.model.Patient;
import com.caresyntax.studyscheduler.model.Schedule;
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
import java.util.Map;

/**
 * @author Mihail Adamenko
 */
@Controller
class StudyController {

    private final StudyRepository studyRepository;
    private final PatientRepository patientRepository;


    public StudyController(StudyRepository studyRepository, PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        this.studyRepository = studyRepository;
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
    @ModelAttribute("Schedule")
    public Schedule loadPatientWithSchedules(@PathVariable("patientId") int patientId, Map<String, Object> model) {
        Patient patient = patientRepository.findById(patientId);
        model.put("patient", patient);
        Schedule schedule = new Schedule();
        patient.getSchedules().add(schedule);
        return schedule;
    }

    // Spring MVC calls method loadPatientWithStudy(...) before initNewStudyForm is called
    @GetMapping("/patients/*/patients/{patientId}/studies/new")
    public String initNewStudyForm(@PathVariable("patientId") int patientId, Map<String, Object> model) {
        return "patients/createOrUpdateStudyForm";
    }

    // Spring MVC calls method loadPatientWithStudy(...) before processNewStudyForm is called
    @PostMapping("/patients/{patientId}/patients/{patientId}/studies/new")
    public String processNewStudyForm(@Valid Study Study, BindingResult result) {
        if (result.hasErrors()) {
            return "patients/createOrUpdateStudyForm";
        } else {
            this.studyRepository.save(Study);
            return "redirect:/patients/{patientId}";
        }
    }

}
