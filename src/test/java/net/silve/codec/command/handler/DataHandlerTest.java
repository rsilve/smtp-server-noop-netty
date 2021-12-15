package net.silve.codec.command.handler;

import io.netty.handler.codec.smtp.SmtpCommand;
import io.netty.util.AsciiString;
import net.silve.codec.request.RecyclableSmtpRequest;
import net.silve.codec.response.ConstantResponse;
import net.silve.codec.session.MessageSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DataHandlerTest {

    @Test
    void shouldHaveName() {
        CharSequence name = DataHandler.singleton().getName();
        assertEquals(SmtpCommand.DATA.name(), name);
    }

    @Test
    void shouldReturnResponse() {
        try {
            DataHandler.singleton()
                    .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA), MessageSession.newInstance());
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_SENDER_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionIfTransactionAlreadStarted() {
        try {
            DataHandler.singleton()
                    .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA),
                            MessageSession.newInstance().setReversePath(AsciiString.of("email")));
            fail();
        } catch (InvalidProtocolException e) {
            assertEquals(ConstantResponse.RESPONSE_RECIPIENT_NEEDED, e.getResponse());
        }
    }

    @Test
    void shouldThrowExceptionRequestParameterIsEmpty() throws InvalidProtocolException {

        HandlerResult handle = DataHandler.singleton()
                .handle(RecyclableSmtpRequest.newInstance(SmtpCommand.DATA),
                        MessageSession.newInstance().setReversePath(AsciiString.of("email")).addForwardPath(AsciiString.of("ee"))
                );
        assertEquals(ConstantResponse.RESPONSE_END_DATA_MESSAGE, handle.getResponse());

    }

}