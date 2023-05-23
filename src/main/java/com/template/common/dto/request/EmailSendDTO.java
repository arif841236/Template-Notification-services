package com.template.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendDTO {
	
	  // Class data members
	private String processId;
	private String code;
    private String recipient;
    private String msgBody;
    private String subject;
    private byte[] attachment;
    private String attachedFileName;
    private String source;
}
