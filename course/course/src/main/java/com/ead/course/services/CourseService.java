package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    CourseModel save(CourseModel courseModel);

    void delete(CourseModel courseModel);

    Optional<CourseModel> findById(UUID courseId);

    List<CourseModel> findAll();

    Page<CourseModel> findAll(SpecificationTemplate.CourseSpec spec, Pageable pageable);
}
