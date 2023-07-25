package com.app.myproject.controller;

import com.app.myproject.annotations.TimeLog;
import com.app.myproject.facade.MainFacade;
import com.app.myproject.service.commandservice.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final MainService service;
    private final MainFacade mainFacade;

    @PostMapping(value = "/command")
    @TimeLog
    public ResponseEntity<Void> executeCommand(@RequestHeader(name = "username") String username,@RequestBody MultipartFile data) throws IOException {
        service.execute(username,data);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/test",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> test(@RequestBody MultipartFile data) throws IOException {
        return ResponseEntity.ok(mainFacade.audioToText(data));
    }


}
