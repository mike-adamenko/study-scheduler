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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

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

    @ModelAttribute("rooms")
    public Collection<Room> populateRooms() {
        return this.roomRepository.findAll();
    }

    @ModelAttribute("doctors")
    public Collection<Doctor> populateDoctors() {
        return this.doctorRepository.findAll();
    }

    @ModelAttribute("studyStatuses")
    public Study.STATUS[] populateStudyStatuses() {
        return Study.STATUS.values();
    }

    @GetMapping("/studies")
    public String initStudyListForm(Model model) {
        List<Study> studyList = this.studyRepository.findAll();
        model.addAttribute(studyList);
        return "study/studyList";
    }

    @GetMapping("/patient/{patientId}/study/new")
    public String getNewStudyForm(Model model) {
        Study study = new Study();
        model.addAttribute(study);
        return "study/createOrUpdateStudyForm";
    }

    @GetMapping("/patient/{patientId}/study/{studyId}/edit")
    public String getStudyForm(@PathVariable("studyId") int studyId, Model model, String source) {
        Study study = this.studyRepository.findById(studyId).get();
        model.addAttribute(study);
        if (source != null)
            model.addAttribute(source);
        return "study/createOrUpdateStudyForm";
    }

    @PostMapping("/patient/{patientId}/study/new")
    public String updateNewStudyForm(@PathVariable("patientId") int patientId, @Valid Study study, BindingResult result) {


        if (result.hasErrors()) {
            return "study/createOrUpdateStudyForm";
        } else {
            Patient patient = patientRepository.findById(patientId).get();
            study.setPatient(patient);
            eligibilityCheck(study);
            this.studyRepository.save(study);
            return "redirect:/patient/{patientId}";
        }
    }

    private void eligibilityCheck(@Valid Study study) {

    }

    @PostMapping("/patient/{patientId}/study/{studyId}/edit")
    public String updateStudyForm(@PathVariable int patientId, @Valid Study study, BindingResult result, Model model, String source) {
        Patient patient = patientRepository.findById(patientId).get();
        study.setPatient(patient);

        if (result.hasErrors()) {
            model.addAttribute(study);
            return "study/createOrUpdateStudyForm";
        } else {

            this.studyRepository.save(study);
            if ("studyList".equals(source)) return "redirect:/studies";
            else
                return "redirect:/patient/{patientId}";
        }
    }

}
