package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.user.User;

import org.joda.time.LocalDate;

/**
 * User: huyti
 * Date: 14.05.2016
 */
public class GetOrderingByDateBeginEndUserServesRequest {

   private User whoServing;
   private LocalDate begin;
   private LocalDate end;

    public GetOrderingByDateBeginEndUserServesRequest() {
    }

    public GetOrderingByDateBeginEndUserServesRequest(User whoServing, LocalDate begin, LocalDate end) {
        this.whoServing = whoServing;
        this.begin = begin;
        this.end = end;
    }

    public User getWhoServing() {
        return whoServing;
    }

    public void setWhoServing(User whoServing) {
        this.whoServing = whoServing;
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
