package eu.simplecompliance.ublvalidator;

public class InvoiceRequest {

    private String legalName;
    private String vatId;
    private String dueDate;

    public InvoiceRequest() {}

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public void setVatId(String vatId) {
        this.vatId = vatId;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getVatId() {
        return vatId;
    }

    public String getDueDate() {
        return dueDate;
    }
}
