package org.zerock.w2.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString
// ModelMapper를 이용할 떄 대상 클래스의 생성자를 이용할 수 있도록 아래 2개의 생성자를 추가한다.
@AllArgsConstructor // 모든 필드값이 필요한 생성자
@NoArgsConstructor  // 파라미터가 없는 생성자
public class TodoVO {

    private Long tno;

    private String title;

    private LocalDate dueDate;

    private boolean finished;

}

