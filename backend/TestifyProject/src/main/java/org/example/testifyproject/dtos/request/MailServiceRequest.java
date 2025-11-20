package org.example.testifyproject.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.testifyproject.entity.enums.MailType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailServiceRequest {
    private MailType mailType;
//    private
}
