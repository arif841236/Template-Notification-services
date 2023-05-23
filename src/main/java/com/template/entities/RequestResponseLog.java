package com.template.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RequestResponseLog {
	
	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String processId;

	@Column(name = "request",columnDefinition = "text")
	private String request;

	@Column(name = "response",columnDefinition = "text")
	private String response;

	private String source;
	
	private LocalDateTime requestedTime;
	
	private LocalDateTime responseTime;
	
	private String activityType;
	
	private Integer status;

}
