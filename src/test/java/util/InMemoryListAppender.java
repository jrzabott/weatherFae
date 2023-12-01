package util;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.ErrorHandler;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.DefaultErrorHandler;
import org.apache.logging.log4j.message.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class InMemoryListAppender implements Appender {

    private final String name;
    private Collection<LogEvent> logEvents;
    private boolean isStarted;
    private ErrorHandler errorHandler = new DefaultErrorHandler(this);

    public InMemoryListAppender(String name) {
        this.name = name;
    }

    @Override
    public void append(LogEvent event) {
        if (logEvents == null)
            throw new IllegalStateException(this.getClass().getSimpleName() + " - " + this.getName() + " - not initialized");

        logEvents.add(event);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Layout<? extends Serializable> getLayout() {
        return null;
    }

    @Override
    public boolean ignoreExceptions() {
        return true;
    }

    @Override
    public ErrorHandler getHandler() {
        return errorHandler;
    }

    @Override
    public void setHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    @Override
    public State getState() {
        return isStarted ? State.STARTED : State.STOPPED;
    }

    @Override
    public void initialize() {
        start();
    }

    @Override
    public void start() {
        logEvents = new ArrayList<>();
        this.isStarted = true;
    }

    @Override
    public void stop() {
        this.isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return this.isStarted;
    }

    @Override
    public boolean isStopped() {
        return !this.isStarted;
    }

    public Collection<String> getLogEventsMessagesAsText() {
        return logEvents.stream()
                .map(LogEvent::getMessage)
                .map(Message::getFormattedMessage)
                .collect(Collectors.toList());
    }
}
