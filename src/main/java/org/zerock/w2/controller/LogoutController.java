package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
@Log4j2
public class LogoutController extends HttpServlet {     // 세션을 이용하는 로그아웃처리 controller

    @Override   // logout은 중요한 처리작업이기 때문에 POST방식인 경우에만 동작하도록 한다.
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("log out............");

        HttpSession session = req.getSession();

        // 세션을 이용하는 로그아웃 처리 2가지방법
        session.removeAttribute("loginInfo");   // 1. 로그인 확인시에 사용했던 loginInfo를 삭제
        session.invalidate();   // 2. 현재의 HttpSession이 더이상 유효하지 않다고 invalidate() 시킨다.

        resp.sendRedirect("/");
    }
}
