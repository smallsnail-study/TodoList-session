package org.zerock.w2.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
@Log4j2
public class LoginListener implements HttpSessionAttributeListener {    // HttpSession에 setAttribute(),removeAttribute()등의 작업을 감지하는 기능

    // 새로운 로그인이 발생하면 자동실행되는 로그
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {

        String name = event.getName();

        Object obj = event.getValue();

        if (name.equals("loginInfo")) {
            log.info("A user logined.............");
            log.info(obj);
        }
    }
}
