package com.api.services;

import com.api.dao.UserDAO;
import com.api.entities.Session;
import com.api.entities.User;
import com.api.utils.RequestUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Service
public class AuthenticationService {

    private final UserDAO userDAO;

    private final SessionService sessionService;

    public AuthenticationService(UserDAO userDAO, SessionService sessionService){
        this.userDAO = userDAO;
        this.sessionService = sessionService;
    }

    public User getUser(String authenticationKey) throws SQLException {
        if(authenticationKey == null || authenticationKey.isEmpty()) return null;
        Session session = sessionService.getSession(authenticationKey);
        if(session == null) return null;
        return this.userDAO.find(session.getIdUser());
    }

    public User getUser(HttpServletRequest request) throws SQLException {
        return getUser(request.getHeader("Authentication"));
    }

    public User getUser() throws SQLException {
        HttpServletRequest httpServletRequest = RequestUtil.getCurrentRequest();
        User user = (User) httpServletRequest.getAttribute("user");
        return user != null ? user : getUser(httpServletRequest);
    }

}
