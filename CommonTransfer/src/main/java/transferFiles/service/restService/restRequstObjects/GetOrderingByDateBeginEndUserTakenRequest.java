package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.user.User;

import org.joda.time.LocalDate;


public class GetOrderingByDateBeginEndUserTakenRequest {

    private User whoTaken;
    private LocalDate begin;
    private LocalDate end;

    public GetOrderingByDateBeginEndUserTakenRequest() {
    }

    public GetOrderingByDateBeginEndUserTakenRequest(User whoTaken, LocalDate begin, LocalDate end) {
        this.whoTaken = whoTaken;
        this.begin = begin;
        this.end = end;
    }

    public User getWhoTaken() {
        return whoTaken;
    }

    public void setWhoTaken(User whoTaken) {
        this.whoTaken = whoTaken;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public void setBegin(LocalDate begin) {
        this.begin = begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
