package net.silve.codec.session;

import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageSessionTest {

    @Test
    void shouldCreateInstance() {
        MessageSession session = MessageSession.newInstance();
        assertNotNull(session);
        assertNotNull(session.getId());
    }

    @Test
    void shouldHaveAReversePathMethod() {
        MessageSession session = MessageSession.newInstance().setReversePath();
        assertTrue(session.isTransactionStarted());
    }

    @Test
    void shouldHaveAForwardPathMethod() {
        MessageSession session = MessageSession.newInstance();
        assertTrue(session.needForward());
        session.addForwardPath(AsciiString.of("eee"));
        assertFalse(session.needForward());
        assertFalse(session.tooManyForward(2));
        session.addForwardPath(AsciiString.of("eee"));
        assertFalse(session.needForward());
        assertTrue(session.tooManyForward(2));
    }

    @Test
    void shouldHaveACompletedMethod() {
        MessageSession session = MessageSession.newInstance().completed();
        assertTrue(session.duration() > 0);
    }

    @Test
    void shouldHaveARecycleMethode() {
        MessageSession session = MessageSession.newInstance()
                .setReversePath()
                .addForwardPath(AsciiString.of("eee"))
                .completed();
        assertTrue(session.isTransactionStarted());
        assertFalse(session.needForward());
        assertTrue(session.duration() > 0);
        session.recycle();
        assertFalse(session.isTransactionStarted());
        assertTrue(session.needForward());
        assertFalse(session.duration() > 0);
    }


}