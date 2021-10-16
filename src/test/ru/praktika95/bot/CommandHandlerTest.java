package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandHandlerTest {
    private CommandHandler commandHandler;
    private BotResponse botResponse;
    private final String dateText = "Выберите категорию мероприятия, которое состоится";

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        botResponse = new BotResponse();
    }

    private void comparatorExtendedCommand(String typeButtons, String botCommand, String correctAnswer){
        commandHandler.commandHandler(typeButtons, botCommand, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    private void comparatorBasicCommand(String basicCommand, String correctAnswer){
        commandHandler.commandHandler(basicCommand, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectHelpMessage() {
        comparatorBasicCommand("help", "О работе с данным ботом:\n" +
                "Для того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\n" +
                "После, Вам на выбор будет представлено 6 мероприятий.\nКогда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\n" +
                "При подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
    }

    @Test
    void testCorrectStartMessage() {
        comparatorBasicCommand("start", "Привет!\nЯ бот, которые может показать ближайшие мероприятия. " +
                "Вы можете подписаться на их уведомление и вы точно про него не забудете.\n" +
                "Для того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"\n" +
                "Для того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку \"Мероприятия\".");

    }

    @Test
    void testIncorrectMessage() {
        comparatorBasicCommand("read", "Введённой команды не существует, вы можете выполнить команду " +
                "/start, чтобы начать работу с ботом.");
    }

    @Test
    void testCorrectShowCommand() {
        comparatorExtendedCommand("main", "show", "Выберите дату");
    }

    @Test
    void testCorrectHelpCommand() {
        comparatorExtendedCommand("main", "help", "О работе с данным ботом:\n" +
                "Для того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\n" +
                "После, Вам на выбор будет представлено 6 мероприятий.\n" +
                "Когда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\n" +
                "При подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
    }

    @Test
    void testCorrectTodayCommand() {
        comparatorExtendedCommand("date", "today", dateText + " сегодня:");
    }

    @Test
    void testCorrectTomorrowCommand() {
        comparatorExtendedCommand("date", "tomorrow", dateText + " завтра:");
    }

    @Test
    void testCorrectThisWeekCommand() {
        comparatorExtendedCommand("date", "thisWeek", dateText + " на этой неделе:");
    }

    @Test
    void testCorrectNextWeekCommand() {
        comparatorExtendedCommand("date", "nextWeek", dateText + " на слудующей неделе:");
    }

    @Test
    void testCorrectThisMonthCommand() {
        comparatorExtendedCommand("date", "thisMonth", dateText + "в этом месяце:");
    }

    @Test
    void testCorrectNextMonthCommand() {
        comparatorExtendedCommand("date", "nextMonth", dateText + " в следующем месяце:");
    }

    @Test
    void testCorrectTheatreCommand() {
        comparatorExtendedCommand("category", "theatre", "");
    }

    @Test
    void testCorrectMuseumCommand() {
        comparatorExtendedCommand("category", "museum", "");
    }

    @Test
    void testCorrectConcertCommand() {
        comparatorExtendedCommand("category", "concert", "");
    }

    @Test
    void testCorrectAllEventsCommand() {
        comparatorExtendedCommand("category", "allEvents", "");
    }





}
