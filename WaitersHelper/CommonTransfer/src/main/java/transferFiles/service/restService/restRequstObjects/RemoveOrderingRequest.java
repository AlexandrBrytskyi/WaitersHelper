package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

public class RemoveOrderingRequest {

    private Ordering source;
    private User logined;

    public RemoveOrderingRequest() {
    }

    public RemoveOrderingRequest(Ordering source, User logined) {
        this.source = source;
        this.logined = logined;
    }

    public Ordering getSource() {
        return source;
    }

    public void setSource(Ordering source) {
        this.source = source;
    }

    public User getLogined() {
        return logined;
    }

    public void setLogined(User logined) {
        this.logined = logined;
    }
}
