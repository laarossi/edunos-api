package com.api.interceptors;

import com.api.annotations.UserScope;
import com.api.entities.Session;
import com.api.entities.User;
import com.api.services.AuthenticationService;
import com.api.services.SessionService;
import com.api.services.UserService;
import com.sun.xml.internal.ws.client.sei.MethodHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method method;
        try{
             method = ((HandlerMethod) handler).getMethod();
        }catch (Exception e){
            return false;
        }
        if(!method.isAnnotationPresent(UserScope.class))
            return true;

        User user = this.authenticationService.getUser(request.getHeader("Authentication"));
        if(user == null)
            return false;

        request.setAttribute("user", user);
        return true;
    }
}
