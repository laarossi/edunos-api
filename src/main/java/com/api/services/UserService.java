package com.api.services;

import com.amazonaws.util.IOUtils;
import com.api.dao.UserDAO;
import com.api.entities.User;
import com.api.exceptions.BadRequestException;
import com.api.exceptions.EmailUsedException;
import com.api.exceptions.UsernameUsedException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserDAO userDAO;
    private final AuthenticationService authenticationService;
    private final StorageService storageService;


    public UserService(UserDAO userDAO, AuthenticationService authenticationService, StorageService storageService){
        this.userDAO = userDAO;
        this.authenticationService = authenticationService;
        this.storageService = storageService;
    }

    public List<User> getAllUsers() throws Exception {
        return this.userDAO.findAll();
    }

    public void addUser(String email, String username, String password, String fullName) throws BadRequestException, SQLException, EmailUsedException, UsernameUsedException, NoSuchAlgorithmException {
        if(email == null || email.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty() || fullName == null || fullName.isEmpty())
            throw new BadRequestException("There's an error with the data submitted, please try again later or refresh the page");

        if(userDAO.isEmailExists(email))
            throw new EmailUsedException();

        if(userDAO.isUsernameExists(username))
            throw new UsernameUsedException();

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        userDAO.save(user);
    }

    public User getUser(int idUser) throws SQLException {
        return userDAO.find(idUser);
    }

    public User update(Map<String, String> updatedDataMap) throws SQLException, BadRequestException {
        List<String> allowedFields = Arrays.asList("username", "password", "facebook", "twitter", "github");
        for(String key : updatedDataMap.keySet()){
            if(!allowedFields.contains(key)) throw new BadRequestException(key + " column not authorized");
        }
        User user = authenticationService.getUser();
        for(String key : updatedDataMap.keySet()){
            String value = updatedDataMap.get(key);
            if(value == null || value.isEmpty()) continue;
            this.userDAO.updateColumn(user.getId(), key, updatedDataMap.get(key));
        }
        return userDAO.find(user.getId());
    }

    public User updatePicture(String extension, InputStream inputStream) throws IOException, SQLException {
        User user = authenticationService.getUser();
        String fileName = String.valueOf(UUID.randomUUID()).concat(".").concat(extension);
        this.storageService.uploadFile("e-learning-main-bucket", fileName, inputStream);
        try {
            this.userDAO.updateColumn(user.getId(), "picture", fileName);
        } catch (SQLException e) {
            this.storageService.deleteFile("e-learning-main-bucket", fileName);
            throw e;
        }
        return getUser(authenticationService.getUser().getId());
    }

    public User getProfile() throws SQLException {
        return authenticationService.getUser();
    }
}
