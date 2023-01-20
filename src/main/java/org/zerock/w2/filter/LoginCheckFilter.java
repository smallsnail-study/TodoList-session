package org.zerock.w2.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

        if (session.getAttribute("loginInfo") == null) {    // 세션에 loginInfo이름의 값이 존재하지 않는다면 login 페이지로 이동시킨다
            resp.sendRedirect("/login");
            return;
        }

        // 다음 필터나 목적지(서블릿,JSP)로 갈 수 있도록 FilterChain의 doFilter()를 실행한다.
        chain.doFilter(request,response);
    }
}
