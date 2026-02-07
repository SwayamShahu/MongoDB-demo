package com.app.analytics.controller;

import com.app.analytics.dto.GetStudentDto;
import com.app.analytics.dto.StudentResponseDto;
import com.app.analytics.model.Student;
import com.app.analytics.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        try{
            studentService.addStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Student created successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudent(){
        return ResponseEntity.ok(studentService.getStudents());
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadfile(@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(file.getOriginalFilename());
//        System.out.println(file.getSize());
//        System.out.println(file.getContentType());
//        System.out.println(file.isEmpty());
//        System.out.println(file.getResource());
//        System.out.println(file.getName());
        String uploadDir = System.getProperty("user.dir")+ "\\uploads\\";
        File directory = new File(uploadDir);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String path = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(path));
        return ResponseEntity.ok(file.getOriginalFilename());
    }

    @Operation(summary = "uploads multiple files", description = "You can upload multiple format file like pdf, word, text and many more")
    @PostMapping(value = "/uploadMultiple", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadsfile(@RequestPart("files") List<MultipartFile> files) throws IOException {
        String uploadDir = System.getProperty("user.dir")+ "\\uploads\\";
        File directory = new File(uploadDir);
        if (!directory.exists()){
            directory.mkdirs();
        }
        files.forEach(file -> {
            String path = uploadDir + file.getOriginalFilename();
            try {
                file.transferTo(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok("Successfully");
    }

    // Take two file in different key -> files, student image

    @PostMapping(value = "/upload/TwoFile", consumes = "multipart/form-data")
    public ResponseEntity<String> addTwoFile(@RequestParam("file") MultipartFile file, @RequestParam("studentImage") MultipartFile studentImage) throws IOException {
        String uploadDir = System.getProperty("user.dir")+ "\\uploads\\";
        File directory = new File(uploadDir);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String path1 = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(path1));
        String path2 = uploadDir + studentImage.getOriginalFilename();
        studentImage.transferTo(new File(path2));

        return ResponseEntity.ok("Successfully");
    }

    // Write a api to get profile image of student and save key database.
    @PostMapping(value = "/updateProfileImage", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProfileImage(@RequestParam("file") MultipartFile file, @RequestParam String studentName) throws IOException {
        String uploadDir = System.getProperty("user.dir")+ "\\uploads\\";
        File directory = new File(uploadDir);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String key = studentService.saveProfilePhoto(studentName, file.getOriginalFilename());
        String path = uploadDir + key + file.getOriginalFilename();
        file.transferTo(new File(path));
        return ResponseEntity.ok("Successfully");
    }
}