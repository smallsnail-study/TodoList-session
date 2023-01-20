package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "todoRegisterController", value = "/todo/register")
@Log4j2
public class TodoRegisterController extends HttpServlet {   // 등록기능 controller

    private TodoService todoService = TodoService.INSTANCE;
    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override   // GET방식으로 등록화면을 보여준다. (jsp의 <form>태그내에 입력항목들을 채운다.)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("/todo/register GET......");

        // Todo를 등록할 때, 로그인 체크하기
        HttpSession session = req.getSession();

        if (session.isNew()) {  // 기존에 JSEEEIONID가 없는 새로운 사용자
            log.info("JESSIONID 쿠키가 새로 만들어진 사용자");
            resp.sendRedirect("/login");
            return;
        }

        // JESSIONID는 있지만 해당 세션 컨텍스트에 loginInfo라는 이름으로 저장된 객체가 없는 경우
        if (session.getAttribute("loginInfo") == null) {
            log.info("로그인한 정보가 없는 사용자.");
            resp.sendRedirect("/login");
            return;
        }

        // 정상적인 경우라면 입력화면으로 이동
        req.getRequestDispatcher("/WEB-INF/todo/register.jsp").forward(req,resp);
    }

    @Override   // jsp의 <form>태그로 입력항목들을 받아서 POST방식으로 처리한다. 처리후에는 목록화면으로 redirect한다.
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {

        TodoDTO todoDTO = TodoDTO.builder()
                .title(req.getParameter("title"))   // HttpServletRequest의 getParameter()로 title/dueDate를 이용해서 TodoDTO를 구성한다.
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATEFORMATTER))
                .build();

        log.info("/todo/register POST......");
        log.info(todoDTO);
        try {
            todoService.register(todoDTO);      // TodoService의 register()를 호출한다.
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 정상적으로 등록된 후에는 GET방식으로 바로 리다이렉트하여 등록하자마자 바로 목록화면으로 이동한다.
        resp.sendRedirect("/todo/list");
    }
}
