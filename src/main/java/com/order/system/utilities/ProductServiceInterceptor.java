package com.order.system.utilities;

import com.order.system.exceptions.models.TokenIncorrectException;
import com.order.system.exceptions.models.TokenNotProvidedException;
import com.order.system.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.order.system.constants.GeneralConstants.TOKEN_PREFIX;

@Component
public class ProductServiceInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;


    public ProductServiceInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle
            (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        switch (request.getRequestURI()) {
            case "/api/login":
            case "/api/register":
                break;
            default:
                return verifyUserToken(request);
        }

        return true;
    }

    private boolean verifyUserToken(HttpServletRequest request) throws TokenNotProvidedException, TokenIncorrectException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            throw new TokenNotProvidedException();
        }

        if (!userRepository.existsByToken(authorization.substring(TOKEN_PREFIX.length()))) {
            throw new TokenIncorrectException();
        }

        return true;
    }

}
