package com.ead.course.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsService {

    @Value("${ead.api.url.authuser}")
    String REQUEST_URI;

    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
        return REQUEST_URI + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() + "&sort=" +
                pageable.getSort().toString().replaceAll(": ", ",");
    }

    public String createUrlGetOneUserById(UUID userId) {
        return REQUEST_URI + "/users/" + userId;
    }
}
