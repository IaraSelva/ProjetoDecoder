package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDto lessonDto) {
        log.debug("POST saveLesson lessonDto received {} ", lessonDto.toString());
        Optional<ModuleModel> optionalModuleModel = moduleService.findById(moduleId);
        if (optionalModuleModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(optionalModuleModel.get());

        log.debug("POST saveLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson saved successfully lessonId {} ", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        log.debug("DELETE deleteLesson lessonId received {} ", lessonId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(lessonId, moduleId);
        if (lessonModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        lessonService.delete(lessonModelOptional.get());

        log.debug("DELETE deleteLesson lessonId deleted {} ", lessonId);
        log.info("Lesson deleted successfully lessonId {} ", lessonId);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId,
                                               @RequestBody @Valid LessonDto lessonDto) {
        log.debug("PUT updateLesson lessonDto received {} ", lessonDto.toString());
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(lessonId, moduleId);
        if (lessonModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        var lessonModel = lessonModelOptional.get();
        BeanUtils.copyProperties(lessonDto, lessonModel);

        log.debug("PUT updateLesson lessonId saved {} ", lessonModel.getLessonId());
        log.info("Lesson updated successfully lessonId {} ", lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
    }

    @GetMapping("/modules/{moduleId}/lessons/pageable")
    public ResponseEntity<Page<LessonModel>> getAllLessonsPageable(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<List<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(moduleId));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value = "lessonId") UUID lessonId,
                                               @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<LessonModel> lessonIntoModule = lessonService.findLessonIntoModule(lessonId, moduleId);
        if (lessonIntoModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this lesson.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonIntoModule.get());
    }
}
