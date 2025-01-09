package com.swiftmessage.swiftmessage.controller;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@SessionScoped
public class UserController {
    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        keycloakSessionLogout(request);
        tomcatSessionLogout(request);
        return "redirect:/";
    }

    @GetMapping(path = "user")
    public String user(HttpServletRequest request) throws ServletException {
        // keycloakSessionLogout(request);
        // tomcatSessionLogout(request);
        RefreshableKeycloakSecurityContext c = getKeycloakSecurityContext(request);

        return "user: " + c.getToken().getGivenName();
    }

    private void keycloakSessionLogout(HttpServletRequest request) {
        RefreshableKeycloakSecurityContext c = getKeycloakSecurityContext(request);
        KeycloakDeployment d = c.getDeployment();
        c.logout(d);
    }

    private void tomcatSessionLogout(HttpServletRequest request) throws ServletException {
        request.logout();
    }

    private RefreshableKeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request) {
        return (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}