package org.zerock.w2.domain;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberVO {     // 자바에서 데이터베이스의 데이터를 객체로 처리

    private String mid;
    private String mpw;
    private String mname;
}
