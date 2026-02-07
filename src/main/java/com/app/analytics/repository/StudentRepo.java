package com.app.analytics.repository;

import com.app.analytics.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepo extends MongoRepository<Student,String> {
    public Optional<Student> findByName(String studentName);
}
