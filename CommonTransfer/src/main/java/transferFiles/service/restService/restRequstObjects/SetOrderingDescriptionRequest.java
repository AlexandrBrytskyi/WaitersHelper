package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.order.Ordering;

public class SetOrderingDescriptionRequest {

   private String description; private Ordering ordering  ;

    public SetOrderingDescriptionRequest() {
    }

    public SetOrderingDescriptionRequest(String description, Ordering ordering) {
        this.description = description;
        this.ordering = ordering;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }
}
