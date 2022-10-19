package com.csv.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.csv.model.Report;
import com.csv.repository.ReportsRepository;
import com.csv.util.CSVHelper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AWSStorageService {

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 amazonS3;

	@Autowired
	public ReportsRepository reportsRepository;

	public String uploadFile(MultipartFile file) {

		File fileObj = convertMultipartFileToFile(file);
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();

		return fileName + "hsa been uploaded";
	}

	public List<Report> downloadFile(String fileName) {
		S3Object s3Object = amazonS3.getObject(bucketName, fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();

		List<Report> reports = CSVHelper.getCsvReports(inputStream);
		System.out.println("reports:" + reports);
		reportsRepository.saveAll(reports);

		return reports;

	}

	public String deleteFile(String fileName) {

		amazonS3.deleteObject(bucketName, fileName);

		return fileName + "removed from the s3";
	}

	public File convertMultipartFileToFile(MultipartFile file) {

		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			log.error("error while converting multipart file into file");
		}

		return convertedFile;
	}
}
