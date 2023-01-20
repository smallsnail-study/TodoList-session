package org.zerock.w2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {        // 서비스게층과 컨트롤러에서 사용

    private String mid;
    private String mpw;
    private String mname;
}
