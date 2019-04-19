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

import com.caresyntax.studyscheduler.model.Patient;
import com.caresyntax.studyscheduler.dao.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * @author Mihail Adamenko
 */
@Controller
class PatientController {

    private static final String VIEWS_PATIENT_CREATE_OR_UPDATE_FORM = "patients/createOrUpdatePatientForm";
    private final PatientRepository patientRepository;


    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("sexes")
    public  Patient.SEX[] populateSexes() {
        return Patient.SEX.values();
    }

    @GetMapping("/patients/new")
    public String initCreationForm(Map<String, Object> model) {
        Patient patient = new Patient();
        model.put("patient", patient);
        return VIEWS_PATIENT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/patients/new")
    public String processCreationForm(@Valid Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_PATIENT_CREATE_OR_UPDATE_FORM;
        } else {
            this.patientRepository.save(patient);
            return "redirect:/patients/" + patient.getId();
        }
    }

    @GetMapping("/")
    public String initFindForm(Map<String, Object> model) {
        model.put("patient", new Patient());
        return "patients/findPatients";
    }

    @GetMapping("/patients")
    public String processFindForm(Patient patient, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /patients to return all records
        if (patient.getName() == null) {
            patient.setName(""); // empty string signifies broadest possible search
        }

        // find patients by name
        Collection<Patient> results = patientRepository.findByName(patient.getName());
        if (results.isEmpty()) {
            // no patients found
            result.rejectValue("name", "notFound", "not found");
            return "patients/findPatients";
        } else if (results.size() == 1) {
            // 1 patient found
            patient = results.iterator().next();
            return "redirect:/patients/" + patient.getId();
        } else {
            // multiple patients found
            model.put("selections", results);
            return "patients/patientsList";
        }
    }

    @GetMapping("/patients/{patientId}/edit")
    public String initUpdatePatientForm(@PathVariable("patientId") int patientId, Model model) {
        Patient patient = this.patientRepository.findById(patientId);
        model.addAttribute(patient);
        return VIEWS_PATIENT_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/patients/{patientId}/edit")
    public String processUpdatePatientForm(@Valid Patient patient, BindingResult result, @PathVariable("patientId") int patientId) {
        if (result.hasErrors()) {
            return VIEWS_PATIENT_CREATE_OR_UPDATE_FORM;
        } else {
            patient.setId(patientId);
            this.patientRepository.save(patient);
            return "redirect:/patients/{patientId}";
        }
    }

    /**
     * Custom handler for displaying an patient.
     *
     * @param patientId the ID of the patient to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/patients/{patientId}")
    public ModelAndView showPatient(@PathVariable("patientId") int patientId) {
        ModelAndView mav = new ModelAndView("patients/patientDetails");
        mav.addObject(this.patientRepository.findById(patientId));
        return mav;
    }

}
