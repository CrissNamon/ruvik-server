package ru.kpekepsalt.ruvik;

public interface Urls {

    String API = "/api";
    String API_VERSION = "v1";
    String API_PATH = API+"/"+API_VERSION;

    interface GATE {
        String END_POINT = "/gate";
    }

}
