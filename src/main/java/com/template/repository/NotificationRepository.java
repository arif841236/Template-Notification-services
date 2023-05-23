package com.template.repository;


import com.template.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
	
	@Query(value = "SELECT pg_terminate_backend(pg_stat_activity.pid) "
            +" FROM pg_stat_activity WHERE datname = ?1 " 
            +" and (state = 'idle') " 
            +" and query_start < current_timestamp - interval '2 minutes'", nativeQuery = true)
	public void delIdleConnection(String databasename);
}
