package brytskyi.waitershelperclient.app.restService.utils;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.charset.Charset;

public class MyHttpServerErrorException extends HttpServerErrorException {

    private HttpHeaders headers;

    public MyHttpServerErrorException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset, HttpHeaders headers) {
        super(statusCode, statusText, responseBody, responseCharset);
        this.headers = headers;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}
