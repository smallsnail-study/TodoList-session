package org.zerock.w2.filter;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
@Log4j2
public class UTF8Filter implements Filter { // todo로 등록되는 모든 문자열의 한글 처리(한글깨짐 해결)

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("UTF8 filter.........");
        HttpServletRequest req = (HttpServletRequest)request;

        req.setCharacterEncoding("UTF-8");

        chain.doFilter(request,response);
    }
}
