package com.app.myproject.facade;

import com.app.myproject.service.AssemblyAiService;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MainFacade {
    private final AssemblyAiService assemblyAiService;

    @Value("${token}")
    private String token;

    public String audioToText(MultipartFile data) throws IOException {
        String audioUrl = assemblyAiService.getAudioUrl(token,data);
        String getId = assemblyAiService.getId(token,audioUrl);
        return assemblyAiService.convertAudioToText(token,getId);

    }

}
