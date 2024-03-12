package com.service;

import com.model.Account;
import com.model.Game;
import com.model.Wishlist;
import com.repository.AccountRepository;
import com.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WishlistService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GameRepository gameRepository;

    public void addToWishlist(Long accountId, int gameId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        Game game = gameRepository.findById(gameId).orElse(null);

        if (account != null && game != null) {
            Wishlist wishList = account.getWishList();
            if (wishList == null) {
                wishList = new Wishlist();
                wishList.setAccount(account);
                wishList.setCreatedDate(new Date());
                account.setWishList(wishList);
            }

            boolean productAlreadyInWishlist = wishList.getGames().contains(game);

            if (!productAlreadyInWishlist) {
                wishList.addGame(game);
                accountRepository.save(account);
            }
        }
    }
}
