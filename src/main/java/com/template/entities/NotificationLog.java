package com.template.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @OneToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "request_at")
    private LocalDateTime requestAt;

    @Column(name = "response_at")
    private LocalDateTime responseAt;


}
