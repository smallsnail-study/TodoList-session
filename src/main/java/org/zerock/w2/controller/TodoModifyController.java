package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "todoModifyController", value = "/todo/modify")
@Log4j2
public class TodoModifyController extends HttpServlet {     // 수정/삭제가 가능한 화면 확인 후 수정기능 controller

    // 특정 글 번호(tno)를 조회하는 Readcontroller과 동일
    private TodoService todoService = TodoService.INSTANCE;
    private final DateTimeFormatter DATETIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override     // GET방식으로 tno파라미터를 이용해서 수정/삭제가 가능한 화면을 보여준다.
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long tno = Long.parseLong(req.getParameter("tno"));
            TodoDTO todoDTO = todoService.get(tno);
            // 데이터 담기
            req.setAttribute("dto", todoDTO);
            req.getRequestDispatcher("/WEB-INF/todo/modify.jsp").forward(req,resp);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServletException("modify get... error");
        }
    }

    @Override   // POST방식으로 수정작업을 처리
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {

        // modify.jsp의 <form>태그에서 전송된 title,finished 등을 이용해서 TodoDTO를 구성
        // boolean 타입으로 처리된 finished는 주의!
        String finishedStr = req.getParameter("finished");

        TodoDTO todoDTO = TodoDTO.builder()
                .tno(Long.parseLong(req.getParameter("tno")))
                .title(req.getParameter("title"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATETIMEFORMATTER))
                .finished(finishedStr != null && finishedStr.equals("on"))
                .build();

        log.info("/todo/modify POST...");
        log.info(todoDTO);

        // TodoDTO는 TodoService 객체로 전달되고, 목록화면으로 다시 이동(수정된 결과 확인)
        try {
            todoService.modify(todoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.sendRedirect("/todo/list");
    }
}
