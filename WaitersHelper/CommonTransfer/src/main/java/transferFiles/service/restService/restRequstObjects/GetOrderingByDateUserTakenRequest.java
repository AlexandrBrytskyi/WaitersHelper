package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.user.User;

import org.joda.time.LocalDate;

public class GetOrderingByDateUserTakenRequest {
    private User whoTaken;
    private LocalDate concretteDay;

    public GetOrderingByDateUserTakenRequest() {
    }

    public GetOrderingByDateUserTakenRequest(User whoTaken, LocalDate concretteDay) {
        this.whoTaken = whoTaken;
        this.concretteDay = concretteDay;
    }

    public User getWhoTaken() {
        return whoTaken;
    }

    public void setWhoTaken(User whoTaken) {
        this.whoTaken = whoTaken;
    }

    public LocalDate getConcretteDay() {
        return concretteDay;
    }

    public void setConcretteDay(LocalDate concretteDay) {
        this.concretteDay = concretteDay;
    }
}
