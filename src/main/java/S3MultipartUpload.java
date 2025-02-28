import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class S3MultipartUpload {
    public static void uploadFile(String bucketName, String keyName, Part partFile, String REGION) throws IOException {
        AmazonS3 amazonS3 = RegisterDAO.getAmazonS3(REGION);
        File file = convertPartToFile(partFile);
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
        InitiateMultipartUploadResult InitiateMultipartUploadResult = amazonS3.initiateMultipartUpload(initiateMultipartUploadRequest);
        String uploadId = InitiateMultipartUploadResult.getUploadId();

        long partSize = 5 * 1024 * 1024;
        long fileSize = file.length();
        List<PartETag> partETags = new ArrayList<>();

        try {
            for (int i = 0; i < fileSize / partSize + 1; i++) {
                long bytePosition = i * partSize;
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(keyName)
                        .withUploadId(uploadId)
                        .withPartNumber(i + 1)
                        .withFileOffset(bytePosition)
                        .withFile(file)
                        .withPartSize(partSize);
                UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadRequest);
                partETags.add(uploadPartResult.getPartETag());
            }
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                    bucketName, keyName, uploadId, partETags);
            amazonS3.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
            e.printStackTrace();
        }
    }

    private static File convertPartToFile(Part part) throws IOException {
        // Create a temporary file to store the contents of the part
        String fileName = getFileName(part);  // You can extract the file name from the part
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        try (InputStream inputStream = part.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }

    private static String getFileName(Part part) {
        // Extract the file name from the 'Content-Disposition' header
        String contentDisposition = part.getHeader("Content-Disposition");
        String fileName = null;

        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                fileName = content.split("=")[1].trim().replace("\"", "");
                break;
            }
        }

        return fileName != null ? fileName : "default-file-name.txt";
    }
}
