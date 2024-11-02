package com.example.bookstore.mainService.serviceImpl;


import com.example.bookstore.mainService.services.ClockService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ClockServiceImpl implements ClockService {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Override
    public void startClock() {
        this.startTime = LocalDateTime.now();
    }

    @Override
    public String closeClock() {
        this.endTime = LocalDateTime.now();
        if(startTime != null && endTime != null){
            return "登录时间为：" + this.startTime.toString() +"\n登出时间为：" + this.endTime.toString()  + "\n总共在线时长："+Duration.between(startTime, endTime).getSeconds() + "s";
        }
        return null;
    }
}
