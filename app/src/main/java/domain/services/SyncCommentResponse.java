package domain.services;

import loans.model.LoanCollection;

public class SyncCommentResponse {
    public final SyncResponseEventType eventType;
    public final LoanCollection loanCollection;

    public SyncCommentResponse(SyncResponseEventType eventType, LoanCollection loanCollection) {
        this.eventType = eventType;
        this.loanCollection = loanCollection;
    }
}
