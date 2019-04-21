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

import com.caresyntax.studyscheduler.dao.PatientRepository;
import com.caresyntax.studyscheduler.model.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * Patient web controller
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */
@Controller
class PatientController {

    public static final String CREATE_OR_UPDATE_PATIENT_FORM = "patient/createOrUpdatePatientForm";
    private final PatientRepository patientRepository;


    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("sexes")
    public Patient.SEX[] populateSexes() {
        return Patient.SEX.values();
    }

    @GetMapping("/patient/new")
    public String getNewPatientForm(Map<String, Object> model) {
        Patient patient = new Patient();
        model.put("patient", patient);
        return CREATE_OR_UPDATE_PATIENT_FORM;
    }

    @PostMapping("/patient/new")
    public String updateNewPatientForm(@Valid Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_PATIENT_FORM;
        } else {
            this.patientRepository.save(patient);
            return "redirect:/patient/" + patient.getId();
        }
    }

    @GetMapping("/")
    public String initFindForm(Map<String, Object> model) {
        model.put("patient", new Patient());
        return "patient/findPatients";
    }

    @GetMapping("/patient")
    public String searchPatient(Patient patient, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /patient to return all records
        if (patient.getName() == null) {
            patient.setName(""); // empty string signifies broadest possible search
        }

        // find patient by name
        Collection<Patient> results = patientRepository.findByName(patient.getName());
        if (results.isEmpty()) {
            // no patient found
            result.rejectValue("name", "notFound", "not found");
            return "patient/findPatients";
        } else if (results.size() == 1) {
            // 1 patient found
            patient = results.iterator().next();
            return "redirect:/patient/" + patient.getId();
        } else {
            // multiple patient found
            model.put("selections", results);
            return "patient/patientList";
        }
    }

    @GetMapping("/patient/{patientId}/edit")
    public String getPatientForm(@PathVariable("patientId") int patientId, Model model) {
        Patient patient = this.patientRepository.findById(patientId).get();
        model.addAttribute(patient);
        return CREATE_OR_UPDATE_PATIENT_FORM;
    }

    @PostMapping("/patient/{patientId}/edit")
    public String updatePatientForm(@Valid Patient patient, BindingResult result, @PathVariable("patientId") int patientId) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_PATIENT_FORM;
        } else {
            patient.setId(patientId);
            this.patientRepository.save(patient);
            return "redirect:/patient/{patientId}";
        }
    }

    /**
     * Custom handler for displaying an patient.
     *
     * @param patientId the ID of the patient to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/patient/{patientId}")
    public String showPatient(@PathVariable("patientId") int patientId, Model model) {
        model.addAttribute(this.patientRepository.findById(patientId).get());
        return "patient/patientDetails";
    }

}
