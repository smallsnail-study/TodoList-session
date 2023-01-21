package org.zerock.w2.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

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

        // 자동 로그인 처리
        String auto = req.getParameter("auto");

        boolean rememberMe = auto != null && auto.equals("on"); // auto라는 이름으로 체크박스에서 전송되는 값이 on인지 확인

        try {
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpw);

            if (rememberMe) {   // rememberMe 변수가 true라면 java.util의 UUID를 이용해서 임의의 번호를 생성한다.
                String uuid = UUID.randomUUID().toString();

                MemberService.INSTANCE.updateUuid(mid, uuid);
                memberDTO.setUuid(uuid);

                // 브라우저에 remember-me 이름의 쿠키를 생성해서 전송
                Cookie remeberCookie = new Cookie("remember-me", uuid);
                remeberCookie.setMaxAge(60 * 60 * 24 * 7);  // 쿠키의 유효기간은 1주일
                remeberCookie.setPath("/");

                resp.addCookie(remeberCookie);
            }

            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("/todo/list");
        } catch (Exception e) {
            resp.sendRedirect("/login?result=error");
        }

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
