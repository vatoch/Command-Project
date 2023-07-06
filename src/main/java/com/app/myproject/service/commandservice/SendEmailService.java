package com.app.myproject.service.commandservice;

import com.app.myproject.service.EmailService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService implements GenericService {
    private final EmailService emailService;
    private String name;

    @Override
    public void execute(String... args){
        StringBuilder builder = new StringBuilder();

        for(int i = 5;i<args.length;i++) {
            builder.append(args[i]).append(" ");
        }
        try {
            emailService.sendEmail(args[0], args[4].replace(".","").toLowerCase(), builder.toString());
        }catch(MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        name = "Send email";
    }


}
