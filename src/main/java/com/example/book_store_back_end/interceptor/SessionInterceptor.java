package com.example.book_store_back_end.interceptor;

import com.example.book_store_back_end.constants.UserRole;
import com.example.book_store_back_end.services.UserServive;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    private UserServive userServive;

    @Autowired
    public SessionInterceptor(UserServive userServive) {
        this.userServive = userServive;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            System.out.println("OPTIONS is invoked");
            return true;
        }

        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("uid") != null){
            Long uid = Long.valueOf(session.getAttribute("uid").toString());
            Optional<UserRole> userRole = userServive.findRoleByUid(uid);
            if(userRole.isEmpty()){
                response.setStatus(400);
                return false;
            }
            else if(userRole.get() == UserRole.BANNED){
                System.out.println("Session is banned");
                response.setStatus(403);
                return false;
            }
        }

        if (session != null && session.getAttribute("uid") != null && session.getAttribute("role") != null) {
            //System.out.println("Session is already logged in");
            return true;
        }
        System.out.println("Session is not logged in yet");
        response.setStatus(401);
        return false;
    }
}
