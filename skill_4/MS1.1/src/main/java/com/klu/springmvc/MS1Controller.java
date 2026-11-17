package com.klu.springmvc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klu.springmvc.model.User;
import com.klu.springmvc.service.JWTService;
import com.klu.springmvc.service.UserService;

@RestController
@RequestMapping("/ms1")
public class MS1Controller {

    @Autowired
    UserService us;

    @Autowired
    JWTService jwts;


    // Addition API
    @GetMapping("/add")
    public String add(@RequestParam("a") int a,
                      @RequestParam("b") int b) {

        int result = a + b;

        return "MS1.1 addition " + result;
    }


    // Test API
    @GetMapping("/test")
    public String test() throws InterruptedException {

        Thread.sleep(6000);

        return "MS1 service is working";
    }


    // Signup API
    @PostMapping("/signup")
    public Object signup(@RequestBody User u) {

        return us.signupservice(u);
    }


    // Signin API - Generates JWT
    @PostMapping("/signin")
    public Object signin(@RequestBody Map<String, String> u) {

        return us.signinservice(u);
    }


    // Protected Banking Account API
    @GetMapping("/account/details")
    public Object accountDetails(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Map<String, Object> response = new HashMap<>();


        // Check whether Authorization header exists
        if (authHeader == null || authHeader.isEmpty()) {

            response.put("code", 401);
            response.put("message", "Unauthorized - Token is missing");

            return response;
        }


        // Check Bearer format
        if (!authHeader.startsWith("Bearer ")) {

            response.put("code", 401);
            response.put("message", "Unauthorized - Invalid Authorization format");

            return response;
        }


        // Extract token
        String token = authHeader.substring(7);


        // Validate JWT
        if (!jwts.validateToken(token)) {

            response.put("code", 401);
            response.put("message", "Unauthorized - Invalid or Expired Token");

            return response;
        }


        // Get username from token
        String username = jwts.getUsername(token);


        // Return account details
        response.put("code", 200);
        response.put("message", "Account details accessed successfully");
        response.put("username", username);
        response.put("accountNumber", "XXXXXX1234");
        response.put("accountType", "Savings");
        response.put("balance", 50000);
        response.put("currency", "INR");

        return response;
    }
}