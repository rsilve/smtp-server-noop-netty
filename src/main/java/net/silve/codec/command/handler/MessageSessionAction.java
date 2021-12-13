package net.silve.codec.command.handler;

import net.silve.codec.session.MessageSession;

public interface MessageSessionAction {
    void execute(MessageSession session);
}
