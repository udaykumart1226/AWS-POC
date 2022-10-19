package com.csv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csv.model.Report;

public interface ReportsRepository extends JpaRepository<Report, Long>{

}
