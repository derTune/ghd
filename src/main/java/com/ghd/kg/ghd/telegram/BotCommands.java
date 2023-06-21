package com.ghd.kg.ghd.telegram;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "Start bot"),
            new BotCommand("/help", "Bot info")
    );

    String HELP_TEXT = "Hello, Dastan!";
    String MAIN_CHAT_YES_TEXT = "Чат %s установлен как основной.";
}
