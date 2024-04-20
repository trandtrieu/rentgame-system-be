package com.repository;

import com.model.Nick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NickRepository extends JpaRepository<Nick, Long> {

}
