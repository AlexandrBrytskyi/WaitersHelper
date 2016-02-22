package client.view;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.IRemoteService;
import to.ProductTO;

/**
 * Created by serhii on 22.02.16.
 */
public class TestCommonService {


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("client-app-context.xml");
        IRemoteService remoteService = context.getBean(IRemoteService.class);
        ProductTO productTO = remoteService.getProduct();

    }
}
