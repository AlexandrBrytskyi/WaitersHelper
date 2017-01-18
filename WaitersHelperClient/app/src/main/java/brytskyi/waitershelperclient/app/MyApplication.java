package brytskyi.waitershelperclient.app;

import android.app.Application;
import android.content.Context;
import brytskyi.waitershelperclient.app.restService.*;
import brytskyi.waitershelperclient.app.restService.utils.MyErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.IBarmenService;
import transferFiles.service.restService.ICookService;
import transferFiles.service.restService.IWaitersService;
import transferFiles.validator.restValidator.IValidator;

import java.util.HashMap;
import java.util.Map;

/* Class to get context in static way*/
public class MyApplication extends Application {

    private static Map<String, Object> myContext = new HashMap<String, Object>(){{put("workActivity", false);}};
    private static Context appContext;
    private static IValidator validator;
    private static ICookService cookService;
    private static IBarmenService barmenService;
    private static IWaitersService waitersService;
    private static IAdminService adminService;
    private static User loginedUser;
    private static RestTemplate template = new RestTemplate();
    private static ObjectMapper mapper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        mapper = converter.getObjectMapper();
        mapper.registerModule(new JodaModule());
        template.getMessageConverters().add(converter);
        appContext = super.getApplicationContext();
        validator = new RestValidator(template, mapper);
        template.setErrorHandler(new MyErrorHandler());
    }

    public static User getLoginedUser() {
        return loginedUser;
    }

    public static void setLoginedUser(User logined) {
        loginedUser = logined;
        initService(loginedUser);
    }

    private static void initService(User loginedUser) {
        if (loginedUser == null) return;
        UserType type = loginedUser.getType();
        if (type.equals(UserType.COLD_KITCHEN_COCK) ||
                type.equals(UserType.HOT_KITCHEN_COCK) ||
                type.equals(UserType.MANGAL_COCK))
            cookService = new RestCookService(template, mapper);
        else if (type.equals(UserType.BARMEN)) barmenService = new RestBarmenService(template, mapper);
        else if (type.equals(UserType.WAITER)) waitersService = new RestWaiterService(template, mapper);
        else if (type.equals(UserType.ADMIN)) adminService = new RestAdminService(template, mapper);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static IValidator getValidator() {
        return validator;
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static RestTemplate getTemplate() {
        return template;
    }

    public static void putInContext(String name, Object value) {
        myContext.put(name, value);
    }

    public static Object getFromContext(String name) {
        return myContext.get(name);
    }



    public static Object getService() {
        if (adminService != null) return adminService;
        if (waitersService != null) return waitersService;
        if (barmenService != null) return barmenService;
        if (cookService != null) return cookService;
        return null;
    }
}
