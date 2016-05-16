package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.denomination.Denomination;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class SetPortionRequest {
   private   double portion; private Denomination denomination;

    public SetPortionRequest() {
    }

    public SetPortionRequest(double portion, Denomination denomination) {
        this.portion = portion;
        this.denomination = denomination;
    }

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }
}
