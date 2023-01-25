package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.TodoDAO;
import org.zerock.w2.domain.TodoVO;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum TodoService {
    INSTANCE;

    private TodoDAO dao;
    private ModelMapper modelMapper;

    TodoService() {

        dao = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    // 목록 기능 구현
    public List<TodoDTO> listAll() throws Exception {
        // TodoDAO에서 가져온 TodoVO의 목록을 모두 TodoDTO로 변환해서 반환해야 한다.

        List<TodoVO> voList = dao.selectAll();

        log.info("voList........................");
        log.info(voList);

        // ModelMapper와 Java Stream의 map()을 이용하면 간단하게 처리할 수 있다.
        List<TodoDTO> dtoList = voList.stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());

        return dtoList;
    }
    // 등록 기능 구현
    public void register(TodoDTO todoDTO) throws Exception {

        // TodoDTO를 파라미터로 받아서 TodoVO로 변환하는 과정이 필요
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

        // ModelMapper로 처리된 TodoVO를 println을 이용해서 확인
//        System.out.println("todoVO: " + todoVO);
        log.info(todoVO);   // println 대신 log기능으로 대체

        // TodoDAO를 이용해서 insert()를 실행하고 TodoVO를 등록
        dao.insert(todoVO); // int를 반환하므로 이를 이용해서 예외처리도 가능
    }

    // 조회 기능 구현
    public TodoDTO get(Long tno) throws Exception {     // TodoDTO를 반환한다.

        log.info("tno: " + tno);
        TodoVO todoVO = dao.selectOne(tno);     // TodoDAO의 selectOne()를 통해서 TodoVO 객체를 가져온다.
        TodoDTO todoDTO = modelMapper.map(todoVO, TodoDTO.class);   // MOdelMapper를 이용해서 TodoDTO로 변환한다.
        return todoDTO;
    }

    // 삭제 기능 구현
    public void remove(Long tno) throws Exception {
        // 번호(tno)만을 이용
        log.info("tno: " + tno);
        dao.deleteOne(tno);
    }

    // 수정 기능 구현
    public void modify(TodoDTO todoDTO) throws Exception {
        // TodoDTO타입을 파라미터로 이용한다.
        log.info("todoDTO: " + todoDTO);

        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);

        dao.updateOne(todoVO);
    }
}
