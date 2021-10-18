package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

class CommandHandlerTest {
    private CommandHandler commandHandler;
    private BotResponse botResponse;
    private BotRequest botRequest;
    private final String dateText = "Выберите категорию мероприятия, которое состоится";

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        botResponse = new BotResponse();
    }

    private void setBotRequest(String typeButtons, String botCommand){
        Update update = new Update();
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }

    private void comparatorExtendedCommand(String correctAnswer){
        commandHandler.commandHandler(botRequest, botResponse);
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
        setBotRequest("main", "show");
        comparatorExtendedCommand("Выберите дату");
    }

    @Test
    void testCorrectHelpCommand() {

        setBotRequest("main", "help");
        String correctAns = "О работе с данным ботом:\n" +
                "Для того, чтобы выбрать категорию мероприятия и подходящий период времени, используйте соответствующие кнопки.\n" +
                "После, Вам на выбор будет представлено 6 мероприятий.\n" +
                "Когда Вы выберете конкретное мероприятие, Вы сможете либо подписаться на мероприятие, либо сразу приобрести билеты.\n" +
                "При подписке на мероприятие, бот уведомит Вас о выбранном событии за определенный период времени.";
        comparatorExtendedCommand(correctAns);
    }

    @Test
    void testCorrectTodayCommand() {
        setBotRequest("date", "today");
        comparatorExtendedCommand(dateText + " сегодня:");
    }

    @Test
    void testCorrectTomorrowCommand() {
        setBotRequest("date", "tomorrow");
        comparatorExtendedCommand( dateText + " завтра:");
    }

    @Test
    void testCorrectThisWeekCommand() {
        setBotRequest("date", "thisWeek");
        comparatorExtendedCommand(dateText + " на этой неделе:");
    }

    @Test
    void testCorrectNextWeekCommand() {
        setBotRequest("date", "nextWeek");
        comparatorExtendedCommand(dateText + " на слудующей неделе:");
    }

    @Test
    void testCorrectThisMonthCommand() {
        setBotRequest("date", "thisMonth");
        comparatorExtendedCommand(dateText + "в этом месяце:");
    }

    @Test
    void testCorrectNextMonthCommand() {
        setBotRequest("date", "nextMonth");
        comparatorExtendedCommand(dateText + " в следующем месяце:");
    }

    @Test
    void testCorrectTheatreCommand() {
        setBotRequest("category", "theatre");
        comparatorExtendedCommand( "");
    }

    @Test
    void testCorrectMuseumCommand() {
        setBotRequest("category", "museum");
        comparatorExtendedCommand("");
    }

    @Test
    void testCorrectConcertCommand() {
        setBotRequest("category", "concert");
        comparatorExtendedCommand("");
    }

    @Test
    void testCorrectAllEventsCommand() {
        setBotRequest("category", "allEvents");
        comparatorExtendedCommand("");
    }

//    @Test
//    void test(){
//        Update update = creator("/start");
//        botRequest.setTypeButtons("");
//        botRequest.setBotCommand();
//        commandHandler.commandHandler(botRequest, botResponse);
//    }

    private Update createUpdate(String text){
        Chat chat = new Chat(123456789l, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText(text);

        Update update = new Update();
        update.setMessage(message);
        return update;
    }

}
