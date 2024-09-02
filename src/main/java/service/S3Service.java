package service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class S3Service {
    private S3Client s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostConstruct
    public void init() {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file, String groupId) throws IOException {
        // image/ 폴더 하위에 그룹 ID를 사용하여 경로 설정
        String fileName = "image/" + groupId + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // PutObjectRequest 생성 시, InputStream으로 파일 내용을 읽어 S3에 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // MultipartFile의 InputStream을 사용하여 S3에 업로드
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return getFileUrl(fileName);
    }


    public String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    public String uploadAudioFile(byte[] audioData, String groupId) {
        String fileName = "audio/" + groupId + "/" + System.currentTimeMillis() + ".mp3";

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("audio/mpeg")
                .build();

        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(new ByteArrayInputStream(audioData), audioData.length));

        return getFileUrl(fileName);
    }


    //그룹 수정시 유저 이미지
    public String uploadProfileImage(MultipartFile file, Long groupId, Long userId, Long targetUserId) throws IOException {
        // profile/ 폴더 하위에 그룹 ID, 사용자 ID, 대상 사용자 ID를 사용하여 경로 설정
        String fileName = String.format("profile/%d/%d/%d/%s", groupId, userId, targetUserId, System.currentTimeMillis() + "_" + file.getOriginalFilename());

        // PutObjectRequest 생성 시, InputStream으로 파일 내용을 읽어 S3에 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // MultipartFile의 InputStream을 사용하여 S3에 업로드
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return getFileUrl(fileName);
    }

    public String uploadGroupImage(MultipartFile groupImage, Long groupId) throws IOException {
        if (groupId == null) {
            throw new IllegalArgumentException("S3그룹이미지 저장시 그룹아이디는 null 이 들어올 수 없음.");
        }
        String folderName = "group/" + groupId;
        return uploadFile(groupImage, folderName);
    }

    public String uploadGalleryImage(MultipartFile file, Long groupId) throws IOException {
        if (groupId == null) {
            throw new IllegalArgumentException("S3 갤러리 이미지 저장시 그룹 아이디는 null이 될 수 없습니다.");
        }
        // Gallery 폴더 하위에 그룹 ID를 사용하여 경로 설정
        String fileName = "gallery/" + groupId + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // PutObjectRequest 생성 시, InputStream으로 파일 내용을 읽어 S3에 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        // MultipartFile의 InputStream을 사용하여 S3에 업로드
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return getFileUrl(fileName);
    }

    public String uploadAIImage(String imageUrl, String groupId) throws IOException, URISyntaxException {
        // 이미지 URL로부터 InputStream을 얻음
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();

        // 파일 이름 생성
        String fileName = "ai-image/" + groupId + "/" + System.currentTimeMillis() + ".png";

        // S3에 이미지 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("image/png")
                .build();

        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, Files.size(Paths.get(url.toURI()))));

        return getFileUrl(fileName);
    }

}
