package com.ghd.kg.ghd.telegram;

import com.ghd.kg.ghd.common.BookingStatus;
import com.ghd.kg.ghd.db.entity.Bookings;
import com.ghd.kg.ghd.db.entity.TgChats;
import com.ghd.kg.ghd.db.repo.BookingRepo;
import com.ghd.kg.ghd.db.repo.TgChatsRepo;
import com.ghd.kg.ghd.service.interfaces.ITgChatsService;
import com.ghd.kg.ghd.utils.DateTimeUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ghd.kg.ghd.telegram.Constants.*;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BookingBot extends TelegramLongPollingBot implements BotCommands {
    final TgChatsRepo tgChatsRepo;
    final ITgChatsService tgChatsService;
    final BookingRepo bookingRepo;
    @Value("${telegram.bot.name}")
    String botName;
    @Value("${telegram.bot.token}")
    String botToken;

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        //если получено сообщение текстом
        if (update.hasMessage()) {
            Chat chat = update.getMessage().getChat();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chat, userName, update);
            }

            //если нажата одна из кнопок бота
        } else if (update.hasCallbackQuery()) {
            Chat chat = update.getCallbackQuery().getMessage().getChat();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();
            update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chat, userName, update);
        }
    }

    public void sendNewBooking(Bookings bookings) {
        SendMessage message = new SendMessage();
        TgChats tgChat = tgChatsRepo.getMainChat();
        message.setChatId(tgChat.getId());

        String arrival = DateTimeUtil.formatDateTime(
                bookings.getArrivalDate(),
                "ru",
                FormatStyle.FULL);
        String departure = DateTimeUtil.formatDateTime(
                bookings.getDepartureDate(),
                "ru",
                FormatStyle.FULL);

        long days = bookings.getArrivalDate().until(bookings.getDepartureDate(), ChronoUnit.DAYS);

        String template = """
                <b><i>Отправитель:</i></b>  %s\n
                <b><i>Дата въезда:</i></b>  %s\n
                <b><i>Дата выезда:</i></b>  %s\n
                <b><i>Кол-во дней:</i></b>  %s\n
                <b><i>Количество человек:</i></b>  %s\n
                <b><i>Контакты:</i></b>  %s
                """;
        String textToSend = String.format(
                template,
                bookings.getApplicantName(),
                arrival,
                departure,
                days,
                bookings.getAdultsCount(),
                bookings.getPhones()
        );


        message.setText(textToSend);
        message.setParseMode(ParseMode.HTML);
        message.setReplyMarkup(applicationActionsMarkup(bookings.getId()));

        try {
            execute(message);
            log.info("Booking sent -> id:" + bookings.getId() + ", phone:" + bookings.getPhones());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void startBot(Chat chat, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText("Здравствуй, " + userName + "! Бот активирован!\n\nСделать этот чат основным для приёма заявок?");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да!");
        yesButton.setCallbackData(SET_CHAT);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Нет, спасибо...");
        noButton.setCallbackData(NOT_SET_CHAT);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
            log.info("Bot started...");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private InlineKeyboardMarkup applicationActionsMarkup(Long applicationId) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton progressButton = new InlineKeyboardButton();
        progressButton.setText("Принять заявку [" + applicationId + "]");
        progressButton.setCallbackData(PROGRESS_APPLICATION);

        rowInLine.add(progressButton);
        rowsInLine.add(rowInLine);

        markup.setKeyboard(rowsInLine);
        return markup;
    }

    private void botAnswerUtils(String receivedMessage, Chat chat, String userName, Update update) {
        switch (receivedMessage) {
            case START_BOT -> startBot(chat, userName);
            case HELP -> sendHelpText(chat, HELP_TEXT);
            case SET_CHAT -> setChatMain(chat, MAIN_CHAT_YES_TEXT);
            case NOT_SET_CHAT -> ignoreChatMain(chat);
            case PROGRESS_APPLICATION -> processApplication(update);
            default -> {
            }
        }
    }

    private void sendHelpText(Chat chat, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Help text sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void setChatMain(Chat chat, String text) {
        Optional<TgChats> existing = tgChatsRepo.findById(chat.getId());
        TgChats dbChat;

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        if (existing.isEmpty()) {
            TgChats tgChat = new TgChats();
            tgChat.setId(chat.getId());
            tgChat.setFirstName(chat.getFirstName());
            tgChat.setLastName(chat.getLastName());
            tgChat.setUsername(chat.getUserName());
            tgChat.setTitle(chat.getTitle());
            tgChat.setDescription(chat.getDescription());
            tgChat.setType(chat.getType());
            tgChat.setMain(true);
            dbChat = tgChatsRepo.save(tgChat);

            tgChatsService.setChatsAsNotMain(chat.getId());

            String textToSend = String.format(text, dbChat.getTitle());
            message.setText(textToSend);
        } else {
            dbChat = existing.get();
            if (dbChat.isMain()) {
                message.setText("Чат и так является основным)");
            } else {
                tgChatsService.setChatAsMain(chat.getId());
                tgChatsService.setChatsAsNotMain(chat.getId());
                String textToSend = String.format(text, dbChat.getTitle());
                message.setText(textToSend);
            }
        }
        try {
            execute(message);
            log.info("Chat set message sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void ignoreChatMain(Chat chat) {
    }

    private void processApplication(Update update) {
        CallbackQuery callback = update.getCallbackQuery();

        String data = callback.getMessage().getReplyMarkup().getKeyboard().get(0).get(0).getText();
        String charId = String.valueOf(data.charAt(data.indexOf("[") + 1));
        Long id = Long.valueOf(charId);
        Bookings toUpdate = bookingRepo.findById(id).get();
        toUpdate.setState(BookingStatus.IN_PROGRESS);
        bookingRepo.save(toUpdate);

        EditMessageReplyMarkup replyMarkup = new EditMessageReplyMarkup();
        replyMarkup.setChatId(update.getCallbackQuery().getMessage().getChatId());
        replyMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        InlineKeyboardButton progressButton = new InlineKeyboardButton();
        progressButton.setText("Заявка принята!");
        progressButton.setCallbackData("stub");

        rowInLine.add(progressButton);
        rowsInLine.add(rowInLine);

        markup.setKeyboard(rowsInLine);
        replyMarkup.setReplyMarkup(markup);

        try {
            execute(replyMarkup);
            log.info("Application accepted: " + id);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
