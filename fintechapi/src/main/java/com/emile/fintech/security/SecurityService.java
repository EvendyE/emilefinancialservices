package com.emile.fintech.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {

	boolean login(String userName,String password, HttpServletRequest request, HttpServletResponse response);
}
