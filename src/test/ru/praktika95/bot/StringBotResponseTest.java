package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class StringBotResponseTest {
    private CommandHandler commandHandler;
    private BotResponse botResponse;
    private BotRequest botRequest;
    private final String dateText = "Выберите категорию мероприятия, которое состоится";

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        botResponse = new BotResponse();
    }

    private void compareBasicCommandWork(String basicCommand, String correctAnswer){
        commandHandler.commandHandler(basicCommand, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectHelpMessage() {
        compareBasicCommandWork("help", "О работе с данным ботом:\n" +
                "Для того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\n" +
                "После, Вам на выбор будет представлено 6 мероприятий.\nКогда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\n" +
                "При подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.");
    }

    @Test
    void testCorrectStartMessage() {
        compareBasicCommandWork("start", "Привет!\nЯ бот, которые может показать ближайшие мероприятия. " +
                "Вы можете подписаться на их уведомление и вы точно про него не забудете.\n" +
                "Для того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"\n" +
                "Для того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку \"Мероприятия\".");
    }

    @Test
    void testIncorrectMessage() {
        compareBasicCommandWork("read", "Введённой команды не существует, вы можете выполнить команду " +
                "/start, чтобы начать работу с ботом.");
    }

    private void setBotRequest(String typeButtons, String botCommand){
        Update update = createTestUpdate();
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }

    private Message createTestMessage(){
        Message message = new Message();
        message.setMessageId(689);
        Chat chat = new Chat(454652745l, "private", null, "UserFirstName", "UserLastName", "userName", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        return message;
    }

    private CallbackQuery createTestCallBackQuery(){
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId("1962727177397599840");
        Message message = createTestMessage();
        callbackQuery.setMessage(message);
        return callbackQuery;
    }

    private Update createTestUpdate(){
        CallbackQuery callbackQuery = createTestCallBackQuery();
        Update update = new Update(55632457, null, null, null, callbackQuery, null, null, null, null, null, null, null, null, null);
        return update;
    }

    private void compareStringBotResponse(String correctAnswer){
        commandHandler.commandHandler(botRequest, botResponse);
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        compareStringBotResponse("Выберите дату");
    }

    @Test
    void testCorrectHelpCommand() {
        setBotRequest("main", "help");
        String correctAns = "О работе с данным ботом:\n" +
                "Для того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\n" +
                "После, Вам на выбор будет представлено 6 мероприятий.\n" +
                "Когда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\n" +
                "При подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.";
        compareStringBotResponse(correctAns);
    }

    @Test
    void testCorrectTodayCommand() {
        setBotRequest("date", "today");
        compareStringBotResponse(dateText + " сегодня:");
    }

    @Test
    void testCorrectTomorrowCommand() {
        setBotRequest("date", "tomorrow");
        compareStringBotResponse( dateText + " завтра:");
    }

    @Test
    void testCorrectThisWeekCommand() {
        setBotRequest("date", "thisWeek");
        compareStringBotResponse(dateText + " на этой неделе:");
    }

    @Test
    void testCorrectNextWeekCommand() {
        setBotRequest("date", "nextWeek");
        compareStringBotResponse(dateText + " на следующей неделе:");
    }

    @Test
    void testCorrectThisMonthCommand() {
        setBotRequest("date", "thisMonth");
        compareStringBotResponse(dateText + " в этом месяце:");
    }

    @Test
    void testCorrectNextMonthCommand() {
        setBotRequest("date", "nextMonth");
        compareStringBotResponse(dateText + " в следующем месяце:");
    }
}
