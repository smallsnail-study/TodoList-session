package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "todoReadController", value = "/todo/read")
@Log4j2
public class TodoReadController extends HttpServlet {       //특정번호 조회기능 controller

    private TodoService todoService = TodoService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long tno = Long.parseLong(req.getParameter("tno"));

            TodoDTO todoDTO = todoService.get(tno);

            // 데이터 담기
            req.setAttribute("dto", todoDTO);   // dto라는 이름으로 TodoDTO를 담는다.

            // 쿠키 찾기(조회한 Todo 확인하기)
            // 현재 요청(Request)에 있는 모든 쿠키중에 조회 목록 쿠키(viewTodos를 찾아내는 메소드)
            Cookie viewTodoCookie = findCookie(req.getCookies(), "viewTodos");  // viewTodos 이름의 쿠키를 찾는다.
            String todoListStr = viewTodoCookie.getValue();
            boolean exist = false;

            if (todoListStr != null && todoListStr.indexOf(tno+"-") >= 0) { // 특정한 tno가 쿠키의 내용물이 있는지 확인
                exist = true;
            }

            log.info("exist: " + exist);

            if (!exist) {       // 조회한 적이 없는 번호라면 쿠키의 내용물을 갱신해서 브라우저로 보낸다.(다시 경로나 유효시간을 세팅해야한다)
                todoListStr += tno+"-";
                viewTodoCookie.setValue(todoListStr);
                viewTodoCookie.setMaxAge(60* 60* 24);
                viewTodoCookie.setPath("/");
                resp.addCookie(viewTodoCookie);
            }
            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req, resp);  // read.jsp로 TodoDTO를 전달

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {

        Cookie targetCookie = null;

        if (cookies != null && cookies.length > 0) {
            for (Cookie ck : cookies) {
                if (ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }

        if (targetCookie == null) {
            targetCookie = new Cookie(cookieName, "");
            targetCookie.setPath("/");
            targetCookie.setMaxAge(60*60*24);
        }

        return targetCookie;
    }
}
