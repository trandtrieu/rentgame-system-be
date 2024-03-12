package com.controller;

import com.dto.GameDTO;
import com.model.*;
import com.repository.AccountRepository;
import com.repository.WishlistRepository;
import com.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/wishlist/")
public class WishlistController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add-wishlist")
    public ResponseEntity<String> addToWishlist(@RequestParam Long accountId, @RequestParam int gameId) {
        try {
            wishlistService.addToWishlist(accountId, gameId);
            return ResponseEntity.ok("Sản phẩm đã được thêm vào wishlist.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi thêm sản phẩm vào wishlist: " + e.getMessage());
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Set<GameDTO>> getGamesInWishlist(@PathVariable Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null && account.getWishList() != null) {
            Wishlist wishlist = account.getWishList();
            Set<Game> games = wishlist.getGames();

            Set<GameDTO> gameDTOS = games.stream().map(game -> {
                GameDTO gameDTO = new GameDTO();
                gameDTO.setId(game.getId());
                gameDTO.setName(game.getName());
                gameDTO.setPrice(game.getPrice());
                gameDTO.setDateReleased(game.getDateReleased());
                gameDTO.setDescribe(game.getDescribe());
                gameDTO.setNote(game.getNote());
                gameDTO.setAgeLimit(game.getAgeLimit());
                List<String> imageUrls = game.getImages().stream().map(Game_image::getImageUrl)
                        .collect(Collectors.toList());
                gameDTO.setImageUrls(imageUrls);
                List<String> categories = game.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toList());
                gameDTO.setCategories(categories);
                List<String> platforms = game.getPlatforms().stream()
                        .map(Platform::getName)
                        .collect(Collectors.toList());
                gameDTO.setPlatforms(platforms);
                return gameDTO;
            }).collect(Collectors.toSet());

            return ResponseEntity.ok(gameDTOS);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{accountId}/remove-game/{gameId}")
    public ResponseEntity<String> removeGameFromWishlist(@PathVariable Long accountId,
                                                         @PathVariable Integer gameId) {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null && account.getWishList() != null) {
            Wishlist wishlist = account.getWishList();

            Game productToRemove = null;
            for (Game game : wishlist.getGames()) {
                if (game.getId() == gameId) {
                    productToRemove = game;
                    break;
                }
            }
            if (productToRemove != null) {
                wishlist.getGames().remove(productToRemove);
                wishlistRepository.save(wishlist);
                return ResponseEntity.ok("Game removed from wishlist successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count/{accountId}")
    public ResponseEntity<Long> countProductsInWishlist(@PathVariable Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null && account.getWishList() != null) {
            Wishlist wishlist = account.getWishList();
            Set<Game> products = wishlist.getGames();
            long productCount = products.size();
 
            return ResponseEntity.ok(productCount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
