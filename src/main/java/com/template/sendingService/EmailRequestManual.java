package com.template.sendingService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmailRequestManual {

    @NotEmpty(message = "Please enter atleast one email id.")
    private String[] to;

    private String[] cc;

    private String[] bcc;

    @NotEmpty(message = "Subject should not be null or empty.")
    private String subject;

    private String from;

    @NotEmpty(message = "Message should not be null or empty.")
    private String message;

    private Attachment[] attachments;
}
