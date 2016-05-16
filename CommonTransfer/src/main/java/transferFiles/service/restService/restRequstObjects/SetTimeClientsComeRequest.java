package transferFiles.service.restService.restRequstObjects;

import org.joda.time.LocalDateTime;
import transferFiles.model.order.Ordering;


public class SetTimeClientsComeRequest {

   private Ordering ordering;private LocalDateTime time;

    public SetTimeClientsComeRequest() {
    }

    public SetTimeClientsComeRequest(Ordering ordering, LocalDateTime time) {
        this.ordering = ordering;
        this.time = time;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
