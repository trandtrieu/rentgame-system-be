package com.repository;

import com.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByNickId(long userId);

    List<Rental> findByAccount_Id(long accountId);

}