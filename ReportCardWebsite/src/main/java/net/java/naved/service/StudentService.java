package net.java.naved.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.java.naved.attribute.Student;
import net.java.naved.repository.StudentRepository;

@Service
public class StudentService {
	private final StudentRepository studentRepository;
	
	@Autowired
	public StudentService(StudentRepository studentRepository) {
		this.studentRepository=studentRepository;
	}
	@Transactional
	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}
	public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
	public Student getStudentById(Long id) {
        // Implement the logic to retrieve a student by their ID
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudent(Long id) {
        // Implement the logic to delete a student by their ID
        studentRepository.deleteById(id);
    }
}
