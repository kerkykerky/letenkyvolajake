package com.letenky.letenky.repository;

import com.letenky.letenky.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByOdletAndPríletAndDátum(String odlet, String prílet, String dátum);

    List<Ticket> findByOdletAndPríletAndDátumAndCenaLessThan(String odlet, String prílet, String dátum, Double cena);
}
