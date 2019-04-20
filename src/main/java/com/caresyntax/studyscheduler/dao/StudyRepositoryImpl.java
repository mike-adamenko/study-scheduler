package com.caresyntax.studyscheduler.dao;

import com.caresyntax.studyscheduler.model.Study;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final static List<Study.STATUS> statuses = Arrays.asList(Study.STATUS.planned, Study.STATUS.inprogress);

    /**
     * Searches intersections to not allow to store studies in the same time.
     * Compares only by start time as end time is optional
     *
     * @param study study
     * @return true - if exists intersections
     */
    @Override
    public boolean isExistIntersectingStudies(Study study) {
        Query query = entityManager.createQuery("select s from Study s where s.startTime = :startTime " +
            "and s.status in :statuses and (s.patient = :patient or s.doctor = :doctor)").
            setParameter("startTime", study.getStartTime()).
            setParameter("statuses", statuses).
            setParameter("patient", study.getPatient()).
            setParameter("doctor", study.getDoctor());
        return !query.getResultList().isEmpty();
    }
}
