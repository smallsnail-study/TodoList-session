package org.zerock.w2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data  // getter/setter/toString/equals/hashCode 등을 모두 컴파일할 때 생성해준다.
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDTO {

    private Long tno;

    private String title;

    private LocalDate dueDate;

    private boolean finished;
}

// VO와 같은 구조지만 VO는 getter만을 이용해서 읽기 전용으로 구성한다.