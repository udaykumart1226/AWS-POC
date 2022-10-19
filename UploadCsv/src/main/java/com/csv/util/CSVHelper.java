package com.csv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.csv.model.Report;

public class CSVHelper {

	public static String TYPE = "text/csv";
	static String[] HEADERs = { "batchId", "accountId", "customerId", "transactionType", "transactionAmount" };
	
	public static boolean hasCSVFormat(MultipartFile file) {

	    if (!TYPE.equals(file.getContentType())) {
	      return false;
	    }

	    return true;
	  }


	public static List<Report> getCsvReports(InputStream is) {

		List<Report> recordList = new ArrayList<Report>();
		try (BufferedReader filereReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csv = new CSVParser(filereReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
						.withHeader(HEADERs).withIgnoreHeaderCase().withTrim());) {
			Iterable<CSVRecord> csvRecords = csv.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				Report report = new Report();
				if (!StringUtils.hasText(csvRecord.get("batchId")))
					return recordList;
				report.setBatchId(Long.parseLong(csvRecord.get("batchId")));
				report.setAccountId(Long.parseLong(csvRecord.get("accountId")));
				report.setCustomerId(Long.parseLong(csvRecord.get("customerId")));
				report.setTransactionAmount(Double.parseDouble(csvRecord.get("transactionAmount")));
				report.setTransactionType(csvRecord.get("transactionType"));
				recordList.add(report);
			}
			return recordList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}

	}
}
