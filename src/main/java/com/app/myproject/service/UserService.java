package com.app.myproject.service;

import com.app.myproject.entity.Balance;
import com.app.myproject.entity.User;
import com.app.myproject.entity.UserFriend;
import com.app.myproject.enums.FriendshipStatus;
import com.app.myproject.exceptions.UserNotFoundException;
import com.app.myproject.repo.UserFriendRepository;
import com.app.myproject.repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repo;
    private final UserFriendRepository userFriendRepository;
    private final static Logger logg = LoggerFactory.getLogger("mylog");
    private final WebClient webClient;
    private final ObjectMapper mapper;

    @Transactional
    public void registerUser(User user) {
        Balance balance = new Balance();
        balance.setMoney(BigDecimal.valueOf(0));
        balance.setUser(user);
        balance.setLastUpdated(LocalDateTime.now());
        user.setBalance(balance);
        repo.save(user);

    }



    @Transactional
    public void sendFriendRequest(String sender,String receiver){
        Optional<UserFriend> userF1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userF2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userF1.isPresent() || userF2.isPresent()) {
            UserFriend userFriend1 = userF1.orElseGet(userF2::get);
            if(userFriend1.getStatus()==FriendshipStatus.FRIENDS) {
                throw new UnsupportedOperationException("User already in friend list");
            } else if(userFriend1.getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Pending request");
            } else {
                userFriend1.setStatus(FriendshipStatus.PENDING);
                return;

            }
        }
        Optional<User> user1 = repo.findByUsername(sender);
        Optional<User> user2 = repo.findByUsername(receiver);
        if(user1.isEmpty()||user2.isEmpty()) {
            throw new UserNotFoundException();

        }

        UserFriend userfriend2 = UserFriend.builder().status(FriendshipStatus.PENDING).date(LocalDateTime.now()).receiver(user2.get())
                .sender(user1.get()).build();
        userFriendRepository.save(userfriend2);


    }

    @Transactional
    public void acceptFriendRequest(String sender,String receiver) {
        Optional<UserFriend> userFriend = userFriendRepository.finDbyUserNames(receiver,sender);
        UserFriend userFriend1 = userFriend.orElseThrow(UserNotFoundException::new);


        if(userFriend1.getStatus()!=FriendshipStatus.PENDING) {
            throw new UnsupportedOperationException("Can't accept friend request");
        }


        userFriend1.setStatus(FriendshipStatus.FRIENDS);
        userFriend1.setDate(LocalDateTime.now());

    }

    @Transactional
    public void rejectFriendRequest(String sender,String receiver) {
        Optional<UserFriend> userFriend = userFriendRepository.finDbyUserNames(receiver,sender);
        UserFriend userFriend1 = userFriend.orElseThrow(UserNotFoundException::new);
        if(userFriend1.getStatus()!=FriendshipStatus.PENDING) {
            throw new UnsupportedOperationException("Can't reject friend request");
        }
        userFriend1.setStatus(FriendshipStatus.DELETED);
        userFriend1.setDate(LocalDateTime.now());
    }

    @Transactional
    public void deleteFriend(String sender,String receiver) {
        Optional<UserFriend> userFriend1 = userFriendRepository.finDbyUserNames(sender,receiver);
        Optional<UserFriend> userFriend2 = userFriendRepository.finDbyUserNames(receiver,sender);
        if(userFriend1.isEmpty()&&userFriend2.isEmpty()) {
            throw new UnsupportedOperationException("Can't delete friend doesn't exist");
        }
        if(userFriend1.isPresent()) {
            if(userFriend1.get().getStatus()==FriendshipStatus.DELETED||userFriend1.get().getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Already deleted or pending request");
            }
            userFriend1.get().setStatus(FriendshipStatus.DELETED);
        } else  {
            if(userFriend2.get().getStatus()==FriendshipStatus.DELETED||userFriend2.get().getStatus()==FriendshipStatus.PENDING) {
                throw new UnsupportedOperationException("Already deleted or pending request");
            }
            userFriend2.get().setStatus(FriendshipStatus.DELETED);

        }


    }
    @Transactional
    public List<User> getFriends(String username) {
        Optional<List<UserFriend>> userFriends = userFriendRepository.getFriends(username);
        List<UserFriend> userFriends1 = userFriends.orElseGet(List::of);
        List<User> users = new ArrayList<>();
        userFriends1.forEach(x-> {
            if(x.getSender().getUsername().equals(username)) {
                users.add(x.getReceiver());
            } else {
                users.add(x.getSender());
            }
        });
        return users;

    }

    public String getAudioUrl(String accessToken, byte[] fileData) throws IOException {
        String url = "https://api.assemblyai.com/v2/upload";
        AtomicReference<String> result = new AtomicReference<>();
        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(accessToken);
        headers.set(HttpHeaders.TRANSFER_ENCODING, "chunked");

        // Create the multipart request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileData);


        webClient.post()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(responseBody-> {
                    try {
                        result.set(extractAudioUrlFromResponseBody(responseBody));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(result.get());
                    System.out.println(responseBody);
                });

        while(result.get()==null) {

        }

        return result.get();

    }
    public String getId(String accessToken,String audioUrl) {
        String url = "https://api.assemblyai.com/v2/transcript";
        HttpHeaders headers = new HttpHeaders();
         AtomicReference<String> result  = new AtomicReference<>();
        headers.setBearerAuth(accessToken);
        String requestBody = "{\"audio_url\":\"" + audioUrl + "\"}";

        webClient.post().uri(url).headers(h->h.addAll(headers)).bodyValue(requestBody).retrieve().bodyToMono(String.class)
                .subscribe(responseBody-> {
                    String s = null;
                    try {
                        result.set(extractIdFromResponseBody(responseBody));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
        while(result.get()==null) {

        }
        return result.get();
    }

    public String convertAudioToText(String accessToken,String id) throws IOException {
        String url = "https://api.assemblyai.com/v2/transcript/"+id;
        AtomicReference<String> result = new AtomicReference<>();


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        while(true) {
            String responseBody = webClient.get()
                    .uri(url).headers(h -> h.addAll(headers))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if(extractStatusFromResponseBody(responseBody).equals("completed")) {
                result.set(extractTextFromResponseBody(responseBody));
                break;
            }
        }

        while(result.get()==null) {

        }
        return result.get();
    }
    private String extractTextFromResponseBody(String responseBody)throws IOException {
        JsonNode jsonNode = mapper.readTree(responseBody);
        String id = jsonNode.get("text").asText();
        return id;
    }
    private String extractStatusFromResponseBody(String responseBody) throws IOException {
        JsonNode jsonNode = mapper.readTree(responseBody);
        String id = jsonNode.get("status").asText();
        return id;
    }
    private String extractIdFromResponseBody(String responseBody)throws IOException {

        JsonNode jsonNode = mapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        return id;
    }
    private String extractAudioUrlFromResponseBody(String responseBody)throws IOException {

        JsonNode jsonNode = mapper.readTree(responseBody);

        // Extract the value of the "audio_url" field
        String audioUrl = jsonNode.get("upload_url").asText();

        return audioUrl;
    }

    private void h1(String a) {

    }
    private void h1(Integer a) {

    }

}







