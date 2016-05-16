package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

public class SetWhoServesOrderingRequest {
    private Ordering ordering;
    private User user;

    public SetWhoServesOrderingRequest() {
    }

    public SetWhoServesOrderingRequest(Ordering ordering, User user) {
        this.ordering = ordering;
        this.user = user;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
