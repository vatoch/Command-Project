package com.app.myproject.service.commandservice;

import com.app.myproject.service.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageService implements GenericService {

    private final MessageService messageService;
    private String name;

    @Override
    public void execute(String... args) {
        StringBuilder builder = new StringBuilder();

        for(int i = 5;i<args.length;i++) {
            builder.append(args[i]).append(" ");
        }
        messageService.sendMessage(args[0],args[4],builder.toString());

    }

    @Override
    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        name = "Send message";
    }




}
