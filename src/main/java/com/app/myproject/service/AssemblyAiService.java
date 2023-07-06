package com.app.myproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AssemblyAiService {
    private final WebClient webClient;
    private final ObjectMapper mapper;
    @Value("${uploadUrl}")
    private String uploadUrl;
    @Value("${transcriptUrl}")
    private String transcriptUrl;
    @Value("${transformUrl}")
    private String transformUrl;

    public String getAudioUrl(String accessToken, byte[] fileData) throws IOException {

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(accessToken);
        headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");

        // Create the multipart request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileData);


        String responseBody = webClient.post()
                .uri(uploadUrl)
                .headers(h -> h.addAll(headers))
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block();


        return extractAudioUrlFromResponseBody(responseBody);

    }
    private String extractAudioUrlFromResponseBody(String responseBody)throws IOException {

        JsonNode jsonNode = mapper.readTree(responseBody);

        return jsonNode.get("upload_url").asText();
    }

    public String getId(String accessToken,String audioUrl) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String requestBody = "{\"audio_url\":\"" + audioUrl + "\"}";

        String response = webClient.post().uri(transcriptUrl).headers(h->h.addAll(headers)).bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
        return extractIdFromResponseBody(response);
    }
    private String extractIdFromResponseBody(String responseBody)throws IOException {

        JsonNode jsonNode = mapper.readTree(responseBody);

        return jsonNode.get("id").asText();
    }
    public String convertAudioToText(String accessToken,String id) throws IOException {
        String url = transformUrl+id;

        String responseBody;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        do {
            responseBody = webClient.get()
                    .uri(url).headers(h -> h.addAll(headers))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        }while (!extractStatusFromResponseBody(responseBody).equals("completed"));


        return extractTextFromResponseBody(responseBody);
    }
    private String extractTextFromResponseBody(String responseBody)throws IOException {
        JsonNode jsonNode = mapper.readTree(responseBody);
        return jsonNode.get("text").asText();
    }
    private String extractStatusFromResponseBody(String responseBody) throws IOException {
        JsonNode jsonNode = mapper.readTree(responseBody);
        return jsonNode.get("status").asText();
    }





}
