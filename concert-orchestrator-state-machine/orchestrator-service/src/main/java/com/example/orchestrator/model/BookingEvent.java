package com.example.orchestrator.model;

public enum BookingEvent {
    PROCESS_PAYMENT,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    RESERVE_SEATS,
    RESERVATION_SUCCESS,
    RESERVATION_FAILED
}