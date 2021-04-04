class LoanCaseDto {
    constructor(id, clientId, loanId, amount, durationMonths, statusBankSide, statusClientSide, confirmationDate,
                client, loan, closed, payments) {
        this.id = id;
        this.clientId = clientId;
        this.loanId = loanId;
        this.amount = amount;
        this.durationMonths = durationMonths;
        this.statusBankSide = statusBankSide;
        this.statusClientSide = statusClientSide;
        this.confirmationDate = confirmationDate;

        this.client = client;
        this.loan = loan;
        this.closed = closed;
        this.payments = payments;
    }
}