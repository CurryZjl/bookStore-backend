package com.example.book_store_back_end.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionUtils {
    public static HttpSession getSession(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = servletRequestAttributes.getRequest().getSession();
        return session;
    }

    public static Long getCurrentAuthUid(){
        HttpSession session = getSession();
        if(session != null && session.getAttribute("uid") != null){
            return Long.valueOf(session.getAttribute("uid").toString());
        }
        return Long.valueOf(-1);
    }
}
