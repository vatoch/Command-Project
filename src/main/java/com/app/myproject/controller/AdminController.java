package com.app.myproject.controller;

import com.app.myproject.entity.Command;
import com.app.myproject.repo.CommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
@RequiredArgsConstructor
public class AdminController {

    private final CommandRepository repository;
    @PostMapping("/add")
    public ResponseEntity<Void> addCommand(@RequestBody Command command) {
        repository.save(command);
        return ResponseEntity.ok().build();
    }

}
