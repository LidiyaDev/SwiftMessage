package com.swiftmessage.swiftmessage.controller;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Controller
public class HomeController {

    private String fullName;

    

    @RequestMapping("/")
    public String index() {

        return "/home.xhtml";
    }



    public String getFullName() {

        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        RefreshableKeycloakSecurityContext c = (RefreshableKeycloakSecurityContext) request

                .getAttribute(KeycloakSecurityContext.class.getName());
                String usern = c.getToken().getPreferredUsername(); 
            
                fullName= c.getToken().getGivenName();
        return fullName;
    }



    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}