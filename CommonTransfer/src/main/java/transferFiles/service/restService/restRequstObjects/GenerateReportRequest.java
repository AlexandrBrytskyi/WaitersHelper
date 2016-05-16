package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;

import java.util.List;

public class GenerateReportRequest {

    private List<Denomination> denominations;
    private List<Ingridient> ingridients;
    private String headString;

    public GenerateReportRequest() {
    }

    public GenerateReportRequest(List<Denomination> denominations, List<Ingridient> ingridients, String headString) {
        this.denominations = denominations;
        this.ingridients = ingridients;
        this.headString = headString;
    }

    public List<Denomination> getDenominations() {
        return denominations;
    }

    public void setDenominations(List<Denomination> denominations) {
        this.denominations = denominations;
    }

    public List<Ingridient> getIngridients() {
        return ingridients;
    }

    public void setIngridients(List<Ingridient> ingridients) {
        this.ingridients = ingridients;
    }

    public String getHeadString() {
        return headString;
    }

    public void setHeadString(String headString) {
        this.headString = headString;
    }
}
