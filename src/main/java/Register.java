import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


import static java.lang.Integer.*;

@WebServlet("/Register")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 11)
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @FunctionalInterface
    interface hyphanLogic {
        String setHyphanIfEmpty(String stringValue);
    }

    private static final String BUCKET_NAME = "my-aws-bucket-24";
    private static final String REGION = "eu-north-1";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Function<String, String> paramsAsString = request::getParameter;
        Function<String, Integer> paramsAsInt = value -> valueOf(request.getParameter(value));
        Part filePart = request.getPart("imageURL");
        Function<Part, String> imageFormat = value -> filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().lastIndexOf("."));
        String imageName = UUID.randomUUID() + imageFormat.apply(filePart);

//        S3MultipartUpload.uploadFile(BUCKET_NAME, imageName, filePart, REGION);

        try (InputStream fileContent = filePart.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(filePart.getSize());
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, imageName, fileContent, metadata);
            RegisterDAO.getAmazonS3(REGION).putObject(putObjectRequest);
            response.getWriter().println("File uploaded successfully to S3.");

            String imageUrl = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + imageName;
            Consumer<Employee> insert = new RegisterDAO()::insert;
            insert.accept(new Employee(paramsAsString.apply("employeeName"), paramsAsInt.apply("employeeAge"),
                    paramsAsInt.apply("employeeExperieance"), paramsAsString.apply("employeeDateOfJoin"),
                    paramsAsString.apply("employeeSalary"), paramsAsString.apply("employeeQualifications"),
                    imageUrl));
            response.setContentType("text/plain");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error uploading file to S3: " + e.getMessage());
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Function<Employee ,ResultSet> getValueResultSet = new RegisterDAO()::getValue;
        ResultSet resultSet = getValueResultSet.apply(new Employee(request.getParameter("employeeName"), 0, 0, "",request.getParameter("employeeSalary"), "", ""));

        JSONArray jsonResponse = new JSONArray();
        Predicate<String> isEmptyString = string -> Objects.isNull(string) || string.isEmpty();
        Function<String ,String> hyphenLogic = string -> isEmptyString.test(string) ? "-" : string;
        try {
            while(resultSet.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("empId", hyphenLogic.apply(resultSet.getString("emp_id")));
                jsonObject.put("employeeName", hyphenLogic.apply(resultSet.getString("employee_name")));
                jsonObject.put("employeeAge", hyphenLogic.apply(resultSet.getString("employee_age")));
                jsonObject.put("employeeExperieance", hyphenLogic.apply(resultSet.getString("employee_experieance")));
                jsonObject.put("employeeDateOfJoin", hyphenLogic.apply(resultSet.getString("employee_date_of_join")));
                jsonObject.put("employeeSalary", hyphenLogic.apply(resultSet.getString("employee_salary")));
                jsonObject.put("employeeQualifications", hyphenLogic.apply(resultSet.getString("qualifications")));
                jsonObject.put("imageURL", hyphenLogic.apply(resultSet.getString("image_url")));
                jsonResponse.put(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
