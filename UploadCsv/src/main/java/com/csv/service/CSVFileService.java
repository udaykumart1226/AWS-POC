package com.csv.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.csv.model.Report;
import com.csv.repository.ReportsRepository;
import com.csv.util.CSVHelper;

@Service
public class CSVFileService {

	@Autowired
	public ReportsRepository reportsRepository;
	
	public void save(MultipartFile file) {
		try {
			List<Report> reports = CSVHelper.getCsvReports(file.getInputStream());
			System.out.println("reports:"+reports);
		      reportsRepository.saveAll(reports);
		} catch (IOException e) {
		      throw new RuntimeException("fail to store csv data: " + e.getMessage());
	    }
	}
	public List<Report> getAllReports() {
	    return reportsRepository.findAll();
	  }
}
