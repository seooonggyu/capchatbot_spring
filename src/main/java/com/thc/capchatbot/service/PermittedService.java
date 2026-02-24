package com.thc.capchatbot.service;

public interface PermittedService {
    void check(String target, Integer func, Long userId);
    boolean isPermitted(String target, Integer func, Long userId);
}
