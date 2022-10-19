package com.csv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csv.model.Report;
import com.csv.response.ResponseMessage;
import com.csv.service.AWSStorageService;
import com.csv.service.CSVFileService;
import com.csv.util.CSVHelper;

@RestController
@RequestMapping("/api/csv")
public class ReportController {

	@Autowired
	public CSVFileService csvFileService;

	@Autowired
	public AWSStorageService AWSService;
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				//csvFileService.save(file);
				AWSService.uploadFile(file);
				message = "Uploaded the file successfully into aws: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));

	}
	
	
	@GetMapping("/reports")
	public ResponseEntity<List<Report>> getAllReports() {
		try {
			List<Report> reports = csvFileService.getAllReports();
			if (reports.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(reports, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/saveinrds")
	public ResponseEntity<ResponseMessage> s3Download(@RequestParam("fileName") String fileName){
		String message = null;
		try {
		AWSService.downloadFile(fileName);
		message="records saved successfully in AWS RDS";
		} catch (Exception e) {
			message = "file not available in s3, please check the file name";
		}
		
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	}

}
