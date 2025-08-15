package com.intro.sandbox.service;

import com.intro.sandbox.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final RegisterRepository registerRepository;

}
