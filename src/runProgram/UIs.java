package runProgram;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.exceptions.CsvValidationException;
import dealwithdata.*;

public class UIs {
    public String id;

    public String getID() {
        return id;
    }

    public void homePage() throws IOException, CsvValidationException, ParserConfigurationException, SAXException {
        Conversion conversion = new Conversion();
        conversion.stringToCSV();
        conversion.xmlToString();

        System.out.println("Welcome to LMS");
        System.out.println("created by {Mohammed.ElBosaty_date}");
        System.out.println("====================================================================================");
        System.out.println("Home page");
        System.out.println("====================================================================================");

        Manipulation manipulation = new Manipulation();
        manipulation.printStData();
        System.out.println("------------------------------------------------------------------------------------");
        viewStStatus();
        choiceMehod();
        
        
    }

    public void viewStStatus() throws IOException, CsvValidationException {
        Console console = System.console();
        Manipulation manipulation = new Manipulation();
        System.out.print("Please select the required student:");
        id = console.readLine();
        int idInt = Integer.parseInt(id);
        
        if ((idInt>0)&&(idInt<=100) ) {
            manipulation.stStatues(id);
        } else {
            System.out.println("Enter a valid id (1~100)");
            System.out.println("------------------------------------------------------------------------------------");
            viewStStatus();

        }

       
    }
    
    public void enrollmentPage() throws IOException, CsvValidationException, ParserConfigurationException, SAXException {
        System.out.println("Enrollment page");
        System.out.println("====================================================================================================");
        Manipulation manipulation = new Manipulation();
        manipulation.printCrsData();
        System.out.println("----------------------------------------------------------------------------------------------------");
    
        Console console = System.console();
        boolean enrolled = false;
        int maxCourses = 6; // maximum number of courses a student can enroll in
    
        while (!enrolled) {
            System.out.println("Please make one of the following:");
            System.out.println("Enter the course id that you want to enroll the student to");
            System.out.println("Enter b to go back to the home page");
            System.out.println("Please select the required action: ");
            String choice = console.readLine().trim();
            String studentId = getID();
    
            if (choice.equals("b")) {
                homePage();
                break;
            } else {
                try {
                    int courseId = Integer.parseInt(choice);
                    if ((courseId>0)&&(courseId<=17)) {
                        boolean courseIdExist = manipulation.isCourseIdExist(studentId,courseId);
                        if (courseIdExist == false) {
                            manipulation.enrollStudentInCourse(studentId, courseId, maxCourses);
                            enrolled = true;
                        } else {
                            System.out.println("the student is enrolled in this course already");
                        }
                    } else {
                        System.out.println("Failed to enroll: The course with id = " + courseId + " is not exist");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong choice! please enter a valid choice");
                }
            }
        }
    }
    
    public void replacementCoursePage() throws CsvValidationException, IOException, ParserConfigurationException, SAXException {
        Manipulation manipulation = new Manipulation();
        Console console = System.console();
        String studentId = getID();
        System.out.println("Please enter the course id to be replaced: ");
        int crsid = Integer.parseInt(console.readLine());
        manipulation.availableCourses(studentId);
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.println("Please enter the required course id to replace: ");
        int crs2id = Integer.parseInt(console.readLine());
        try {
            manipulation.replaceCourseForStudent(studentId, crsid, crs2id);
            manipulation.availableCourses(studentId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        choiceMehod();
    }
    
    public void unenrollmentPage() throws CsvValidationException, ParserConfigurationException, SAXException {
        Manipulation manipulation = new Manipulation();
        boolean isUnenrollmentSuccessful = false;
        while (!isUnenrollmentSuccessful) {
            try {
                String studentId = getID();
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
                JsonArray jsonArray = json.getAsJsonArray(studentId);
                if (jsonArray.size() <= 1) {
                    System.out.println("Failed to unenroll: The student has only one or no courses to unenroll.");
                    choiceMehod();
                } else {
                    System.out.print("Please enter course id: ");
                    Console console = System.console();
                    int courseId = Integer.parseInt(console.readLine());
                    manipulation.unenrollStudentFromCourse(studentId, courseId);
                    System.out.println("Unenrolled successfully from the "+manipulation.getCourseName(courseId)+" course");
                    manipulation.stStatues(studentId);
                    isUnenrollmentSuccessful = true;
                    choiceMehod();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void choiceMehod() throws CsvValidationException, IOException, ParserConfigurationException, SAXException {
        
        Console console = System.console();
        while (true) {
            
            System.out.println("Please choose from the following:");
            System.out.println("a - Enroll in a course");
            System.out.println("d - Unenroll from an existing course");
            System.out.println("r - Replacing an existing course");
            System.out.println("b - Back to the main page");
            System.out.print("please select the required action: ");
            
            String choice = console.readLine();
        
            switch (choice) {
                case "a":
                    enrollmentPage();
                    break;
                case "d":
                    unenrollmentPage();
                    break;
                case "r":
                    replacementCoursePage();
                    break;
                case "b":
                    
                    homePage();
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
            
        }
    }

    
    
   

    

    

    
        
    

    

}
