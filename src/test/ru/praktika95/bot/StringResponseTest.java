package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Response;

class StringResponseTest {
    private CommandHandler commandHandler;
    private Response response;
    private BotRequest botRequest;
    private final String dateText = "Выберите категорию мероприятия, которое состоится";

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        response = new Response();
    }

    private void compareBasicCommandWork(String basicCommand, String correctAnswer){
        commandHandler.commandHandler(basicCommand, response);
        assertEquals(correctAnswer,response.getText());
    }

    @Test
    void testCorrectHelpMessage() {
        compareBasicCommandWork("help", "О работе с данным ботом:" +
                "\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, " +
                "используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 3 мероприятия." +
                "\nКогда Вы выберете конкретное мероприятие, Вы сможете сразу приобрести билеты, либо " +
                "поставить на него уведомление." +
                "\nДоступно оповещение о событии за день, за неделю, либо за оба периода сразу." +
                "\nДля отмены уведомления Вам нужно найти на главном меню кнопку \"Мои мероприятия\" " +
                "и в появившемся списке выбрать мероприятие, оповещение на которое нужно убрать." +
                "\nДля полной отмены уведомления на событие, нужно исключить все записи о данном мероприятии из списка \"Мои мероприятия\".");
    }

    @Test
    void testCorrectStartMessage() {
        compareBasicCommandWork("start", "Привет!\nЯ бот, которые может показать ближайшие мероприятия. " +
                "Вы можете поставить уведомление о выбранном событии, и я оповещу Вас о нём!" +
                "\nДля того, чтобы посмотреть доступные мероприятия, выбрать подходящее время используйте кнопку" +
                " \"Выбрать\"."
                + "\nДля того, чтобы узнать больше о работе с данным ботом используйте кнопку \"Помощь\"."
                + "\nДля того, чтобы посмотреть все выбранные Вами мероприятия используйте кнопку \"Мои мероприятия\".");
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

    private Update createTestUpdate(){
        Message message = createTestMessage();
        Update update = new Update(55632457, message, null, null, null, null, null, null, null, null, null, null, null, null);
        return update;
    }

    private void compareStringBotResponse(String correctAnswer){
        commandHandler.commandHandler(botRequest, response);
        assertEquals(correctAnswer, response.getText());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        compareStringBotResponse("Выберите дату");
    }

    @Test
    void testCorrectHelpCommand() {
        setBotRequest("main", "help");
        String correctAns = "О работе с данным ботом:" +
        "\nДля того, чтобы выбрать категорию мероприятия и подходящий период времени, " +
                "используйте соответствующие кнопки.\nПосле, Вам на выбор будет представлено 3 мероприятия." +
                "\nКогда Вы выберете конкретное мероприятие, Вы сможете сразу приобрести билеты, либо " +
                "поставить на него уведомление." +
                "\nДоступно оповещение о событии за день, за неделю, либо за оба периода сразу." +
                "\nДля отмены уведомления Вам нужно найти на главном меню кнопку \"Мои мероприятия\" " +
                "и в появившемся списке выбрать мероприятие, оповещение на которое нужно убрать." +
                "\nДля полной отмены уведомления на событие, нужно исключить все записи о данном мероприятии из списка \"Мои мероприятия\".";
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
