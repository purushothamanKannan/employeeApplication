import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebServlet("/Operation")
public class Operation extends HttpServlet {
    private static final String BUCKET_NAME = "my-aws-bucket-24";
    private static final String REGION = "eu-north-1";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Function<String , ResultSet> getValueResultSet = new RegisterDAO()::getValueByID;
            ResultSet resultSet = getValueResultSet.apply(request.getParameter("empId"));
            String imageName = "";
            while(resultSet.next()) {imageName = resultSet.getString("image_url");}
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BUCKET_NAME,
                    Arrays.stream(imageName.split(".amazonaws.com/")).skip(1).collect(Collectors.joining()));
//            Consumer<DeleteObjectRequest> delete = deleteRequest -> RegisterDAO.getAmazonS3(REGION).deleteObject(deleteRequest);
//            delete.accept(deleteObjectRequest);
            RegisterDAO.getAmazonS3(REGION).deleteObject(deleteObjectRequest);

            Consumer<Integer> deleteFunction = new RegisterDAO()::deleteValue;
            deleteFunction.accept(Integer.valueOf(request.getParameter("empId")));

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
