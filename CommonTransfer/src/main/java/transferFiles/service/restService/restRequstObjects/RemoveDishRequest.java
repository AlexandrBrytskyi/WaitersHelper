package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.dish.Dish;
import transferFiles.model.user.User;

public class RemoveDishRequest {
   private Dish dish;
    private User logined;

    public RemoveDishRequest() {
    }

    public RemoveDishRequest(Dish dish, User logined) {
        this.dish = dish;
        this.logined = logined;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public User getLogined() {
        return logined;
    }

    public void setLogined(User logined) {
        this.logined = logined;
    }
}
