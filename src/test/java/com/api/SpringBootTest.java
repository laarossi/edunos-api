package com.api;

import com.api.resources.UserResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@org.springframework.boot.test.context.SpringBootTest
@AutoConfigureMockMvc
public class SpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextTest(){}

}
