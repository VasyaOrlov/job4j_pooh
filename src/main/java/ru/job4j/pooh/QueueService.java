package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * класс QueueService - реализует режим "queue"
 * Отправитель посылает запрос на добавление данных с указанием очереди и значением параметра.
 * Сообщение помещается в конец очереди. Если очереди нет в сервисе, то нужно создать новую и поместить в нее сообщение.
 * Получатель посылает запрос на получение данных с указанием очереди.
 * Сообщение забирается из начала очереди и удаляется.
 * Если в очередь приходят несколько получателей, то они поочередно получают сообщения из очереди.
 * Каждое сообщение в очереди может быть получено только одним получателем
 */
public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        String name = req.getSourceName();
        Resp rsl = new Resp("", "500");
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
            queue.get(name).add(req.getParam());
            rsl = new Resp(req.getParam(), "200");
        } else if ("GET".equals(req.httpRequestType())) {
            rsl = new Resp(queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll(), "200");
            if (rsl.text() == null) {
                rsl = new Resp("", "204");
            }
        }
        return rsl;
    }
}