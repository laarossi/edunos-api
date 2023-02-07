package com.api.services;

import com.api.dao.SessionDAO;
import com.api.dao.UserDAO;
import com.api.entities.Session;
import com.api.entities.User;
import com.api.exceptions.WrongCredentialsException;
import com.api.utils.JWTUtil;
import com.api.utils.SecurityUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Service
public class SessionService {
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public SessionService(UserDAO userDAO, SessionDAO sessionDAO){
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
    }

    public User openSession(String email, String password) throws SQLException, WrongCredentialsException, NoSuchAlgorithmException {
        password = SecurityUtil.hash(password);
        User user = userDAO.findByCredentials(email, password);
        if(user == null)
            throw new WrongCredentialsException();
        String authKey = JWTUtil.generateToken(user);
        createSession(user.getId(), authKey);
        user.setAuthenticationKey(authKey);
        return user;
    }

    public void createSession(int userId, String sessionKey) throws SQLException {
        closeSession(userId);
        Session session = new Session(userId, sessionKey);
        sessionDAO.save(session);
    }

    public boolean closeSession(int userId) throws SQLException {
        return sessionDAO.delete(userId);
    }

    public Session getSession(String authKey) throws SQLException {
        return sessionDAO.findByKey(authKey);
    }
}
