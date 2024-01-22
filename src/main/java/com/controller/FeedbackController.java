package com.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.repository.GameRepository;
import com.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.FeedbackDTO;
import com.model.Account;
import com.model.Feedback;
import com.model.Game;
import com.repository.AccountRepository;
import com.repository.FeedbackRepository;
import com.repository.GameRepository;
import com.service.GameService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/game/feedback/")
public class FeedbackController {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @GetMapping("/getFeedback")
    public List<FeedbackDTO> getGameFeedbacks(@RequestParam long gameId) {
        List<FeedbackDTO> feedbackDTOs = new ArrayList<>();

        Game game = gameService.getProductById(gameId);
        if (game != null) {
            List<Feedback> feedbacks = game.getFeedbackList();
            feedbacks.sort(Comparator.comparing(Feedback::getFeedback_id)
                    .thenComparing(Feedback::getCreated_at_time, Comparator.nullsLast(Comparator.naturalOrder()))
                    .reversed());

            for (Feedback feedback : feedbacks) {
                FeedbackDTO feedbackDTO = new FeedbackDTO();
                feedbackDTO.setFeedback_id(feedback.getFeedback_id());
                feedbackDTO.setRating(feedback.getRating());
                feedbackDTO.setComment(feedback.getComment());
                if (feedback.getCreated_at_time() != null) {

                    feedbackDTO.setCreated_at_time(
                            feedback.getCreated_at_time().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                feedbackDTO.setCreated_date(feedback.getCreated_date());
                feedbackDTO.setGame_id(game.getId());
                feedbackDTO.setG_name(game.getName());
                feedbackDTO.setUser_id(feedback.getUser().getId());
                feedbackDTO.setUser_name(feedback.getUser().getUsername());
                feedbackDTO.setAvatar(feedback.getUser().getAvatar());
                feedbackDTO.setRoles(feedback.getUser().getRoles());

                feedbackDTOs.add(feedbackDTO);
            }
        }

        return feedbackDTOs;
    }

    //	"/pharmacy-online/product/feedback/"
    // delete feedback
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteFeedback(@RequestParam long feedbackId, @RequestParam long user_id) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        Account account = accountRepository.findById(user_id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (feedback == null) {
            return new ResponseEntity<>("Feedback not found", HttpStatus.NOT_FOUND);
        }
        feedbackRepository.delete(feedback);
        return new ResponseEntity<>("Feedback deleted successfully", HttpStatus.OK);
    }

    //
    // add feedback
    @PostMapping("add")
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackDTO feedbackDTO, @RequestParam long game_id,
                                         @RequestParam long user_id) {
        Account account = accountRepository.findById(user_id).orElse(null);
        if (account == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        Game game = gameRepository.findById(game_id).orElse(null);
        Feedback feedback = new Feedback();
        if (game != null) {
            feedback.setComment(feedbackDTO.getComment());
            feedback.setRating(feedbackDTO.getRating());
            LocalDate created_date = LocalDate.now();
            LocalTime create_time = LocalTime.now();
            feedback.setCreated_date(created_date);
            feedback.setCreated_at_time(create_time);
            feedbackDTO.setCreated_date(feedback.getCreated_date());

            if (feedback.getCreated_at_time() != null) {
                feedbackDTO.setCreated_at_time(feedback.getCreated_at_time().format(DateTimeFormatter.ofPattern("HH:mm")));
            }

            feedback.setUser(accountRepository.findById(user_id).orElse(account));
            feedback.setGame(gameRepository.findById(game_id).orElse(game));
            feedbackDTO.setUser_id(feedback.getUser().getId());
            feedbackDTO.setGame_id(feedback.getGame().getId());
            feedbackDTO.setG_name(feedback.getGame().getName());
            feedbackDTO.setUser_name(feedback.getUser().getUsername());
            feedbackRepository.save(feedback);
        }

        return new ResponseEntity<>("Feedback added successfully", HttpStatus.OK);
    }
//
//    // calculate average rating
//    @GetMapping("/averageRating/{productId}")
//    public double getAverageRating(@PathVariable Integer productId) {
//        double average = 0;
//        Product product = productService.getProductById(productId);
//        if (product != null) {
//            List<Feedback> feedbacks = product.getFeedbackList();
//            int total = feedbacks.size();
//            if (total > 0) {
//                double totalRating = 0;
//
//                for (Feedback feedback : feedbacks) {
//                    totalRating += feedback.getRating();
//                }
//
//                average = ((double) Math.round((totalRating / total) * 10) / 10);
//            }
//        }
//
//        return average;
//    }

    //
//    @GetMapping("/{productId}/{rating}")
//    public int getTotalFeedbackByRating(@PathVariable int productId, @PathVariable int rating) {
//        return feedbackRepository.countByRatingAndProductId(rating, productId);
//    }
//
//    @GetMapping("/{productId}/countFeedback")
//    public int countFeedbacksByProductId(@PathVariable int productId) {
//        return feedbackRepository.countFeedbacksByProductId(productId);
//    }
}
