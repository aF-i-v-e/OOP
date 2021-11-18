package ru.praktika95.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Elements elements = document.select(".content .event-cards-container .event-card");
        List<Event> events = response.getEvents();

        for (Element element : elements) {
            events.add(new Event(
                    element.select("img").attr("data-src"),
                    element.select(".title").text(),
                    element.select(".event-card__caption .date").text(),
                    element.select(".venue").text(),
                    element.select(".cost.rub").text(),
                    element.select(".poster  a").attr("href")
            ));
        }

        response.setEvents(events);
    }
}
