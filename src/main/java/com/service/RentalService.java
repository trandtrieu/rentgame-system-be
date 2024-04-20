package com.service;

import com.dto.RentalDTO;
import com.model.Game;
import com.model.Nick;
import com.model.Rental;
import com.repository.GameRepository;
import com.repository.NickRepository;
import com.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private NickRepository nickRepository;

    @Autowired
    private GameRepository gameRepository;


    public void rentAccount(long userId, long gameId) {
        Optional<Nick> optionalNick = nickRepository.findById(userId);
        Optional<Game> optionalGame = gameRepository.findById(gameId);

        if (optionalNick.isPresent() && optionalGame.isPresent()) {
            Nick user = optionalNick.get();
            Game game = optionalGame.get();

            if (game.getStock() > 0) {
                String username = user.getUsername();
                String password = user.getPassword();

                Rental rental = new Rental();
                rental.setNick(user);
                rental.setGame(game);
                rental.setRentalDate(LocalDate.now());

                game.setStock(game.getStock() - 1);
                gameRepository.save(game);

                rentalRepository.save(rental);

                System.out.println("Game rented successfully!");

                launchSteam(username, password);

            } else {
                throw new RuntimeException("Game is out of stock");
            }
        } else {
            throw new RuntimeException("User or game not found");
        }
    }


    private void launchSteam(String username, String password) {
        try {
            // Execute command to change directory
            System.out.println("Launching Steam...");

            ProcessBuilder cdProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "cd", "\"C:\\Program Files (x86)\\Steam\"");
            Process cdProcess = cdProcessBuilder.start();
            cdProcess.waitFor();

            // Check if changing directory was successful
            if (cdProcess.exitValue() != 0) {
                throw new RuntimeException("Failed to change directory to Steam installation folder");
            }
            System.out.println("Logging in to Steam...");
            System.out.println("Renting game for user: " + username + ", password: " + password);

            ProcessBuilder steamProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "\"C:\\Program Files (x86)\\Steam\\Steam.exe\"", "-login", username, password);
            Process steamProcess = steamProcessBuilder.start();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute commands: " + e.getMessage());
        }
    }


    public void returnAccount(long rentalId) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setReturnDate(LocalDate.now());
            rentalRepository.save(rental);

            Game game = rental.getGame();
            game.setStock(game.getStock() + 1);
            gameRepository.save(game);
        } else {
            throw new RuntimeException("Rental not found");
        }

    }

    public List<RentalDTO> getRentedAccountsByUser(long userId) {
        List<Rental> rentals = rentalRepository.findByNickId(userId);
        return rentals.stream()
                .map(this::convertToRentalDTO)
                .collect(Collectors.toList());
    }

    private RentalDTO convertToRentalDTO(Rental rental) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setGameId(rental.getGame().getId());
        rentalDTO.setGameName(rental.getGame().getName());
        rentalDTO.setRentalDate(rental.getRentalDate());
        rentalDTO.setReturnDate(rental.getReturnDate());
        rentalDTO.setStatus(rental.getStatus());


        return rentalDTO;
    }

}
