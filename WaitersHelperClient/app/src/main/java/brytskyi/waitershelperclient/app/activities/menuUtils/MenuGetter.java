package brytskyi.waitershelperclient.app.activities.menuUtils;


import transferFiles.model.user.UserType;

import java.util.LinkedList;
import java.util.List;

public class MenuGetter {

    public static final List<String> BARMEN_MAIN_MENU_POINTS = new LinkedList<String>() {{
        add("Work");
        add("Orders");
        add("Dishes");
        add("Account");
    }};

    public static final List<String> WAITER_MAIN_MENU_POINTS = new LinkedList<String>() {{
        add("Orders");
        add("Dishes");
        add("Account");
    }};

    public static final List<String> ADMIN_MAIN_MENU_POINTS = new LinkedList<String>() {{
        add("Orders");
        add("Dishes");
        add("Products");
        add("Reports");
        add("Users");
        add("Account");
    }};

    public static final List<String> COOK_MAIN_MENU_POINTS = new LinkedList<String>() {{
        add("Work");
        add("Account");
    }};


    public static List<String> getPoints(UserType type) {
        switch (type) {
            case BARMEN: return BARMEN_MAIN_MENU_POINTS;
            case WAITER:return WAITER_MAIN_MENU_POINTS;
            case ADMIN:return ADMIN_MAIN_MENU_POINTS;
        }
        return COOK_MAIN_MENU_POINTS;
    }
}
