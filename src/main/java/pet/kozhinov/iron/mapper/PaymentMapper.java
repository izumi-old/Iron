package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.entity.dto.PaymentDto;
import pet.kozhinov.iron.exception.ForbiddenException;

@RequiredArgsConstructor
@Component(PaymentMapper.NAME)
public class PaymentMapper implements Mapper<Payment, PaymentDto> {
    public static final String NAME = "iron_PaymentMapper";

    @Override
    public PaymentDto map1(Payment from) {
        PaymentDto dto = new PaymentDto();
        dto.setId(from.getId().toString());
        dto.setAmount(from.getAmount());
        dto.setDate(from.getDate());
        dto.setPaidOut(from.isPaidOut());
        dto.setInterestRepaymentAmount(from.getInterestRepaymentAmount());
        dto.setLoanRepaymentAmount(from.getLoanRepaymentAmount());
        return dto;
    }

    @Override
    public Payment map2(PaymentDto to) {
        throw new ForbiddenException();
    }
}
