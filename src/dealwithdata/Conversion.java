package dealwithdata;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.opencsv.CSVWriter;

public class Conversion {

    public String textToString() {
        File file = new File("src\\res\\student-data.txt");
        StringBuilder modifiedText = new StringBuilder();
        

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replace("#", ",");
                line = line.replace("$", "\n");
                modifiedText.append(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file: ");
        }

        String finalText = modifiedText.toString();
        String[] lines = finalText.split("\n");
        String csvText = "";

        for (int i = 0; i < lines.length; i++) {
            if (i==0) {
                csvText+=("id,"+lines[i]+"\n");
            } else {
                csvText+=(i+","+lines[i]+"\n");
            }
        }
        
        return csvText;
    }

    public String xmlToString() throws ParserConfigurationException, SAXException, IOException{
        String xmlText = xmlToText();
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);
        String[] headers = {"id","Course Name","Instructor","Course duration","Course time","Location"};
        csvWriter.writeNext(headers);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlText));
        Document document = builder.parse(is);
        NodeList nodeList = document.getElementsByTagName("row");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String CourseName = element.getElementsByTagName("CourseName").item(0).getTextContent();
                String Instructor = element.getElementsByTagName("Instructor").item(0).getTextContent();
                String Courseduration = element.getElementsByTagName("Courseduration").item(0).getTextContent();
                String Coursetime = element.getElementsByTagName("Coursetime").item(0).getTextContent();
                String Location = element.getElementsByTagName("Location").item(0).getTextContent();
                String[] data = {id, CourseName, Instructor,Courseduration,Coursetime,Location};
                csvWriter.writeNext(data);
            }
        }
        csvWriter.close();
        return stringWriter.toString();
    }

    public String xmlToText() throws IOException {
        File xmlFile = new File("src\\res\\coursedata.xml");
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(xmlFile));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        br.close();
        String xmlText= sb.toString();
        return xmlText;
    }

    public void stringToCSV() throws ParserConfigurationException, SAXException, IOException {
        String text =textToString();
        String textCrs = xmlToString().replace("\"", ""); 

        try {
            byte[] csVBytes = textCrs.getBytes();
            Files.write(Paths.get("src\\outres\\crsData.csv"), csVBytes);

            byte[] csvBytes = text.getBytes();
            Files.write(Paths.get("src\\outres\\stData.csv"), csvBytes);


        } catch (IOException e) {
            System.out.println("An error occurred while writing the file:");
        }
    }

    

}

