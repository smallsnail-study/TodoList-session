package org.zerock.w2.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Log4j2
public class W2AppListener implements ServletContextListener {  // 프로젝트가 실행되자마자 실행되었으면 하는 작업 설정

    @Override
    public void contextInitialized(ServletContextEvent sce) {   // 프로젝트를 실행하면 출력되는 로그

        log.info("-------init-------------------------");
        log.info("-------init-------------------------");
        log.info("-------init-------------------------");

        // ServletContextEvent객체를 이용하면 현재 애플리케이션이 실행되는 공간인 ServletContext에 접근 가능
        ServletContext servletContext = sce.getServletContext();

        // ServletContext에는 setAttribute()를 이용해서 원하는 이름으로 객체를 보관할 수 있다.
        // ServletContext에 무언가를 저장하면 모드느 컨트롤러나 JSP 등에서 활용할 수 있디.
        servletContext.setAttribute("appName", "W2");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {     // 프로젝트를 종료하면 출력되는 로그

        log.info("-------destroy-------------------------");
        log.info("-------destroy-------------------------");
        log.info("-------destroy-------------------------");
    }
}
