package com.example.orchestrator.machine;

public class RetryPolicy {
    private final int maxAttempts;
    private final long delayMs;
    
    public RetryPolicy(int maxAttempts, long delayMs) {
        this.maxAttempts = maxAttempts;
        this.delayMs = delayMs;
    }
    
    public int getMaxAttempts() { return maxAttempts; }
    public long getDelayMs() { return delayMs; }
}