package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebFilter(urlPatterns = {"/todo/*"})   // /todo/...로 시작하는 모든 경로에 대해 필터링
@Log4j2
public class LoginCheckFilter implements Filter {   // 특정한 서블릿이나 JSP등에 도달하는 과정에서 필터링하는 역할

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Filte 인터페이스에는 존재하는 doFilter라는 추상메서드(필터가 필터링이 필요한 로직을 구현하는 부분)

        log.info("Login check filter..........");

        // javax.servlet.Filter 인터페이스의 doFilter()는
        // HttpServletRequest, HttpServletResponse보다 상위 타입의 파라미터를 사용하므로 HTTP와 관련된 작업을 하려면
        // 아래와 같이 다운캐스팅 해주어야 한다.
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        HttpSession session = req.getSession();     // HttpSession을 구한다.

        if (session.getAttribute("loginInfo") != null) { // HttpSession에 loginInfo 이름의 객체 저장여부확인
            chain.doFilter(request, response);
            return;
        }
        // HttpSession에는 없고, 쿠키에 UUID 값만 있는 경우 고려
        // session에 loginInfo 값이 없다면 쿠키를 체크
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        // 세션에도 없고 쿠키도 없다면 로그인으로 리다이렉트
        if (cookie == null) {
            resp.sendRedirect("/login");
            return;
        }

        // 쿠키가 존재하는 상황이라면
        log.info("cookie는 존재하는 상황");
        // uuid값
        String uuid = cookie.getValue();

        try {
            // 데이터베이스 확인
            MemberDTO memberDTO = MemberService.INSTANCE.getByUUID(uuid);

            log.info("쿠키의 값으로 조회한 사용자 정보: " + memberDTO);
            if (memberDTO == null) {
                throw new Exception("Cookie value is not valid");
            }
            // 회원 정보를 세션에 추가
            session.setAttribute("loginInfo", memberDTO);
            chain.doFilter(request, response);

        } catch (Exception e) {
            e.getMessage();
            resp.sendRedirect("/login");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String name) {

        if (cookies == null || cookies.length == 0) {
            return null;
        }

        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(name))
                .findFirst();

        return result.isPresent()?result.get():null;
    }
}
