package com.letenky.letenky.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letenky.letenky.model.Ticket;
import com.letenky.letenky.repository.TicketRepository;
import com.letenky.letenky.service.api.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Value("${skyscanner.api.key}")
    private String apiKey;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> search(String origin, String destination, String date) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("User-Agent", "Mozilla/5.0");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/browsequotes/v1.0/" +
                "US/USD/en-US/" + origin + "/" + destination + "/" + date;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode quotes = root.path("Quotes");

                List<Ticket> tickets = new ArrayList<>();

                for (JsonNode quote : quotes) {
                    Ticket ticket = new Ticket();
                    ticket.setPrice(Double.valueOf(quote.path("MinPrice").asInt()));
                    ticket.setDepartureDate(quote.path("OutboundLeg").path("DepartureDate").asText());
                    ticket.setArrivalDate(quote.path("InboundLeg").path("DepartureDate").asText());
                    ticket.setOrigin(quote.path("OutboundLeg").path("OriginId").asText());
                    ticket.setDestination(quote.path("OutboundLeg").path("DestinationId").asText());

                    tickets.add(ticket);
                }

                return tickets;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket ticket = getTicketById(id);
        ticket.setOrigin(updatedTicket.getOrigin());
        ticket.setDestination(updatedTicket.getDestination());
        ticket.setPrice(updatedTicket.getPrice());
        ticket.setDepartureDate(updatedTicket.getDepartureDate());
        ticket.setArrivalDate(updatedTicket.getArrivalDate());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }

    public List<Ticket> searchTickets(String from, String to, String date, Double price) {
        if (price != null) {
            return ticketRepository.findByOdletAndPríletAndDátumAndCenaLessThan(from, to, date, price);
        } else {
            return ticketRepository.findByOdletAndPríletAndDátum(from, to, date);
        }
    }
}
