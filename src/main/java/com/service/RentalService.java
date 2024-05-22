package com.service;

import com.dto.GameDTO;
import com.dto.NickDTO;
import com.dto.RentalDTO;
import com.exception.*;
import com.model.*;
import com.repository.AccountRepository;
import com.repository.GameRepository;
import com.repository.NickRepository;
import com.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;
    private final NickRepository nickRepository;
    private final GameRepository gameRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository, NickRepository nickRepository, GameRepository gameRepository, AccountRepository accountRepository) {
        this.rentalRepository = rentalRepository;
        this.nickRepository = nickRepository;
        this.gameRepository = gameRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public RentalDTO rentAccountByHour(long userId, long gameId, int hours) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        List<Nick> gameAccounts = findAvailableGameAccounts(gameId);

        if (gameAccounts.isEmpty()) {
            throw new GameAccountNotFoundException("No game account found for the requested game");
        }

        if (game.getStock() <= 0) {
            throw new OutOfStockException("Game is out of stock");
        }
        logger.info("pass stock");
        if (checkIfAccountIsInUseByAnotherUser(gameAccounts, userId)) {
            throw new GameAccountInUseException("Game account is currently in use by another user");
        }
        logger.info("pass check acc was used");

        if (account.getBalance() < game.getPrice() * hours) {
            throw new InsufficientBalanceException("Insufficient balance to rent the game for specified hours");
        }
        logger.info("pass check balance");

        RentalDTO rentalDTO;
        if (isAccountRentedByCurrentUser(gameAccounts, userId)) {
            logger.info("hello extend");

            rentalDTO = extendRentalTimeForCurrentUser(gameAccounts, userId, hours);
        } else {
            logger.info("hello rent new");

            rentalDTO = rentNewAccountForCurrentUser(account, gameAccounts, game, userId, hours);
        }

        return rentalDTO;
    }

    private List<Nick> findAvailableGameAccounts(long gameId) {
        return nickRepository.findAllByGames_Id(gameId);
    }

    private boolean checkIfAccountIsInUseByAnotherUser(List<Nick> gameAccounts, Long userId) {
        return gameAccounts.stream()
                .anyMatch(gameAccount ->
                        "Rented".equals(gameAccount.getStatus()) &&
                                (gameAccount.getRentedBy() == null || !gameAccount.getRentedBy().equals(userId))
                );
    }

    private boolean isAccountRentedByCurrentUser(List<Nick> gameAccounts, long userId) {
        return gameAccounts.stream()
                .anyMatch(gameAccount -> gameAccount.getRentedBy() != null && gameAccount.getRentedBy().equals(userId));
    }

    private RentalDTO extendRentalTimeForCurrentUser(List<Nick> gameAccounts, long userId, int hours) {
        Nick selectedGameAccount = gameAccounts.stream()
                .filter(gameAccount -> gameAccount.getRentedBy().equals(userId))
                .findFirst()
                .orElseThrow(() -> new GameAccountNotFoundException("Game account not found"));

        LocalDateTime newRentalEnd = selectedGameAccount.getRentalEnd().plusHours(hours);
        selectedGameAccount.setRentalEnd(newRentalEnd);
        nickRepository.save(selectedGameAccount);

        logger.info("Game rental time extended successfully!");
        return convertToRentalDTO(selectedGameAccount.getRental());
    }

    private RentalDTO rentNewAccountForCurrentUser(Account account, List<Nick> gameAccounts, Game game, long userId, int hours) {
        Random random = new Random();
        Nick selectedGameAccount = gameAccounts.get(random.nextInt(gameAccounts.size()));
        selectedGameAccount.setStatus("Rented");
        selectedGameAccount.setRentedBy(userId);
        selectedGameAccount.setRentalStart(LocalDateTime.now());
        selectedGameAccount.setRentalEnd(LocalDateTime.now().plusHours(hours));
        nickRepository.save(selectedGameAccount);

        // Deduct user's balance
        account.setBalance(account.getBalance() - game.getPrice() * hours);
        accountRepository.save(account);

        // Create new rental
        Rental rental = createRental(selectedGameAccount, game);
        logger.info("Gamerented successfully!");
        return convertToRentalDTO(rental);
    }

    private Rental createRentalByHour(Nick user, Game game, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        game.setStock(game.getStock() - 1);
        gameRepository.save(game);

        Rental rental = new Rental();
        rental.setNick(user);
        rental.setGame(game);
        rental.setRentalStart(startDateTime);
        rental.setRentalEnd(endDateTime);
        rentalRepository.save(rental);

        return rental;
    }
    private Rental createRental(Nick user, Game game) {
        game.setStock(game.getStock() - 1);
        gameRepository.save(game);
        Rental rental = new Rental();
        rental.setNick(user);
        rental.setGame(game);
        rental.setRentalStart(LocalDateTime.now());
        rental.setRentalEnd(LocalDateTime.now());

        rentalRepository.save(rental);

        return rental;}


    @Transactional
    public void returnAccount(long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));
        rental.setRentalEnd(LocalDate.now().atStartOfDay());
        rentalRepository.save(rental);

        Game game = rental.getGame();
        game.setStock(game.getStock() + 1);
        gameRepository.save(game);
    }
    public List<NickDTO> getRentedAccountsByUser(long userId) {
        updateExpiredRentals();

        List<Nick> rentedAccounts = nickRepository.findByRentedBy(userId);
        return rentedAccounts.stream()
                .map(this::convertToNickDTO)
                .collect(Collectors.toList());
    }

    private NickDTO convertToNickDTO(Nick nick) {
        NickDTO nickDTO = new NickDTO();
        nickDTO.setId(nick.getId());
        nickDTO.setUsername(nick.getUsername());
        nickDTO.setPassword(nick.getPassword());
        nickDTO.setEmail(nick.getEmail());
        nickDTO.setPhone(nick.getPhone());
        nickDTO.setNote(nick.getNote());
        nickDTO.setStatus(nick.getStatus());
        nickDTO.setRentedBy(nick.getRentedBy());
        nickDTO.setReturnDate(nick.getRentalEnd());
        nickDTO.setRentalDate(nick.getRentalStart());
        // Tính toán thời gian còn lại
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rentalEnd = nick.getRentalEnd();
        if (rentalEnd != null) {
            long remainingTime = Duration.between(now, rentalEnd).getSeconds();
            nickDTO.setRemainingTime(remainingTime);
        } else {
            nickDTO.setRemainingTime(0); // Hoặc giá trị phù hợp khác
        }
        List<GameDTO> games = nick.getGames().stream()
                .map(this::convertToGameDTO)
                .collect(Collectors.toList());
        nickDTO.setGames(games);
        return nickDTO;
    }
    private void updateExpiredRentals() {
        LocalDateTime now = LocalDateTime.now();
        List<Nick> expiredRentals = nickRepository.findByRentalEndBefore(now);

        for (Nick nick : expiredRentals) {
            nick.setStatus("Available");
            nick.setRentedBy(null);
            nick.setRentalStart(null);
            nick.setRentalEnd(null);
            nickRepository.save(nick);
        }
    }
    private RentalDTO convertToRentalDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setGameId(rental.getGame().getId());
        rentalDTO.setGameName(rental.getGame().getName());
        rentalDTO.setRentalDate(LocalDate.from(rental.getRentalStart()));
        rentalDTO.setReturnDate(LocalDate.from(rental.getRentalEnd()));
        rentalDTO.setStatus(rental.getStatus());
        rentalDTO.setUsername(rental.getNick().getUsername());
        rentalDTO.setPassword(rental.getNick().getPassword());
        return rentalDTO;
    }

    private GameDTO convertToGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setName(game.getName());
        List<String> platforms = game.getPlatforms().stream()
                .map(Platform::getName)
                .collect(Collectors.toList());
        gameDTO.setPlatforms(platforms);
        return gameDTO;
    }
}