package com.api.resources;

import com.api.annotations.UserScope;
import com.api.entities.User;
import com.api.exceptions.RequestException;
import com.api.exceptions.WrongCredentialsException;
import com.api.services.SessionService;
import com.api.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@RestController
@RequestMapping("sessions")
public class SessionResource {

    private final SessionService sessionService;

    public SessionResource(SessionService sessionService){
        this.sessionService = sessionService;
    }

    @PostMapping(value = "login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) throws SQLException, NoSuchAlgorithmException {
        try{
            return ResponseUtil.render(sessionService.openSession(email, password));
        }catch (RequestException e){
            return ResponseUtil.renderException(e);
        }
    }

    @DeleteMapping("logout")
    @UserScope
    public ResponseEntity<?> logout(HttpServletRequest request) throws SQLException {
        User user = (User) request.getAttribute("user");
        return ResponseUtil.render(sessionService.closeSession(user.getId()));
    }

    @GetMapping("check")
    @UserScope
    public ResponseEntity<?> checkUserSession(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @UserScope
    public ResponseEntity<?> deleteSession(HttpServletRequest request) throws SQLException {
        User user = (User) request.getAttribute("user");
        sessionService.closeSession(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
