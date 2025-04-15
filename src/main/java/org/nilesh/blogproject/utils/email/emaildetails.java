package org.nilesh.blogproject.utils.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class emaildetails {
    private String recipent;
    private String msgBody;
    private String subject;
    
}
