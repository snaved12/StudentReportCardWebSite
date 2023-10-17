package net.java.naved.controller;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import net.java.naved.attribute.Student;
import net.java.naved.attribute.StudentData;
import net.java.naved.repository.StudentRepository;
import net.java.naved.service.StudentService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ReportController {
	
	private final StudentService studentService;

    @Autowired
    private StudentRepository studentRepository; // Adjust the repository type to match your actual repository interface
    public ReportController(StudentService studentService) {
        this.studentService = studentService;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @PostMapping("/addDataToDatabase")
    @ResponseBody
    public String addDataToDatabase(@RequestParam("jsonData") String jsonData,HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StudentData studentData = objectMapper.readValue(jsonData, StudentData.class);
            List<Student> students = studentData.getStudents();
            studentRepository.saveAll(studentData.getStudents());
            // You can add a success message or redirect to another page upon successful data addition.
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the process, e.g., display an error message.
        }
        return "redirect:/"; // Redirect back to the index page
    }
    /*
    @PostMapping("/generateReport")
    @ResponseBody
    public void generateReport(@RequestParam("jsonData") String jsonData, HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StudentData studentData = objectMapper.readValue(jsonData, StudentData.class);
            List<Student> students = studentData.getStudents();
            studentRepository.saveAll(studentData.getStudents());
            ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(zipStream);

            for (Student student : students) {
                String rollno = student.getRollNumber();
                String pdfFileName = "C:\\Users\\ssyed\\OneDrive\\Desktop\\SCOREME\\SCHOOL\\" + rollno + ".pdf";

                // Generate the PDF report
                String jrxmlFilePath = "C:\\Users\\ssyed\\Documents\\workspace-spring-tool-suite-4-4.20.0.RELEASE\\ReportCardWebsite\\src\\main\\resources\\ProgressReport.jrxml";
                JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(student.getSubjects());
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("name", student.getName());
                parameters.put("rollno", rollno);
                parameters.put("dataset", dataSource2);

                JasperReport report = JasperCompileManager.compileReport(jrxmlFilePath);
                JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

                // Export the report to a PDF file
                JasperExportManager.exportReportToPdfFile(print, pdfFileName);

                // Add the PDF file to the zip archive
                File pdfFile = new File(pdfFileName);
                try (FileInputStream pdfStream = new FileInputStream(pdfFile)) {
                    zipOut.putNextEntry(new ZipEntry(rollno + ".pdf"));
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = pdfStream.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, length);
                    }
                }
                pdfFile.delete();
            }

            zipOut.close();

            // Set response headers for the zip file
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=reports.zip");

            // Write the zip data to the response output stream
            response.getOutputStream().write(zipStream.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    @GetMapping("/generateReport")
    @ResponseBody
    public void generateReport(HttpServletResponse response) {
        try {
            // 1. Fetch all students from the database
            List<Student> students = studentRepository.findAll();

            if (students != null && !students.isEmpty()) {
                ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(zipStream);

                for (Student student : students) {
                    String rollno = student.getRollNumber();
                    String pdfFileName = "C:\\Users\\ssyed\\OneDrive\\Desktop\\SCOREME\\SCHOOL\\" + rollno + ".pdf";

                    // 2. Generate the PDF report
                    String jrxmlFilePath = "C:\\Users\\ssyed\\Documents\\workspace-spring-tool-suite-4-4.20.0.RELEASE\\ReportCardWebsite\\src\\main\\resources\\ProgressReport.jrxml";
                    JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(student.getSubjects());
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("name", student.getName());
                    parameters.put("rollno", rollno);
                    parameters.put("dataset", dataSource2);

                    JasperReport report = JasperCompileManager.compileReport(jrxmlFilePath);
                    JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

                    // 3. Export the report to a PDF file
                    JasperExportManager.exportReportToPdfFile(print, pdfFileName);

                    // 4. Add the PDF file to the zip archive
                    File pdfFile = new File(pdfFileName);
                    try (FileInputStream pdfStream = new FileInputStream(pdfFile)) {
                        zipOut.putNextEntry(new ZipEntry(rollno + ".pdf"));
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = pdfStream.read(buffer)) > 0) {
                            zipOut.write(buffer, 0, length);
                        }
                    }
                    pdfFile.delete();
                }

                zipOut.close();

                // 5. Set response headers for the zip file
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=reports.zip");

                // 6. Write the zip data to the response output stream
                response.getOutputStream().write(zipStream.toByteArray());
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } else {
                // Handle the case where no students were found in the database
                // For example, you can return an error message or redirect to an error page.
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the process
            // For example, you can return an error message or redirect to an error page.
        }
    }

    
    @GetMapping("/enterRollNumber")
    public String showEnterRollNumberPage() {
        return "enterRollNumber";
    }

    @PostMapping("/getSingleStudentReport")
    @ResponseBody
    public void getSingleStudentReport(@RequestParam("rollNumber") String rollNumber, HttpServletResponse response) {
        try {
            // 1. Retrieve student data from the database based on the roll number
            Student student = studentRepository.findByRollNumber(rollNumber);

            if (student != null) {
                // 2. Generate the report for the single student
                String pdfFileName = "C:\\Users\\ssyed\\OneDrive\\Desktop\\SCOREME\\SCHOOL\\" + rollNumber + ".pdf";

                // Generate the PDF report
                String jrxmlFilePath = "C:\\Users\\ssyed\\Documents\\workspace-spring-tool-suite-4-4.20.0.RELEASE\\ReportCardWebsite\\src\\main\\resources\\ProgressReport.jrxml";
                JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(student.getSubjects());
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("name", student.getName());
                parameters.put("rollno", rollNumber);
                parameters.put("dataset", dataSource2);

                JasperReport report = JasperCompileManager.compileReport(jrxmlFilePath);
                JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

                // 3. Provide the report as a response
                // Export the report to a PDF file
                JasperExportManager.exportReportToPdfFile(print, pdfFileName);

                // Set response headers for the PDF file
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + rollNumber + ".pdf");

                // Write the PDF data to the response output stream
                try (FileInputStream pdfStream = new FileInputStream(pdfFileName)) {
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = pdfStream.read(buffer)) > 0) {
                        response.getOutputStream().write(buffer, 0, length);
                    }
                }

                // Delete the generated PDF file
                File pdfFile = new File(pdfFileName);
                if (pdfFile.exists()) {
                    pdfFile.delete();
                }

                response.getOutputStream().flush();
                response.getOutputStream().close();
            } else {
                // Handle the case where no student with the provided roll number was found
                // For example, you can return an error message or redirect to an error page.
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that may occur during the process
            // For example, you can return an error message or redirect to an error page.
        }
    }


}
