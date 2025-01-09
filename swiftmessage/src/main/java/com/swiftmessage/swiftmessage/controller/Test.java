package com.swiftmessage.swiftmessage.controller;

import javax.annotation.ManagedBean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ManagedBean
public class Test {
     
    private String name;
    private String code;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }



    public void answer()
    {
        System.out.println("name entered  "+name);
        System.out.println("code entered  "+code);
    }  
    



    
}