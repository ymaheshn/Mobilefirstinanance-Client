package loans.model;

import com.google.gson.annotations.SerializedName;


public class Terms {
    @SerializedName("NextPrincipalRedemptionPayment")
    public String nextPrincipalRedemptionPayment;
    @SerializedName("NominalInterestRate")
    public String nominalInterestRate;
    @SerializedName("ContractStatus")
    public String contractStatus;
    @SerializedName("ContractID")
    public String contractID;
    @SerializedName("LegalEntityIDRecordCreator")
    public String legalEntityIDRecordCreator;
    @SerializedName("Tenure")
    public String tenure;
    @SerializedName("CycleAnchorDateOfInterestPayment")
    public String cycleAnchorDateOfInterestPayment;
    @SerializedName("Currency")
    public String currency;
    @SerializedName("CycleAnchorDateOfPrincipalRedemption")
    public String cycleAnchorDateOfPrincipalRedemption;
    @SerializedName("CycleOfPrincipalRedemption")
    public String cycleOfPrincipalRedemption;
    @SerializedName("DayCountConvention")
    public String dayCountConvention;
    @SerializedName("CycleOfInterestPayment")
    public String cycleOfInterestPayment;
    @SerializedName("InitialExchangeDate")
    public String initialExchangeDate;
    @SerializedName("ContractDealDate")
    public String contractDealDate;
    @SerializedName("MaturityDate")
    public String maturityDate;
    @SerializedName("LegalEntityIDCounterparty")
    public String legalEntityIDCounterparty;
    @SerializedName("StatusDate")
    public String statusDate;
    @SerializedName("NotionalPrincipal")
    public String notionalPrincipal;
    @SerializedName("DelinquencyPeriod")
    public String delinquencyPeriod;
    @SerializedName("RoundingConvention")
    public String roundingConvention;
    @SerializedName("BusinessDayConvention")
    public String businessDayConvention;
    @SerializedName("ProductIdentifier")
    public String productIdentifier;
    @SerializedName("ContractType")
    public String contractType;
    @SerializedName("GracePeriod")
    public String gracePeriod;
    @SerializedName("LoanCycle")
    public int loanCycle;
    @SerializedName("ContractRole")
    public String contractRole;
    @SerializedName("FeeRate")
    public String feeRate;
    @SerializedName("EventType")
    public String eventType;
    @SerializedName("FeeBasis")
    public String feeBasis;
    @SerializedName("CycleAnchorDateOfFee")
    public String cycleAnchorDateOfFee;


    public String getNextPrincipalRedemptionPayment() {
        return nextPrincipalRedemptionPayment;
    }

    public void setNextPrincipalRedemptionPayment(String nextPrincipalRedemptionPayment) {
        this.nextPrincipalRedemptionPayment = nextPrincipalRedemptionPayment;
    }

    public String getNominalInterestRate() {
        return nominalInterestRate;
    }

    public void setNominalInterestRate(String nominalInterestRate) {
        this.nominalInterestRate = nominalInterestRate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getLegalEntityIDRecordCreator() {
        return legalEntityIDRecordCreator;
    }

    public void setLegalEntityIDRecordCreator(String legalEntityIDRecordCreator) {
        this.legalEntityIDRecordCreator = legalEntityIDRecordCreator;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getCycleAnchorDateOfInterestPayment() {
        return cycleAnchorDateOfInterestPayment;
    }

    public void setCycleAnchorDateOfInterestPayment(String cycleAnchorDateOfInterestPayment) {
        this.cycleAnchorDateOfInterestPayment = cycleAnchorDateOfInterestPayment;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCycleAnchorDateOfPrincipalRedemption() {
        return cycleAnchorDateOfPrincipalRedemption;
    }

    public void setCycleAnchorDateOfPrincipalRedemption(String cycleAnchorDateOfPrincipalRedemption) {
        this.cycleAnchorDateOfPrincipalRedemption = cycleAnchorDateOfPrincipalRedemption;
    }

    public String getCycleOfPrincipalRedemption() {
        return cycleOfPrincipalRedemption;
    }

    public void setCycleOfPrincipalRedemption(String cycleOfPrincipalRedemption) {
        this.cycleOfPrincipalRedemption = cycleOfPrincipalRedemption;
    }

    public String getDayCountConvention() {
        return dayCountConvention;
    }

    public void setDayCountConvention(String dayCountConvention) {
        this.dayCountConvention = dayCountConvention;
    }

    public String getCycleOfInterestPayment() {
        return cycleOfInterestPayment;
    }

    public void setCycleOfInterestPayment(String cycleOfInterestPayment) {
        this.cycleOfInterestPayment = cycleOfInterestPayment;
    }

    public String getInitialExchangeDate() {
        return initialExchangeDate;
    }

    public void setInitialExchangeDate(String initialExchangeDate) {
        this.initialExchangeDate = initialExchangeDate;
    }

    public String getContractDealDate() {
        return contractDealDate;
    }

    public void setContractDealDate(String contractDealDate) {
        this.contractDealDate = contractDealDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getLegalEntityIDCounterparty() {
        return legalEntityIDCounterparty;
    }

    public void setLegalEntityIDCounterparty(String legalEntityIDCounterparty) {
        this.legalEntityIDCounterparty = legalEntityIDCounterparty;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getNotionalPrincipal() {
        return notionalPrincipal;
    }

    public void setNotionalPrincipal(String notionalPrincipal) {
        this.notionalPrincipal = notionalPrincipal;
    }

    public String getDelinquencyPeriod() {
        return delinquencyPeriod;
    }

    public void setDelinquencyPeriod(String delinquencyPeriod) {
        this.delinquencyPeriod = delinquencyPeriod;
    }

    public String getRoundingConvention() {
        return roundingConvention;
    }

    public void setRoundingConvention(String roundingConvention) {
        this.roundingConvention = roundingConvention;
    }

    public String getBusinessDayConvention() {
        return businessDayConvention;
    }

    public void setBusinessDayConvention(String businessDayConvention) {
        this.businessDayConvention = businessDayConvention;
    }

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getLoanCycle() {
        return loanCycle;
    }

    public void setLoanCycle(int loanCycle) {
        this.loanCycle = loanCycle;
    }

    public String getContractRole() {
        return contractRole;
    }

    public void setContractRole(String contractRole) {
        this.contractRole = contractRole;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFeeBasis() {
        return feeBasis;
    }

    public void setFeeBasis(String feeBasis) {
        this.feeBasis = feeBasis;
    }

    public String getCycleAnchorDateOfFee() {
        return cycleAnchorDateOfFee;
    }

    public void setCycleAnchorDateOfFee(String cycleAnchorDateOfFee) {
        this.cycleAnchorDateOfFee = cycleAnchorDateOfFee;
    }
}
