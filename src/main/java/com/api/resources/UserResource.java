package com.api.resources;

import com.api.annotations.UserScope;
import com.api.exceptions.RequestException;
import com.api.services.UserService;
import com.api.utils.ResponseUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserResource {

    UserService userService;

    public UserResource(UserService userService){
        this.userService = userService;
    }

    @UserScope
    @GetMapping
    public ResponseEntity<?> get() throws Exception {
        return ResponseUtil.render(userService.getAllUsers());
    }

    @UserScope
    @GetMapping(value = "{id_user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("id_user") int idUser) throws SQLException {
        return ResponseUtil.render(userService.getUser(idUser));
    }

    @UserScope
    @GetMapping("profile")
    public ResponseEntity<?> getProfile() throws SQLException {
        return ResponseUtil.render(userService.getProfile());
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> addUser(@RequestParam String userName,
                                     @RequestParam String email,
                                     @RequestParam String password,
                                     @RequestParam String fullName) throws Exception {
        try {
            userService.addUser(email, userName, password, fullName);
        } catch (RequestException e) {
            return ResponseUtil.renderException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @UserScope
    public ResponseEntity<?> update(@RequestBody Map<String, String> updatedDataMap) throws SQLException {
        try {
            return ResponseUtil.render(this.userService.update(updatedDataMap));
        } catch (RequestException e) {
            return ResponseUtil.render(e);
        }
    }

    @PutMapping("picture")
    @UserScope
    public ResponseEntity<?> update(@RequestParam("file") MultipartFile multipartFile) throws IOException, SQLException {
        return ResponseUtil.render(this.userService.updatePicture(FilenameUtils.getExtension(multipartFile.getOriginalFilename()), multipartFile.getInputStream()));
    }

}
