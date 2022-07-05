package com.example.lasttask.service.collection;

import com.example.lasttask.dto.response.ApiResponse;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final static String BUCKET_NAME = "quiet-vector-355212.appspot.com";
    private final static String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/%s?alt=media";
    private final static String KEY = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"quiet-vector-355212\",\n" +
            "  \"private_key_id\": \"ef8ad3af2fc4735924e3412396c556050338a2bb\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDuHLsk0WRJOOWh\\nOCElRqTeq94lP/TpCI9+/Bi9UItQM8gr3DxW0xWVbC6GAQdW7GLPUUCGYCFR+JOQ\\n1P35J8l3zOZ+ENHITgu0QcxAMbfPzODFN0ktJwurVcNQvQF4nf1nA64XLtIxcUoG\\nFNJdehByea0p8w64EdxhLxSVGM5rcBlRYcZ7IM6QTCeUuezGAnyx4GRA+IGVTDi6\\n4sl+5hkdeS2yd6M8J0Ba+nReXGbec1fCUhgM+00TrFgxERhwlq7UkjQZD2RtvfAe\\nv9YGacJZQGRR1BWfGis2a/uGI1Up60Afnu9TFXqhnMNSDQTl07PTfch+R2WIA+C/\\nHC0lbJxPAgMBAAECggEACIufa8LYBoi0NQT+q4fNgvBIGK4i6/oSrEv/10t8tHQn\\nSuusfrE0pRgs00JWMRhqs2YHGst4Zqi1Iw2Bz99Nq67+1qdj3/xtDxsNaYBfSGXh\\n6zIAvk4YbR221L7zvpBs97V6GfrkMEIQlgdJRXGMBY+4VzkR3ilF73bxFii9v71E\\ncGVG13gANjpeNeEJU8otbgwPzwFlUrNy2R/5r2eaQcFvufImxyK52V+ATjWRHIgK\\npYiceXyuDPB/DcLy/+bGDEH2/Ic+AZM8010dMjAgHJHR9aqnGheAuLj0l1ZaQbYy\\nXFcTgMRoZ8dgjnBBq/OmmIemCRfwsFo7hHKOBxTeAQKBgQD+0eVIrBJPPy1a2XRF\\nEVqnQrOs9b0JXxlCJzEKIwbdgl6RtmpUAWlD2xcYLMjSGDzHflLvLoNg2TNWjLka\\nh3rX20fv9iQYz1WxRIOUcxN6OJD0BIilGmEbWOJjrdkXE0+mH1sH1rocIKSsq+Z7\\nuFeotiuRPipb1rVKY3fuQE9o/wKBgQDvNwcF//HMIrCaM4Rvc9a4rNXf4pyFsqHQ\\now97ggt1zFWorXrx61chft7S3Vhxt0jwT9UlKFs9CCCgTsrZB2RVxeoheHH8Sq4U\\nVoqQBNNrUTyo9NjaO7teNUzu40oRrFPBx4KdfTzD7PcuZaBtufdpkjFIlarGnPY4\\ndflPQtb8sQKBgQDRMyj13K67eLaXm2clPPYb01sHaEfl5bkq4loUZGVCfcF+BzCL\\n/wyYtMnlAv9r3JIphcA4tQsO+8J2D9n3ubpUdKY+julbpPxJKGKOAVvAP6hhbUdx\\npJKMjRBvnU5Y73W4gI2Phe4NIY+QYyA5+kGdNNHX0B2egEsd40OWyh1nqQKBgQDL\\nAZbrR46q35Gh+zBb6SGC7eI/MrAEYlkilw7vkKgrGvJiyH56yW7TFTkCGCgTsSis\\nEAPhmcL8I2aUxOkujzBd/iy4pN+O7M2YouzKVd4ZyAJDnC5f3asfwvn8DVVxdB5M\\naAT3pC2qU/JdJI+mLtjBdfhvEdyw2sm1lqCx3BZYsQKBgQD4U07u2DY6U0SQsyIN\\nwOWU+X2xD+5SnAV/bOea2HVuvYfqaVXyZVNeseXcoLfBiFyvd9m+SmtPLe2+Dmk+\\nlFKzBsyOhjlLS2cC6zpzNEFxU4MnMGul3dfqh/+VdxkGTMq2e9qfdPLlYUldOULa\\nLK/HIYRnydXuNtGGuC+KMSnXrQ==\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"firebase-adminsdk-a3gq6@quiet-vector-355212.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"107949304635529143995\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-a3gq6%40quiet-vector-355212.iam.gserviceaccount.com\"\n" +
            "}\n";

    private String TEMP_URL = "";

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(IOUtils.toInputStream(KEY, StandardCharsets.UTF_8));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public ApiResponse upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return new ApiResponse(1, "success", TEMP_URL);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
