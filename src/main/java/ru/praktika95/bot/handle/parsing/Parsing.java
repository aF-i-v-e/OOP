package ru.praktika95.bot.handle.parsing;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.praktika95.bot.handle.SeparatorsConst;
import ru.praktika95.bot.handle.response.DatePeriod;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.response.Response;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {
    public void parsing(Response response) {
        ParsingData parsingData = response.getParsingData();
        DatePeriod date = parsingData.getDatePeriod();

        Map<String,String> query = Map.of(
                "main", parsingData.getCodeCategory(),
                "date_from", date.getDateFrom(),
                "date_to", date.getDateTo(),
                "sort", "1",
                "c", "30"
        );
        Document document;
        try {
            document = Jsoup.connect(ParsingConstants.site)
                .userAgent(ParsingConstants.agent)
                .referrer(ParsingConstants.referrer)
                .timeout(ParsingConstants.connectionTime)
                .data(query)
                .execute()
                .parse();
        } catch (Exception e) {
            response.setError(true);
            response.setText(ParsingConstants.errorConnect);
            return;
        }

        if (document.equals(new Document(""))){
            response.setError(true);
            response.setText(ParsingConstants.errorHandle);
            return;
        }

        Elements elements = document.select(".content .event-card .poster a.image");

        List<Event> events = response.getEvents();

        for (Element element : elements){
            Event event = getEvent(element);
            if (event != null)
                events.add(event);
        }

        response.setEvents(events);
    }

    private Event getEvent(Element element) {
        String dataString = element.attr("data-ec-item");
        dataString = dataString.substring(1, dataString.length() - 1);

        Pattern pattern = Pattern.compile(SeparatorsConst.dateReg);
        Matcher matcher = pattern.matcher(dataString);

        Event event = new Event();

        if (matcher.find()){
            String dateTime = getDateTime(matcher.group());
            if (dateTime != null){
                String[] dateTimeAr = dateTime.split("\\.");
                event.setDate(dateTimeAr[0]);
                event.setTime(dateTimeAr[1]);
            }
            dataString = String.join(SeparatorsConst.nullStr, dataString.split(SeparatorsConst.dateReg));
        }

        for (int i = 1; i < dataString.length(); i++)
            if (dataString.charAt(i) == ',' && (Pattern.matches("[\\d\"]", dataString.substring(i - 1, i))) ||
                    (i > 3 && dataString.startsWith(ParsingConstants.nul, i - 4)))
                    dataString = dataString.substring(0,i) + "|,|" + dataString.substring(i + 1);

        String[] data = dataString.split("\\|,\\|");

        String minPrice = SeparatorsConst.nullStr;
        String maxPrice = SeparatorsConst.nullStr;

        for (String dataElement : data){
            String[] splitDataElement = dataElement.split("\":");
            String nameDataElement = splitDataElement[0].substring(1);
            String valueDataElement = splitDataElement[1];
            switch (nameDataElement) {
                case "name" -> event.setName(valueDataElement.substring(1, valueDataElement.length() - 1));
                case "image" -> event.setPhoto(valueDataElement.substring(1, valueDataElement.length() - 1));
                case "venueName" -> event.setPlace(valueDataElement.substring(1, valueDataElement.length() - 1));
                case "minPrice" -> minPrice = valueDataElement;
                case "maxPrice" -> maxPrice = valueDataElement;
                case "date" -> {
                    String[] dateTime = valueDataElement.substring(1,
                            valueDataElement.length() - 1).split(SeparatorsConst.whitespaces);
                    event.setDate(dateTime[0]);
                    event.setTime(dateTime[1].substring(0, 5));
                }
            }
        }

        if (Objects.equals(minPrice, maxPrice))
            event.setPrice(minPrice +  ParsingConstants.rub);
        else
            event.setPrice(minPrice + SeparatorsConst.dashWithWhitespaces + maxPrice + ParsingConstants.rub);
        event.setUrl(element.attr(ParsingConstants.href));

        if (event.getPhoto() == null || Objects.equals(event.getPhoto(), "null") ||
                event.getName() == null || Objects.equals(event.getName(), "null") ||
                event.getDate() == null || Objects.equals(event.getDate(), "null") ||
                event.getTime() == null || Objects.equals(event.getTime(), "null") ||
                event.getPlace() == null || Objects.equals(event.getPlace(), "null") ||
                event.getPrice() == null || Objects.equals(event.getPrice(), "null") ||
                event.getUrl() == null || Objects.equals(event.getUrl(), "null"))
            return null;
        return event;
    }

    private String getDateTime(String date) {
        JSONObject dateJson = new JSONObject(date.substring(7, date.length() - 1));
        String startString = dateJson.get(ParsingConstants.start_min).toString();
        if (Objects.equals(startString, ParsingConstants.nul))
            return null;
        String[] start = startString.split(SeparatorsConst.whitespaces);
        String endString = dateJson.get(ParsingConstants.end_max).toString();
        String[] end = endString.split(SeparatorsConst.whitespaces);
        String startTime = start[1].substring(0, 5);
        String endTime = end[1].substring(0, 5);
        String time;
        if (startTime.equals(endTime))
            time = start[1].substring(0, 5);
        else
            time = start[1].substring(0, 5) +  SeparatorsConst.dashWithWhitespaces + end[1].substring(0, 5);
        return start[0] + SeparatorsConst.dashWithWhitespaces + end[0] + "." + time;
    }
}
