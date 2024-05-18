package com.example.book_store_back_end.dto;

public class Message {
    private boolean ok;
    private String message;

    public Message(boolean ok) {
        this.ok = ok;
        if(ok){
            this.message = "提交成功";
        }
        else {
            this.message = "提交失败";
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
