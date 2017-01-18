package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import server.service.CoocksMonitor.CoocksMonitor;


@Configuration
@ImportResource(value = {"classpath*:server-app-context.xml"})
@EnableAutoConfiguration
@EnableWebMvc
public class AppRunner {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AppRunner.class, args);
        CoocksMonitor monitor = context.getBean(CoocksMonitor.class);
        DispatcherServlet servlet = context.getBean(DispatcherServlet.class);
        servlet.setThrowExceptionIfNoHandlerFound(false);
        servlet.setDetectAllHandlerExceptionResolvers(true);
        ObjectMapper mapper = context.getBean(ObjectMapper.class);
        mapper.registerModule(new JodaModule());
        monitor.init();
    }
}
