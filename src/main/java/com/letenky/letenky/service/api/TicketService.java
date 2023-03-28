package com.letenky.letenky.service.api;

import com.letenky.letenky.model.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> search(String origin, String destination, String date);

    Ticket getTicketById(Long id);
}
