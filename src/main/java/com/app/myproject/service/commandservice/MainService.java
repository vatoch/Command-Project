package com.app.myproject.service.commandservice;


import com.app.myproject.service.AssemblyAiService;
import com.app.myproject.service.UserService;
import com.app.myproject.servicestorage.ServiceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MainService {
    private final UserService userService;
    private final ServiceStorage serviceStorage;
    private final AssemblyAiService assemblyAiService;
    @Value("${token}")
    private String token;


    public void execute(String username, MultipartFile data) throws IOException {
        String audioUrl = assemblyAiService.getAudioUrl(token,data);
        String id = assemblyAiService.getId(token,audioUrl);
        String text = assemblyAiService.convertAudioToText(token,id);
        String[] words = text.split("\\s+");
        GenericService service = serviceStorage.getMap().get(words[0].replace(".","") + " " + words[1].replace(".",""));
        String [] params = new String[words.length+1];
        params[0] = username;
        for(int i = 0;i<words.length;i++) {
            params[i+1] = words[i];
        }
        service.execute(params);
    }
}
