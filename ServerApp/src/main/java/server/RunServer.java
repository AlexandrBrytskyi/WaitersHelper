package server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.service.CoocksMonitor.CoocksMonitor;

/*Starts context*/
public class RunServer {



    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:server-app-context.xml");
        CoocksMonitor monitor = context.getBean(CoocksMonitor.class);
        monitor.init();
    }

}
