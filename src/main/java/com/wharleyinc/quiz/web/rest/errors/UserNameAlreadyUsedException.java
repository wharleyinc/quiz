package com.wharleyinc.quiz.web.rest.errors;

public class UserNameAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNameAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
