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
package com.caresyntax.studyscheduler.dao;

import java.util.Collection;

import com.caresyntax.studyscheduler.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Patient</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data.
 * See: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Mihail Adamenko
 */
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    /**
     * Retrieve {@link Patient}s from the data store by last name, returning all patients
     * whose last name <i>starts</i> with the given name.
     * @param name Value to search for
     * @return a Collection of matching {@link Patient}s (or an empty Collection if none
     * found)
     */
    @Query("SELECT DISTINCT patient FROM Patient patient WHERE lower(patient.name) LIKE lower('%' || :name || '%')")
    @Transactional(readOnly = true)
    Collection<Patient> findByName(@Param("name") String name);

}
