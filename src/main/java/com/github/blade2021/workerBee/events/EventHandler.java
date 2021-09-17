package com.github.blade2021.workerBee.events;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.service.IEventHandler;

import java.util.function.Consumer;

public class EventHandler implements IEventHandler {
    @Override
    public void publish(Object event) {

    }

    @Override
    public <E> IDisposable onEvent(Class<E> eventClass, Consumer<E> consumer) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
