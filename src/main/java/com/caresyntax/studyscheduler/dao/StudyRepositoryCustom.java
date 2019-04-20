package com.caresyntax.studyscheduler.dao;

import com.caresyntax.studyscheduler.model.Study;

public interface StudyRepositoryCustom {

    boolean isExistIntersectingStudies(Study study);
}
