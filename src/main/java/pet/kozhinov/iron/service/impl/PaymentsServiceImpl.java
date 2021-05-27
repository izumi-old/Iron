package pet.kozhinov.iron.service.impl;

import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.service.PaymentsService;
import pet.kozhinov.iron.utils.AccurateNumber;

import java.util.Collection;
import java.util.LinkedList;

import static pet.kozhinov.iron.utils.Constants.*;
import static pet.kozhinov.iron.utils.Constants.ANNUITY_COEFFICIENT;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    /*
    formulas:

            1) P = S * (i + i / ((1+i)^n - 1));

    y = (1+i)^n -1;
    x = i / y;
    P = S * (i + x);

    where
    P - month payment amount
    S - loan amount
    i - month interest rate
    n - duration in months

    2) I(n) = S(n) * i;

    where
    I(n) - percents amount
    S(n) - left loan amount

    3) B(n) = P - I(n);

    where
    B - body amount
    */
    @Override
    public Collection<Payment> schedule(Case aCase) {
        AccurateNumber monthInterestRate = new AccurateNumber(aCase.getLoan().getInterestRate())
                .divide(MAX_PERCENTS)
                .divide(MONTHS_IN_YEAR);

        AccurateNumber y = ((new AccurateNumber(ANNUITY_COEFFICIENT)
                .add(monthInterestRate))
                .pow(aCase.getDurationMonths()))
                .subtract(ANNUITY_COEFFICIENT);
        AccurateNumber x = monthInterestRate.divide(y);

        AccurateNumber annuityCoefficient = monthInterestRate.add(x);

        AccurateNumber monthPayment = aCase.getAmount().multiply(annuityCoefficient);
        AccurateNumber left = aCase.getAmount();

        Collection<Payment> result = new LinkedList<>();
        for (int i = 1; i <= aCase.getDurationMonths(); i++) {
            AccurateNumber percents = left.multiply(monthInterestRate);
            AccurateNumber body = monthPayment.subtract(percents);

            Payment payment = new Payment();
            payment.setOrderNumber(i);
            payment.setAmount(monthPayment.getValue());
            payment.setInterestRepaymentAmount(percents.getValue());
            payment.setLoanRepaymentAmount(body.getValue());

            left = left.subtract(body);
            result.add(payment);
        }

        return result;
    }
}
