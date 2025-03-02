package org.platform.service;

import org.platform.model.LoginRequest;


public interface TokenService {
    String getToken(LoginRequest loginRequest);
}
