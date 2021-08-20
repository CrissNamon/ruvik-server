package ru.kpekepsalt.ruvik;

/**
 * Urls for mapping
 */
public interface Urls {

    String API = "/api";
    String API_VERSION = "v1";
    String API_PATH = API+"/"+API_VERSION;

    /**
     * Mapping for gate controller
     */
    interface GATE {
        String END_POINT = "/gate";
        String AUTH = "/auth";
        String REGISTER = "/register";
    }

    /**
     * Mapping for conversation controller
     */
    interface CONVERSATION {
        String END_POINT = "/conversation";
        String PENDING = "/pending";
        String INITIATE = "/initiate";
        String ACCEPT = "/accept";
        String MESSAGE = "/message";
        String NEW = "/new";
    }

    /**
     * Mapping for message controller
     */
    interface MESSAGE {
        String END_POINT = "/message";
        String SEND = "/send";
        String SERVICE = "/service";
        String USER = "/user";
        String CONNECT = "/connect";
    }

}
