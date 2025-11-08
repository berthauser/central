package com.visus.central.application.event;

import java.time.Instant;

public class DomainEvent<T> {
	
	public enum Action {
        CREATED,
        UPDATED,
        DELETED,
        LOGIN_SUCCESS,
        LOGIN_FAILURE,
        CUSTOM // para acciones específicas
    }

	private final Action action;
    private final T payload;
    private final Instant timestamp;

    public DomainEvent(Action action, T payload) {
        this.action = action;
        this.payload = payload;
        this.timestamp = Instant.now();
    }

    public Action getAction() {
        return action;
    }

    public T getPayload() {
        return payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
	
}