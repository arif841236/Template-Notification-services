package com.template.repository;


import com.template.entities.RequestResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestResponseLogRepository extends JpaRepository<RequestResponseLog, Long>{

	RequestResponseLog findByProcessIdAndActivityTypeAndStatus(String processID, String ActivityType, Integer status);

}
