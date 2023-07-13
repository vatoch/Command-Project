package com.app.myproject.controller;

import com.app.myproject.dto.BalanceDTO;
import com.app.myproject.dto.CommandDTO;
import com.app.myproject.dto.UserDTO;
import com.app.myproject.entity.User;
import com.app.myproject.facade.UserFacade;
import com.app.myproject.param.UserParam;
import com.app.myproject.service.BalanceService;
import com.app.myproject.service.CommandService;
import com.app.myproject.service.EmailService;
import com.app.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final BalanceService balanceService;
    private final CommandService commandService;
    private final EmailService emailService;



    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserParam user) {
        userFacade.registerUser(user);
        return ResponseEntity.ok().build();

    }
    @PostMapping("/friendrequest/send/{id}")
    public ResponseEntity<Void> sendFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String id) {
        userService.sendFriendRequest(username,id);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/friends")
    @PageableAsQueryParam
    public ResponseEntity<Page<User>> getFriends(@RequestHeader(name = "username") String username,@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getFriends(username,pageable));
    }


    @PostMapping("/friendrequest/response/{id}")
    public ResponseEntity<Void> responseToFriendRequest(@RequestHeader(name = "username") String username,@PathVariable String id,@RequestBody Integer response ) {
        userFacade.friendRequestResponse(username,response,id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/friend/{id}")
    public ResponseEntity<Void> deleteFriend(@RequestHeader(name = "username") String username,@PathVariable String id) {
        userService.deleteFriend(username,id);
        return ResponseEntity.ok().build();

    }
    @GetMapping("/mycommands")
    @PageableAsQueryParam
   public ResponseEntity<Page<CommandDTO>> getAllCommands(@RequestHeader(name = "username") String username,@PageableDefault @SortDefault(sort = "name",direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(userService.getMyCommands(username,pageable));
   }

   @GetMapping("/balance")
   public ResponseEntity<BalanceDTO> getMyBalance(@RequestHeader(name = "username") String username) {
        return ResponseEntity.ok(balanceService.getBalance(username));
   }
   @GetMapping("/profile")
   public ResponseEntity<UserDTO> viewProfile(@RequestHeader(name = "username") String username) {
        return ResponseEntity.ok(userFacade.viewProfile(username));
   }
   @PostMapping("/fill/")
   public ResponseEntity<Void> fillBalance(@RequestHeader(name = "username") String username,@RequestBody String money) {
        userFacade.fillBalance(username,money);
        return ResponseEntity.ok().build();
   }
   @GetMapping("/allcommands")
   @PageableAsQueryParam
   public ResponseEntity<Page<CommandDTO>> allCommands(@RequestHeader(name = "username") String username,@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commandService.getAllCommands(pageable));
   }

   @DeleteMapping("/account/delete")
   public ResponseEntity<Void> deleteAccount(@RequestHeader(name = "username") String username) {
        userService.deleteAccount(username);
        return ResponseEntity.status(204).build();
   }
//   @PostMapping("/email/send/{username}")
//   public ResponseEntity<Void> sendEmailTest(@RequestHeader(name = "username") String username, @PathVariable("username") String username1) {
//        emailService.sendEmail(username,username1,"dasdasdasdasdasdasdasd");
//        return ResponseEntity.ok().build();
//   }

}
