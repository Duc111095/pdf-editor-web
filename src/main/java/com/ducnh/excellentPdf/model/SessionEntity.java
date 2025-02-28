package com.ducnh.excellentPdf.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "sessions")
public class SessionEntity {
	@Id
	private String sessionId;
	
	private String principalName;
	private Date lastResquest;
	private boolean expired;
}
