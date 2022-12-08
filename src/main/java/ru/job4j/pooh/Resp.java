package ru.job4j.pooh;
/**
 * Класс Resp - ответ от сервиса
 */
public class Resp {
    /**
     * text - текст ответа
     */
    private final String text;

    /**
     * status - это HTTP response status codes
     */
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String text() {
        return text;
    }

    public String status() {
        return status;
    }
}