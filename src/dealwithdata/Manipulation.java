package dealwithdata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class Manipulation {
    public String id;

    public void printStData() throws IOException {
        File fileName = new File("src\\outres\\stData.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
        String row;
        System.out.println("Student list:");
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            System.out.printf("%-4s %-15s %-5s %-30s %-40s %-20s %s\n", data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
        }
        csvReader.close();
        
    }
    public void currentStlist() throws IOException {
        File fileName = new File("src\\outres\\stData.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
        String row;
        System.out.println("-------------------------------");
        System.out.println("Current Student List");
        System.out.println("-------------------------------");
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            System.out.printf("%-4s %-15s %-5s %-30s %-40s %-20s %s\n", data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
        }
        csvReader.close();
        
    }

    public void printCrsData() throws IOException {
        File fileName = new File("src\\outres\\crsData.csv");
        BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
        String row = csvReader.readLine();
        String[] header = row.split(",");
        System.out.printf("%-4s %-30s %-20s %-15s %-12s %s\n", header[0], header[1], header[2], header[3], header[4], header[5]);
        System.out.println("----------------------------------------------------------------------------------------------------"); 
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            System.out.printf("%-4s %-30s %-20s %-15s %-12s %s\n", data[0], data[1], data[2], data[3], data[4], data[5]);
        }
        csvReader.close();
    }
    
    public void studentDetails(String id) throws IOException, CsvValidationException {
        
        String fileName = "src\\outres\\stData.csv";
        CSVReader reader = new CSVReader(new FileReader(fileName));
        System.out.println("====================================================================================");
        System.out.println("Student Details page");
        System.out.println("====================================================================================");
       
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {

            if (nextLine[0].equals(id)) {
                 for (int i = 1; i <= 3; i++) {
                    if (i == 1) {
                        System.out.print("Name:"+nextLine[i]+"         ");
                    } else if (i == 2) {
                        System.out.print("Grade:"+nextLine[i]+"                ");
                    } else {
                        System.out.print("Email:"+nextLine[i]+"\n");
                    }
                }
                break;
            }
        }
        System.out.println("------------------------------------------------------------------------------------");
        reader.close();
    }

    public void enrollStudentInCourse(String studentId, int courseId, int maxCourses) {
        try {
            String classname = getCourseName(courseId);
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
            JsonArray jsonArray;
    
            if (json.has(studentId)) {
                jsonArray = json.getAsJsonArray(studentId);
                if (jsonArray.size() >= maxCourses) {
                    throw new Exception("The student has reached the maximum number of courses.");
                } else {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsInt() == courseId) {
                            throw new Exception("The student has already enrolled in this course.");
                        }
                    }
                    jsonArray.add(courseId);
                    System.out.println("The student is Enrolled Successfully in the "+classname+" course");
                }
            } else {
                jsonArray = new JsonArray();
                jsonArray.add(courseId);
                json.add(studentId, jsonArray);
                System.out.println("The student is Enrolled Successfully in the "+classname+" course");
            }
    
            try (FileWriter writer = new FileWriter("src\\outres\\Student course details.json")) {
                writer.write(json.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void unenrollStudentFromCourse(String studentId, int courseId) {
        boolean isUnenrollmentSuccessful = false;
        while (!isUnenrollmentSuccessful) {
            try {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
                JsonArray jsonArray;
                if (json.has(studentId)) {
                    jsonArray = json.getAsJsonArray(studentId);
                    boolean isRemoved = false;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsInt() == courseId) {
                            jsonArray.remove(i);
                            isRemoved = true;
                            break;
                        }
                    }
                    if (!isRemoved) {
                        throw new Exception("The student is not enrolled in this course.");
                    }
                    if (jsonArray.size() == 0) {
                        throw new Exception("The student must be enrolled in at least one course.");
                    }
                } else {
                    throw new Exception("The student id is not found.");
                }
                try (FileWriter writer = new FileWriter("src\\outres\\Student course details.json")) {
                    writer.write(json.toString());
                    writer.flush();
                    isUnenrollmentSuccessful = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
   
    public void replaceCourseForStudent(String studentId, int course1Id, int course2Id) {
        boolean isReplacementSuccessful = false;
        while (!isReplacementSuccessful) {
            try {
                Gson gson = new Gson();
                JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
                JsonArray jsonArray;
                if (json.has(studentId)) {
                    jsonArray = json.getAsJsonArray(studentId);
                    boolean isCourse1Removed = false;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsInt() == course1Id) {
                            jsonArray.remove(i);
                            isCourse1Removed = true;
                            break;
                        }
                    }
                    if (!isCourse1Removed) {
                        throw new Exception("The student is not enrolled in course 1.");
                    }
                    if (jsonArray.size() == 0) {
                        throw new Exception("The student must be enrolled in at least one course.");
                    }
                    boolean isCourse2Enrolled = false;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsInt() == course2Id) {
                            isCourse2Enrolled = true;
                            break;
                        }
                    }
                    if (!isCourse2Enrolled) {
                        jsonArray.add(course2Id);
                    } else {
                        throw new Exception("The student is already enrolled in course 2.");
                    }
                } else {
                    throw new Exception("The student id is not found.");
                }
                try (FileWriter writer = new FileWriter("src\\outres\\Student course details.json")) {
                    writer.write(json.toString());
                    writer.flush();
                    isReplacementSuccessful = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getID() {
        return id;
    }

    public String getCourseName(int courseId ) throws FileNotFoundException, IOException {
        String courseName = "";
    
        try (BufferedReader br = new BufferedReader(new FileReader("src\\outres\\crsData.csv"))) {
            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == courseId) {
                    courseName = data[1];
                    break;
                }
            }
        }    return courseName;
    }

    public boolean isCourseIdExist(String studentId, int courseId) throws JsonSyntaxException, JsonIOException, FileNotFoundException { 
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
        JsonArray jsonArray;

        if (json.has(studentId)) {
            jsonArray = json.getAsJsonArray(studentId);
            for (int i = 0; i < jsonArray.size();i++) {
                if (jsonArray.get(i).getAsInt() == courseId) {
                    return true;
                }
            }
            return false;
        } 
        return false;
    }
    
    public void stStatues(String id) throws CsvValidationException, IOException {

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
        studentDetails(id);
        System.out.println("Enrolled courses.");
        JsonArray jsonArray = json.getAsJsonArray(id);
        
        if(jsonArray!=null){

            try (BufferedReader br = new BufferedReader(new FileReader("src\\outres\\crsData.csv"))) {
                String header = br.readLine();
                String line;
                int i = 0;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        if (values[0].equals(jsonArray.get(j).getAsString())) {
                            i++;
                            System.out.println(i+"-"+line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("The student hasn't enrolled in any course yet.");
            System.out.println("------------------------------------------------------------------------------------");
        }
            
        
    }
    
    public void availableCourses(String studentId) throws IOException {
        System.out.println("Available courses");
        System.out.println("====================================================================================================");
        System.out.printf("%-4s %-30s %-20s %-15s %-12s %s\n", "id", "Course Name", "Instructor", "Course duration", "Course time", "Location");
        System.out.println("----------------------------------------------------------------------------------------------------");
    
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader("src\\outres\\Student course details.json"), JsonObject.class);
        JsonArray jsonArray = json.getAsJsonArray(studentId);
    
        try (BufferedReader br = new BufferedReader(new FileReader("src\\outres\\crsData.csv"))) {
            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (data[0].equals(jsonArray.get(i).toString())) {
                        System.out.printf("%-4s %-30s %-20s %-15s %-12s %s\n", data[0], data[1], data[2], data[3], data[4], data[5]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
