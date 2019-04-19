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

import com.caresyntax.studyscheduler.model.Study;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository class for <code>Study</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data.
 * See: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Mihail Adamenko
 */
public interface StudyRepository extends Repository<Study, Integer> {


    /**
     * Save a <code>Study</code> to the data store, either inserting or updating it.
     *
     * @param study the <code>Study</code> to save
     * @see com.caresyntax.studyscheduler.model.BaseEntity#isNew
     */
    void save(Study study) throws DataAccessException;

    List<Study> findByPatientId(Integer patientId);


}
