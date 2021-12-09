package ru.praktika95.bot.handle.helpers;

import ru.praktika95.bot.handle.response.DatePeriod;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.chUtils.CommandHandlerConstants;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

import java.util.Calendar;

import static ru.praktika95.bot.handle.format.FormatDateCalendar.formatDate;

public class CreateDate {
    public static void date(Response response, String typeButtons){
        SetDataForResponse.setMessageAndButtons(TimeConstants.chooseDate, response, typeButtons, null, true);
    }

    public static void today(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        String currentDate = formatDate(calendar);
        SetDataForResponse.setMessageAndButtons(CommandHandlerConstants.dateText + TimeConstants.today,
                createDatePeriod(response, currentDate, currentDate), typeButtons, null, true);
    }

    public static void tomorrow(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        String tomorrow = formatDate(calendar);
        SetDataForResponse.setMessageAndButtons(CommandHandlerConstants.dateText + TimeConstants.tomorrow,
                createDatePeriod(response, tomorrow, tomorrow), typeButtons, null, true);
    }

    public static void thisWeek(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String currentDate = formatDate(calendar);
        String dateTo;
        if (dateWeek == 1)
            dateTo = currentDate;
        else {
            calendar.add(Calendar.DATE, 8 - dateWeek);
            dateTo = formatDate(calendar);
        }
        SetDataForResponse.setMessageAndButtons(CommandHandlerConstants.dateText + TimeConstants.thisWeek,
                createDatePeriod(response, currentDate, dateTo), typeButtons, null, true);
    }

    public static void nextWeek(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dateWeek == 1)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, 9 - dateWeek);
        String dateFrom = formatDate(calendar);
        calendar.add(Calendar.DATE, 6);
        String dateTo = formatDate(calendar);
        SetDataForResponse.setMessageAndButtons(CommandHandlerConstants.dateText + TimeConstants.nextWeek,
                createDatePeriod(response, dateFrom, dateTo), typeButtons, null, true);
    }

    public static void thisMonth(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateMonth = calendar.get(Calendar.DATE);
        String currentDate = formatDate(calendar);
        int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        String dateTo;
        if (dateMonth == lastDayMonth)
            dateTo = currentDate;
        else {
            calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
            dateTo = formatDate(calendar);
        }
        SetDataForResponse.setMessageAndButtons(CommandHandlerConstants.dateText + TimeConstants.thisMonth,
                createDatePeriod(response, currentDate, dateTo), typeButtons, null, true);
    }

    public static void nextMonth(Response response, String typeButtons) {
        Calendar calendar = Calendar.getInstance();
        int dateMonth = calendar.get(Calendar.DATE);
        int lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        if (dateMonth == lastDayMonth)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, lastDayMonth - dateMonth + 1);
        String dateFrom = formatDate(calendar);
        lastDayMonth = calendar.getActualMaximum(Calendar.DATE);
        dateMonth = calendar.get(Calendar.DATE);
        calendar.add(Calendar.DATE, lastDayMonth - dateMonth);
        String dateTo = formatDate(calendar);
        SetDataForResponse.setMessageAndButtons( CommandHandlerConstants.dateText + TimeConstants.nextMonth,
                createDatePeriod(response, dateFrom, dateTo), typeButtons, null, true);
    }

    public static Response createDatePeriod(Response response, String dateFrom, String dateTo){
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setDateFrom(dateFrom);
        datePeriod.setDateTo(dateTo);
        response.setPeriod(datePeriod);
        return response;
    }
}
