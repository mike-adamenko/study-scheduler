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
package com.caresyntax.studyscheduler.dao;

import com.caresyntax.studyscheduler.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository class for <code>Patient</code> domain objects
 * <p>
 * All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data.
 * See: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    /**
     * Retrieve {@link Patient}s from the data store by name, returning all patients
     * whose name <i>contains</i> the given substring.
     *
     * @param name Value to search for
     * @return a Collection of matching {@link Patient}s (or an empty Collection if none
     * found)
     */
    @Query("SELECT patient FROM Patient patient WHERE lower(patient.name) LIKE lower('%' || :name || '%')")
    @Transactional(readOnly = true)
    Collection<Patient> findByName(@Param("name") String name);

}
