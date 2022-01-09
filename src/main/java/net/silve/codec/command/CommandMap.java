package net.silve.codec.command;

import net.silve.codec.command.handler.CommandHandler;
import net.silve.codec.command.parsers.CommandParser;

import java.util.HashMap;
import java.util.Map;

public class CommandMap {

    private static final CommandMap instance = new CommandMap();
    private final Map<CharSequence, CommandHandler> handlerMap = new HashMap<>();
    private final Map<CharSequence, CommandParser> parserMap = new HashMap<>();
    private CommandMap() {
        for (CommandRegistry registry : CommandRegistry.values()) {
            final CommandHandler handler = registry.getHandler();
            this.handlerMap.put(handler.getName(), handler);
            final CommandParser parser = registry.getParser();
            if (parser != null) {
                this.parserMap.put(parser.getName(), parser);
            }

        }
    }

    public static CommandMap getInstance() {
        return instance;
    }

    public CommandHandler getHandler(CharSequence name) {
        return handlerMap.get(name);
    }

    public CommandParser getParser(CharSequence name) {
        return parserMap.get(name);
    }
}
