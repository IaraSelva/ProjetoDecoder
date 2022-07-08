package com.ead.authuser.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsService {

    @Value("${ead.api.url.course}")
    String REQUEST_URI;

    public String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable) {
        return REQUEST_URI + "/courses/pageable?userId=" + userId + "&page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() + "&sort=" +
                pageable.getSort().toString().replaceAll(": ", ",");
    }
}
