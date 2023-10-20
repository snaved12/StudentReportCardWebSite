package net.java.naved.attribute;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Student {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

private String rollNumber;
private String name;

@OneToMany(cascade=CascadeType.ALL)
@JoinColumn(name="studnet_id")
private List<Subject> subjects;


public String getName() {
   return name;
}

public void setName(String name) {
   this.name = name;
}

public String getRollNumber() {
   return rollNumber;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public void setRollNumber(String rollNumber) {
   this.rollNumber = rollNumber;
}

public List<Subject> getSubjects() {
   return subjects;
}

public void setSubjects(List<Subject> subjects) {
   this.subjects = subjects;
}
}
