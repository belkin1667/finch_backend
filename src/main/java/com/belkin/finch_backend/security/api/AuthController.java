package com.belkin.finch_backend.security.api;

import com.belkin.finch_backend.model.User;
import com.belkin.finch_backend.security.dto.RegistrationRequest;
import com.belkin.finch_backend.security.exception.UserAlreadyRegisteredException;
import com.belkin.finch_backend.security.jwt.dto.AuthenticationRequest;
import com.belkin.finch_backend.security.model.ApplicationUser;
import com.belkin.finch_backend.security.service.ApplicationUserService;
import com.belkin.finch_backend.service.UserService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ResponseHeader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.belkin.finch_backend.security.ApplicationUserRole.USER;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Gson gson = new Gson();
    private UserService userService;
    private ApplicationUserService applicationUserService;


    @Autowired
    public AuthController(UserService userService, ApplicationUserService applicationUserService) {
        this.userService = userService;
        this.applicationUserService = applicationUserService;
    }
    @ApiOperation(value="Register new user", notes = "If registration is successful returns 200 OK, else returns 403 Forbidden. " +
                                                     "Does not return JWT token. To get JWT token you shall do 'POST /login' right after you registered")
    @PostMapping("/register")
    public String register(@ApiParam(value = "User credentials. Username will be checked for uniqueness. If it is not unique 403 Forbidden will be returned")
                                   @RequestBody RegistrationRequest registrationRequest) {
        log.info("POST /auth/register with Body: " + gson.toJson(registrationRequest));

        boolean result = applicationUserService.registerUser(registrationRequest);
        if (result)
            return "Success";
        else
            throw new RuntimeException();
    }

    // Endpoint /auth/login is managed by JwtAuthenticationFilter,
    // the following method is used to show correct API with Swagger
    // todo: find the way to configure swagger to work with Filters if such way exists
    @ApiOperation(value="Login the user", notes = "If login is successful, returns JWT Token as 'Bearer <token>' in header 'Authentication', else returns 401 Unauthorized")
    @ResponseHeader(name = "Authentication", description = "Bearer <token>")
    @PostMapping("/login")
    public void login(@RequestBody AuthenticationRequest request) {

    }

}
