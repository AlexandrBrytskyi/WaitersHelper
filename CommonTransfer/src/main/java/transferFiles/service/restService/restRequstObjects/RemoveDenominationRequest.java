package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;

public class RemoveDenominationRequest {
  private   Denomination denomination;
  private   User logined;

    public RemoveDenominationRequest() {
    }

    public RemoveDenominationRequest(Denomination denomination, User logined) {
        this.denomination = denomination;
        this.logined = logined;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }

    public User getLogined() {
        return logined;
    }

    public void setLogined(User logined) {
        this.logined = logined;
    }
}
