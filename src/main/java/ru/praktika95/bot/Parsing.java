package ru.praktika95.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class Parsing {
    public void parsing(BotResponse botResponse) {
        ParsingData parsingData = botResponse.getParsingData();
        DatePeriod date = parsingData.getDatePeriod();
        Map<String,String> query = new HashMap<>() {{
            put("main", /*parsingData.getCodeCategory()*/"0");
            put("date_from", /*date.getDateFrom()*/"01.10.2021");
            put("date_to", /*date.getDateTo()*/"02.10.2021");
            put("sort", "1");
            put("c", "30");
        }};
        String site = "https://ekb.kassir.ru/category?";
        Document document = null;
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
            botResponse.setSendMessage("Ошибка подключения, попробуйте повторить позже");
            return;
        }
        if (document == null){
            botResponse.setError(true);
            botResponse.setSendMessage("Ошибка обработки, попробуйте повторить позже");
            return;
        }
        Elements elements = document.select(".events .col-xs-2 .event");
        Event[] events = new Event[30];
        for (int i = 0; i < 30; i++){
            Element element = elements.get(i);
            Elements div = element.select(".caption");
            events[i] = new Event(
                    element.select("img").attr("data-src"),
                    div.select(".title").text(),
                    div.select(".date").text(),
                    div.select(".place").text(),
                    div.select(".cost.rub").text()
            );
        }
        botResponse.setEvents(events);
//        System.out.println(events[2].getPrice());
    }
}
