package ru.praktika95.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class Parsing {
    public void parsing(BotResponse botResponse) {
        System.out.println("!");
        ParsingData parsingData = botResponse.getParsingData();
        DatePeriod date = parsingData.getDatePeriod();
        Map<String,String> query = new HashMap<>() {{
            put("main", parsingData.getCodeCategory()/*"4093"*/);
            put("date_from", date.getDateFrom()/*"09.10.2021"*/);
            put("date_to", date.getDateTo()/*"30.10.2021"*/);
            put("sort", "1");
            put("c", "30");
        }};
        String site = "https://ekb.kassir.ru/category?";
        Document document;
        try {
            document = Jsoup.connect(site)
                .userAgent("Yandex/21.8.3.614")
                .referrer("https://yandex.ru/")
                .timeout(3000)
                .data(query)
                .execute()
                .parse();
        }
        catch (Exception e) {
            botResponse.setError(true);
            botResponse.setMessage("Ошибка подключения, попробуйте повторить позже");
            return;
        }
        if (document.equals(new Document(null))){
            botResponse.setError(true);
            botResponse.setMessage("Ошибка обработки, попробуйте повторить позже");
            return;
        }
        Elements elements = document.select(".events .col-xs-2 .event");
        int count = elements.size();
        Event[] events = new Event[count];
        for (int i = 0; i < count; i++){
            Element element = elements.get(i);
            Elements div = element.select(".caption");
            events[i] = new Event(
                    element.select("img").attr("data-src"),
                    div.select(".title").text(),
                    div.select(".date").text(),
                    div.select(".place").text(),
                    div.select(".cost.rub").text(),
                    div.select(".buy.hover a").attr("href")
            );
        }
        botResponse.setEvents(events);
    }
}
