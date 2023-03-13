package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class Terms {

    @SerializedName("DayCountConvention")
    String DayCountConvention;

    @SerializedName("CycleOfInterestPayment")
    String CycleOfInterestPayment;

    @SerializedName("InitialExchangeDate")
    String InitialExchangeDate;

    @SerializedName("NominalInterestRate")
    String NominalInterestRate;

    @SerializedName("ContractDealDate")
    String ContractDealDate;

    @SerializedName("ContractID")
    String ContractID;

    @SerializedName("MaturityDate")
    String MaturityDate;

    @SerializedName("LegalEntityIDRecordCreator")
    String LegalEntityIDRecordCreator;

    @SerializedName("LegalEntityIDCounterparty")
    String LegalEntityIDCounterparty;

    @SerializedName("Tenure")
    String Tenure;

    @SerializedName("StatusDate")
    String StatusDate;

    @SerializedName("CycleAnchorDateOfInterestPayment")
    String CycleAnchorDateOfInterestPayment;

    @SerializedName("NotionalPrincipal")
    String NotionalPrincipal;

    @SerializedName("RoundingConvention")
    String RoundingConvention;

    @SerializedName("Currency")
    String Currency;

    @SerializedName("ContractType")
    String ContractType;

    @SerializedName("ContractRole")
    String ContractRole;

    @SerializedName("ContractStatus")
    String ContractStatus;

    @SerializedName("ProductIdentifier")
    String ProductIdentifier;

    @SerializedName("LoanCycle")
    int LoanCycle;

    @SerializedName("AccruedInterest")
    String AccruedInterest;

    @SerializedName("feeAccrued")
    String feeAccrued;


    public void setDayCountConvention(String DayCountConvention) {
        this.DayCountConvention = DayCountConvention;
    }

    public String getDayCountConvention() {
        return DayCountConvention;
    }

    public void setCycleOfInterestPayment(String CycleOfInterestPayment) {
        this.CycleOfInterestPayment = CycleOfInterestPayment;
    }

    public String getCycleOfInterestPayment() {
        return CycleOfInterestPayment;
    }

    public void setInitialExchangeDate(String InitialExchangeDate) {
        this.InitialExchangeDate = InitialExchangeDate;
    }

    public String getInitialExchangeDate() {
        return InitialExchangeDate;
    }

    public void setNominalInterestRate(String NominalInterestRate) {
        this.NominalInterestRate = NominalInterestRate;
    }

    public String getNominalInterestRate() {
        return NominalInterestRate;
    }

    public void setContractDealDate(String ContractDealDate) {
        this.ContractDealDate = ContractDealDate;
    }

    public String getContractDealDate() {
        return ContractDealDate;
    }

    public void setContractID(String ContractID) {
        this.ContractID = ContractID;
    }

    public String getContractID() {
        return ContractID;
    }

    public void setMaturityDate(String MaturityDate) {
        this.MaturityDate = MaturityDate;
    }

    public String getMaturityDate() {
        return MaturityDate;
    }

    public void setLegalEntityIDRecordCreator(String LegalEntityIDRecordCreator) {
        this.LegalEntityIDRecordCreator = LegalEntityIDRecordCreator;
    }

    public String getLegalEntityIDRecordCreator() {
        return LegalEntityIDRecordCreator;
    }

    public void setLegalEntityIDCounterparty(String LegalEntityIDCounterparty) {
        this.LegalEntityIDCounterparty = LegalEntityIDCounterparty;
    }

    public String getLegalEntityIDCounterparty() {
        return LegalEntityIDCounterparty;
    }

    public void setTenure(String Tenure) {
        this.Tenure = Tenure;
    }

    public String getTenure() {
        return Tenure;
    }

    public void setStatusDate(String StatusDate) {
        this.StatusDate = StatusDate;
    }

    public String getStatusDate() {
        return StatusDate;
    }

    public void setCycleAnchorDateOfInterestPayment(String CycleAnchorDateOfInterestPayment) {
        this.CycleAnchorDateOfInterestPayment = CycleAnchorDateOfInterestPayment;
    }

    public String getCycleAnchorDateOfInterestPayment() {
        return CycleAnchorDateOfInterestPayment;
    }

    public void setNotionalPrincipal(String NotionalPrincipal) {
        this.NotionalPrincipal = NotionalPrincipal;
    }

    public String getNotionalPrincipal() {
        return NotionalPrincipal;
    }

    public void setRoundingConvention(String RoundingConvention) {
        this.RoundingConvention = RoundingConvention;
    }

    public String getRoundingConvention() {
        return RoundingConvention;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setContractType(String ContractType) {
        this.ContractType = ContractType;
    }

    public String getContractType() {
        return ContractType;
    }

    public void setContractRole(String ContractRole) {
        this.ContractRole = ContractRole;
    }

    public String getContractRole() {
        return ContractRole;
    }

    public void setContractStatus(String ContractStatus) {
        this.ContractStatus = ContractStatus;
    }

    public String getContractStatus() {
        return ContractStatus;
    }

    public void setProductIdentifier(String ProductIdentifier) {
        this.ProductIdentifier = ProductIdentifier;
    }

    public String getProductIdentifier() {
        return ProductIdentifier;
    }

    public void setLoanCycle(int LoanCycle) {
        this.LoanCycle = LoanCycle;
    }

    public int getLoanCycle() {
        return LoanCycle;
    }

    public void setAccruedInterest(String AccruedInterest) {
        this.AccruedInterest = AccruedInterest;
    }

    public String getAccruedInterest() {
        return AccruedInterest;
    }

    public void setFeeAccrued(String feeAccrued) {
        this.feeAccrued = feeAccrued;
    }

    public String getFeeAccrued() {
        return feeAccrued;
    }
}
