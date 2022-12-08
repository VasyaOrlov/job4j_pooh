package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * класс TopicService реализуер режим "topic"
 * Отправитель посылает запрос на добавление данных с указанием топика (weather) и значением параметра (temperature=18).
 * Сообщение помещается в конец каждой индивидуальной очереди получателей.
 * Если топика нет в сервисе, то данные игнорируются.
 * Получатель посылает запрос на получение данных с указанием топика. Если топик отсутствует, то создается новый.
 * А если топик присутствует, то сообщение забирается из начала индивидуальной очереди получателя и удаляется.
 * Когда получатель впервые получает данные из топика – для него создается индивидуальная пустая очередь.
 * Все последующие сообщения от отправителей с данными для этого топика помещаются в эту очередь тоже.
 */
public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic =
            new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String name = req.getSourceName();
        Resp rsl = new Resp("", "500");
        if ("POST".equals(req.httpRequestType())) {
            rsl = new Resp(req.getParam(), "204");
            for (ConcurrentLinkedQueue<String> queue : topic.getOrDefault(name, new ConcurrentHashMap<>()).values()) {
                queue.add(req.getParam());
                rsl = new Resp(req.getParam(), "200");
            }
        } else if ("GET".equals(req.httpRequestType())) {
            topic.putIfAbsent(name, new ConcurrentHashMap<>());
            topic.get(name).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String text = topic.get(name).get(req.getParam()).poll();
            rsl = new Resp(text, "200");
            if (text == null) {
                rsl = new Resp("", "204");
            }
        }
        return rsl;
    }
}