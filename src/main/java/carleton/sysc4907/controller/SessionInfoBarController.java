package carleton.sysc4907.controller;

import carleton.sysc4907.model.SessionModel;

public class SessionInfoBarController {

    private final SessionModel model;

    public SessionInfoBarController(SessionModel model) {
        this.model = model;
        System.out.println(model);
    }
}
