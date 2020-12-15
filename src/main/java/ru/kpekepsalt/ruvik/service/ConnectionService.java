package ru.kpekepsalt.ruvik.service;

import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.ArrayList;
import java.util.List;

public interface ConnectionService {

    boolean addClient(RSocketRequester requester);
    boolean removeClient(RSocketRequester requester);
    void clear();

}
