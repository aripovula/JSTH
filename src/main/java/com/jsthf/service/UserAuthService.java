package com.jsthf.service;

import com.jsthf.model.UserAuth;

public interface UserAuthService {

	public UserAuth findUserByEmail(String email);
	public void saveUser(UserAuth user);
}
