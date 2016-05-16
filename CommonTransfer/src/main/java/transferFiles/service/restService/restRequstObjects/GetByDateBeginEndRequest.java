package transferFiles.service.restService.restRequstObjects;


import org.joda.time.LocalDate;

public class GetByDateBeginEndRequest {
    private LocalDate begin;
    private LocalDate end;

    public GetByDateBeginEndRequest() {
    }

    public GetByDateBeginEndRequest(LocalDate begin, LocalDate end) {
        this.begin = begin;
        this.end = end;
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
