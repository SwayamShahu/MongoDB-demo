package com.app.analytics.service;

import com.app.analytics.dto.GetStudentDto;
import com.app.analytics.dto.StudentResponseDto;
import com.app.analytics.model.Student;
import com.app.analytics.repository.StudentRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Data
public class StudentService {
    @Autowired
    private final MongoTemplate mongoTemplate;
    private final StudentRepo studentRepo;
    public void addStudent(Student student){
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
