package ru.praktika95.bot;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {
    final int ConnectionTime = 3000;

    public void parsing(Response response) {
        ParsingData parsingData = response.getParsingData();
        DatePeriod date = parsingData.getDatePeriod();

        Map<String,String> query = Map.of(
                "main", parsingData.getCodeCategory()/*"3009"*/,
                "date_from", date.getDateFrom()/*"16.11.2021"*/,
                "date_to", date.getDateTo()/*"16.11.2021"*/,
                "sort", "1",
                "c", "30"
        );

        String site = "https://ekb.kassir.ru/category?";
        Document document;
        try {
            document = Jsoup.connect(site)
                .userAgent("Yandex/21.8.3.614")
                .referrer("https://yandex.ru/")
                .timeout(ConnectionTime)
                .data(query)
                .execute()
                .parse();
        } catch (Exception e) {
            response.setError(true);
            response.setText("Ошибка подключения, попробуйте повторить позже");
            return;
        }

        if (document.equals(new Document(null))){
            response.setError(true);
            response.setText("Ошибка обработки, попробуйте повторить позже");
            return;
        }

        Elements elements = document.select(".content .event-cards-container .event-card .poster a.image");

        List<Event> events = response.getEvents();

        for (Element element : elements){
            events.add(getEvent(element));
        }

        response.setEvents(events);
    }

    private Event getEvent(Element element) {
        String dataString = element.attr("data-ec-item");
        dataString = dataString.substring(1, dataString.length() - 1);

        Pattern pattern = Pattern.compile("\"date\".+},");
        Matcher matcher = pattern.matcher(dataString);

        Event event = new Event();

        if (matcher.find()){
            String dateTime = getDateTime(matcher.group());
            if (dateTime != null){
                String[] dateTimeAr = dateTime.split("\\.");
                event.setDate(dateTimeAr[0]);
                event.setTime(dateTimeAr[1]);
            }
            dataString = String.join("", dataString.split("\"date\".+},"));
        }

        for (int i = 1; i < dataString.length(); i++)
            if (dataString.charAt(i) == ',' && (Pattern.matches("[\\d\"]", dataString.substring(i - 1, i))) ||
                    (i > 3 && dataString.startsWith("null", i - 4)))
                    dataString = dataString.substring(0,i) + "|,|" + dataString.substring(i + 1);

        String[] data = dataString.split("\\|,\\|");

        String minPrice = "";
        String maxPrice = "";

        for (String dataElement : data){
            String[] splitDataElement = dataElement.split("\":");
            String nameDataElement = splitDataElement[0].substring(1);
            String valueDataElement = splitDataElement[1];
            switch (nameDataElement) {
                case "name" -> event.setName(valueDataElement);
                case "image" -> event.setPhoto(valueDataElement.substring(1, valueDataElement.length() - 1));
                case "venueName" -> event.setPlace(valueDataElement);
                case "minPrice" -> minPrice = valueDataElement;
                case "maxPrice" -> maxPrice = valueDataElement;
                case "date" -> {
                    String[] dateTime = valueDataElement.substring(1, valueDataElement.length() - 1).split(" ");
                    event.setDate(dateTime[0]);
                    event.setTime(dateTime[1].substring(0, 5));
                }
            }
        }

        if (Objects.equals(minPrice, maxPrice))
            event.setPrice(minPrice + " руб.");
        else
            event.setPrice(minPrice + " - " + maxPrice + " руб.");
        event.setUrl(element.attr("href"));

        return event;
    }

    private String getDateTime(String date) {
        JSONObject dateJson = new JSONObject(date.substring(7, date.length() - 1));
        String startString = dateJson.get("start_min").toString();
        if (Objects.equals(startString, "null"))
            return null;
        String[] start = startString.split(" ");
        String endString = dateJson.get("end_max").toString();
        String[] end = endString.split(" ");
        String startTime = start[1].substring(0, 5);
        String endTime = end[1].substring(0, 5);
        String time;
        if (startTime.equals(endTime))
            time = start[1].substring(0, 5);
        else
            time = start[1].substring(0, 5) + " - " + end[1].substring(0, 5);
        return start[0] + " - " + end[0] + "." + time;
    }
}
