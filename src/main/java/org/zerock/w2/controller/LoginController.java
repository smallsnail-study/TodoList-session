package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
@Log4j2
public class LoginController extends HttpServlet {      // 로그인처리기능 controller

    @Override   // 로그인 화면을 보여준다
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("loign get.............");

        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req,resp);
    }

    @Override   // 실제 로그인처리(POST)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {

        log.info("login post...........");

        // 사용자의 mid와 mpw를 파라미터로 수집한다.
        String mid = req.getParameter("mid");
        String mpw = req.getParameter("mpw");

        /*
        String str = mid+mpw;   // 수집한 mid와 mpw를 이용해서 문자열을 구성한다.

        HttpSession session = req.getSession();

        session.setAttribute("loginInfo",str);  // 구성한 문자열을 HttpSession의 loginInfo이름을 이용해서 저장한다.

        resp.sendRedirect("todo/list"); // 로그인이 처리된 후에는 목록페이지로 리다이렉트 */

        try {   // 정상적으로 로그인 된 경우 Httpsession 을 이용해서 'loginInfo'이름으로 객체를 저장
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpw);
            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("todo/list");
        } catch (Exception e) { // 예외가 발생하는 경우 /login으로 이동(result 파라미터를 전달해서 문제발생을 같이 전달)
            resp.sendRedirect("login?result=error");
        }
    }
}
