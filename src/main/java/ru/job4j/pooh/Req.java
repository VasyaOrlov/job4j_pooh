package ru.job4j.pooh;

/**
 * Class Req служит для парсинга входящего запроса
 */
public class Req {

    /**
     * httpRequestType - указывает тип запроса: GET или POST
     */
    private final String httpRequestType;

    /**
     * poohMode - указывает на режим работы: queue или topic
     */
    private final String poohMode;

    /**
     * sourceName - имя Queue или Topic
     */
    private final String sourceName;

    /**
     * param - содержимое запроса
     */
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    /**
     * метод of парсит запрос на данные и инициализирует объект типа Req
     * @param content - запрос
     * @return - объект типа Req
     */
    public static Req of(String content) {
        String[] arrayContent = content.split(System.lineSeparator(), 8);
        String[] firstContent = arrayContent[0].split("/");
        String httpRequestType = firstContent[0].trim();
        String poohMode = firstContent[1].trim();
        String sourceName = firstContent[2].split(" ")[0].trim();
        String param;
        if ("GET".equals(httpRequestType) && "topic".equals(poohMode)) {
            param = firstContent[3].split(" ")[0];
        } else {
            param = arrayContent[arrayContent.length - 1].trim();
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}