package com.app.analytics.controller;

import com.app.analytics.dto.StudentResponseDto;
import com.app.analytics.model.Student;
import com.app.analytics.service.MailService;
import com.app.analytics.service.S3Service;
import com.app.analytics.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final S3Service service;
    private final MailService mailService;

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

    // Export to students in CSV
    @GetMapping("/export/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename = student.csv");
        PrintWriter writer = response.getWriter();
        writer.println("Name, City, Age, Url");
        for (StudentResponseDto stu: studentService.getStudents()){
            writer.println(stu.getName() + "," + stu.getCity() + "," +stu.getAge() + "," + stu.getUrl());
        }
        writer.flush();
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadfile(@RequestParam("file") MultipartFile file) throws IOException {
        // Using S3 bucket
        System.out.println(file.getOriginalFilename());
        service.upload(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok("Successfully upload");
    }

    @GetMapping(value = "/download/{name}")
    public ResponseEntity<byte[]> downloadfile(@PathVariable String name) throws IOException {
        // Using S3 bucket
        byte[] response = service.download(name);
        String type = Files.probeContentType(Paths.get(name));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(type)).body(response);
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

    @GetMapping("/welcome-mail")
    public void sendMail(@RequestParam String mail){
        mailService.sentmail(mail);
    }

    // OTP SYSTEM -
}