package net.java.naved.attribute;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Subject {
	
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

private String subject;
private long marks;
private String grade;


public String getSubject() {
   return subject;
}

public void setSubject(String subject) {
   this.subject = subject;
}

public long getMarks() {
   return marks;
}

public void setMarks(long marks) {
   this.marks = marks;
}

public String getGrade() {
   return grade;
}

public void setGrade(String grade) {
   this.grade = grade;
}
}
