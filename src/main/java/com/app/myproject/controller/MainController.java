package com.app.myproject.controller;

import com.app.myproject.annotations.TimeLog;
import com.app.myproject.service.AssemblyAiService;
import com.app.myproject.service.commandservice.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {
    private final MainService service;
    @Value("${token}")
    private String token;
    private final AssemblyAiService assemblyAiService;

    @PostMapping("/command")
    @TimeLog
    public ResponseEntity<Void> executeCommand(@RequestHeader(name = "username") String username,@RequestBody byte[] data) throws IOException {
        service.execute(username,token,data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody byte[]data) throws IOException {
        String audioUrl = assemblyAiService.getAudioUrl(token,data);
        String id = assemblyAiService.getId(token,audioUrl);
        String text = assemblyAiService.convertAudioToText(token,id);
        return ResponseEntity.ok(text);
    }


}
