package com.wharleyinc.quiz.web.rest.errors;

import com.wharleyinc.quiz.service.UsernameAlreadyUsedException;

public class UserNameAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNameAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
