package com.app.analytics.service;

import com.app.analytics.dto.StudentResponseDto;
import com.app.analytics.model.Otp;
import com.app.analytics.model.Student;
import com.app.analytics.repository.OtpRepo;
import com.app.analytics.repository.StudentRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Data
public class StudentService {
    @Autowired
    private final MongoTemplate mongoTemplate;
    private final StudentRepo studentRepo;
    private final MailService mailService;
    private final OtpRepo otpRepo;
    // Old without otp concept
//    public void addStudent(Student student) throws Exception {
//        studentRepo.save(student);
//        mailService.sendOtpMail(student.getMail(), student.getName(), "4365");
//        System.out.println(student.toString());
//    }

    public void addStudent(Student student) throws Exception {
//        if (studentRepo.findByMail(student.getMail()).isEmpty()){
//
//        }

        int min = 1000;
        int max = 9999;

        Otp otp = new Otp();
        otp.setUsed(false);
        otp.setMail(student.getMail());
        otp.setExpireTime(System.currentTimeMillis() + + (5 * 60 * 1000));
        String s = String.valueOf(min + (int)(Math.random() * ((max - min) + 1)));
        otp.setOtp(s);
        mailService.sendOtpMail(student.getMail(), student.getName(), otp.getOtp());
        otpRepo.save(otp);
        studentRepo.save(student);
        System.out.println(student.toString());
    }

    public List<StudentResponseDto> getStudents(){
//        Aggregation aggregation = Aggregation.newAggregation(
//                group("city").count().as("total_student"), sort(Sort.Direction.DESC, "total_student"), project("total_student").and("_id").as("city")
//        );
//        AggregationResults<GetStudentDto> results = mongoTemplate.aggregate(aggregation, "student", GetStudentDto.class);
//        return results.getMappedResults();

        List<Student> students = studentRepo.findAll();
        List<StudentResponseDto> res = new LinkedList<>();

        for (Student stu: students){
            StudentResponseDto s = new StudentResponseDto();
            s.setName(stu.getName());
            s.setAge(stu.getAge());
            s.setCity(stu.getCity());
            if (stu.getProfileImage() != null){
                s.setUrl(System.getProperty("user.dir")+ "\\uploads\\" + stu.getProfileImage());
            }
            res.add(s);
        }
        return res;
    }

    public String saveProfilePhoto(String studentName, String orginalName){
        Student student = studentRepo.findByName(studentName).orElseThrow(() -> new RuntimeException("Incorrect student name"));
        String key = String.valueOf(new Date().getTime());
        student.setProfileImage(key + orginalName);
        studentRepo.save(student);
        return key;
    }
}
