package com.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ReplyDTO;
import com.model.Account;
import com.model.Feedback;
import com.model.Reply;
import com.repository.AccountRepository;
import com.repository.FeedbackRepository;
import com.repository.ReplyRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/game/reply")
public class ReplyController {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AccountRepository accountRepository;

    // get reply list
    @GetMapping("/getByFeedbackId")
    public List<ReplyDTO> getRepliesByFeedbackId(@RequestParam long feedBackId) {
        List<ReplyDTO> replyDTOs = new ArrayList<>();
        Feedback feedback = feedbackRepository.findById(feedBackId).orElse(null);
        List<Reply> replies = feedback.getReplyList();
        for (Reply reply : replies) {
            ReplyDTO replydto = new ReplyDTO();
            replydto.setReply_id(reply.getReply_id());
            replydto.setFeedback_id(feedback.getFeedback_id());
            LocalDate created_date = LocalDate.now();
            LocalTime create_time = LocalTime.now();

            reply.setCreated_date(created_date);
            reply.setCreated_at_time(create_time);
            replydto.setCreated_date(reply.getCreated_date());
            // Thiết lập user_id dựa trên cách xác định người dùng trong ứng dụng của bạn
            if (reply.getCreated_at_time() != null) {

                replydto.setCreated_at_time(reply.getCreated_at_time().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            replydto.setUser_id(feedback.getUser().getId());
            replydto.setUser_name(reply.getAccount().getUsername());
            replydto.setAvatar(reply.getAccount().getAvatar());
            replydto.setRoles(reply.getAccount().getRoles());
            replydto.setReply_feedback(reply.getReply_feedback());
            replyDTOs.add(replydto);
        }

        return replyDTOs;
    }

    // add reply
    @PostMapping("/add")
    public ResponseEntity<?> addReply(@RequestBody ReplyDTO ReplyDTO, @RequestParam long feedbackId,
                                      @RequestParam long user_id) {
        // Tạo một đối tượng Reply và thiết lập thông tin từ DTO
        Account account = accountRepository.findById(user_id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        ReplyDTO.setUser_id(account.getId());
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback != null) {

            Reply reply = new Reply();
//			reply.setReply_id(ReplyDTO.getReply_id());
            reply.setReply_feedback(ReplyDTO.getReply_feedback());
            LocalDate created_date = LocalDate.now();
            LocalTime create_time = LocalTime.now();

            reply.setCreated_date(created_date);
            reply.setCreated_at_time(create_time);
            ReplyDTO.setCreated_date(reply.getCreated_date());
            // Thiết lập user_id dựa trên cách xác định người dùng trong ứng dụng của bạn
            if (reply.getCreated_at_time() != null) {

                ReplyDTO.setCreated_at_time(reply.getCreated_at_time().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            ReplyDTO.setFeedback_id(feedbackId);
            reply.setAccount(accountRepository.findById(user_id).orElse(null));

            ReplyDTO.setUser_name(reply.getAccount().getUsername());
            reply.setFeedback(feedbackRepository.findById(ReplyDTO.getFeedback_id()).orElse(null));
            replyRepository.save(reply);
        }else{
            return new ResponseEntity<>("Feedback not found", HttpStatus.NOT_FOUND);
        }

//        return new ResponseEntity<>(ReplyDTO, HttpStatus.CREATED);
        return new ResponseEntity<>("Reply added successfully", HttpStatus.OK);
    }

}