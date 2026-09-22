package com.example.orchestrator.model;

import lombok.Data;

@Data
public class BookingTransaction {
    private String bookingId;
    private String concertCode;
    private String customerId;
    private String customerEmail;
    private int ticketQuantity;
    private double amount;
    private BookingState currentState = BookingState.INITIATED;
}