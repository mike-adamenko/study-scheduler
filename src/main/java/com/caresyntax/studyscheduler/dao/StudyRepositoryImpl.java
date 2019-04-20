package com.caresyntax.studyscheduler.dao;

import com.caresyntax.studyscheduler.model.Study;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    private final static List<Study.STATUS> statuses = Arrays.asList(Study.STATUS.planned, Study.STATUS.inprogress);

    private final static long MIN_STUDY_DURATION_MIN = 30;

    @Override
    public boolean isExistIntersectingStudies(Study study) {
        //
        LocalDateTime endTime = study.getEndTime();
        if (endTime == null) {
            endTime = study.getStartTime().plusMinutes(MIN_STUDY_DURATION_MIN);
        }
        Query query = entityManager.createQuery("select s from Study s where s.startTime <= :endTime and s.endTime >= :startTime " ).
//            "and s.status in :statuses and (s.patient = :patient or s.doctor = :doctor)").
            setParameter("startTime", study.getStartTime()).
            setParameter("endTime", endTime);
//            setParameter("statuses", statuses).
//            setParameter("patient", study.getPatient()).
//            setParameter("doctor", study.getDoctor());
        return !query.getResultList().isEmpty();
    }
}
