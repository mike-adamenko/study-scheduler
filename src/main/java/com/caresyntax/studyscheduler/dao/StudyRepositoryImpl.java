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

import com.caresyntax.studyscheduler.model.Study;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

/**
 * Custom StudyRepository implementation for methods that requires
 * a different behavior or cannot be implemented by query derivation
 *
 * @author Mike Adamenko (mnadamenko@gmail.com)
 */
@Repository
@Transactional(readOnly = true)
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    private final static List<Study.STATUS> statuses = Arrays.asList(Study.STATUS.planned, Study.STATUS.inprogress);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Searches intersections to not allow to store studies in the same time.
     * Compares only by start time as end time is optional
     *
     * @param study study
     * @return true - if exists intersections
     */
    @Override
    public boolean isExistIntersectingStudies(Study study) {
        String sfx = "";
        if (!study.isNew()) sfx = " and s.id <> :id";

        Query query = entityManager.createQuery("select s from Study s where s.startTime = :startTime " +
            "and s.status in :statuses and (s.patient = :patient or s.doctor = :doctor)" + sfx).
            setParameter("startTime", study.getStartTime()).
            setParameter("statuses", statuses).
            setParameter("patient", study.getPatient()).
            setParameter("doctor", study.getDoctor());
        if (!study.isNew()) query.setParameter("id", study.getId());
        return !query.getResultList().isEmpty();
    }
}
