package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("Value")
    int Value;

    @SerializedName("Units")
    String Units;

    @SerializedName("DebitPaymentChannelID")
    String DebitPaymentChannelID;

    @SerializedName("CreditPaymentChannelID")
    String CreditPaymentChannelID;

    @SerializedName("PaymentMethod")
    String PaymentMethod;

    @SerializedName("LedgerDetails")
    String LedgerDetails;

    @SerializedName("RemainingAmount")
    String RemainingAmount;

    @SerializedName("NominalValue")
    String NominalValue;

    @SerializedName("NotionalPrincipal")
    String NotionalPrincipal;

    public String getNotionalPrincipal() {
        return NotionalPrincipal;
    }

    public void setNotionalPrincipal(String notionalPrincipal) {
        NotionalPrincipal = notionalPrincipal;
    }

    public String getNominalValue() {
        return NominalValue;
    }

    public void setNominalValue(String nominalValue) {
        NominalValue = nominalValue;
    }

    public void setValue(int Value) {
        this.Value = Value;
    }
    public int getValue() {
        return Value;
    }

    public void setUnits(String Units) {
        this.Units = Units;
    }
    public String getUnits() {
        return Units;
    }

    public void setDebitPaymentChannelID(String DebitPaymentChannelID) {
        this.DebitPaymentChannelID = DebitPaymentChannelID;
    }
    public String getDebitPaymentChannelID() {
        return DebitPaymentChannelID;
    }

    public void setCreditPaymentChannelID(String CreditPaymentChannelID) {
        this.CreditPaymentChannelID = CreditPaymentChannelID;
    }
    public String getCreditPaymentChannelID() {
        return CreditPaymentChannelID;
    }

    public void setPaymentMethod(String PaymentMethod) {
        this.PaymentMethod = PaymentMethod;
    }
    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setLedgerDetails(String LedgerDetails) {
        this.LedgerDetails = LedgerDetails;
    }
    public String getLedgerDetails() {
        return LedgerDetails;
    }

    public void setRemainingAmount(String RemainingAmount) {
        this.RemainingAmount = RemainingAmount;
    }
    public String getRemainingAmount() {
        return RemainingAmount;
    }
}
