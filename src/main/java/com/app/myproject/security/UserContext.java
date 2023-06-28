package com.app.myproject.security;


import com.app.myproject.entity.User;
import com.app.myproject.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequestScope
@RequiredArgsConstructor
public class UserContext {

    private final UserRepository userRepository;

    private User user;

    public User getUser() {
        if (user == null) {
            RequestAttributes attribs = RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = ((ServletRequestAttributes) attribs).getRequest();
            String username = request.getHeader("username");


            this.user = userRepository.findByUsername(username).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );
        }
        return user;
    }


}
