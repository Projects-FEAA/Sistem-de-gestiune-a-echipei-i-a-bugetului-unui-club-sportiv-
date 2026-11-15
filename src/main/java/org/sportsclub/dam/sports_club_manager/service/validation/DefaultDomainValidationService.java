package org.sportsclub.dam.sports_club_manager.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.sportsclub.dam.sports_club_manager.domain.exception.BusinessRuleViolationException;
import org.sportsclub.dam.sports_club_manager.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultDomainValidationService implements DomainValidationService {
    private final Validator validator;
    private final DomainValidator<Player> playerValidation;
    private final DomainValidator<Contract> contractValidation;
    private final DomainValidator<Injury> injuryValidation;
    private final DomainValidator<SeasonBudget> budgetValidation;
    private final DomainValidator<Transaction> transactionValidation;
    private final DomainValidator<User> userValidation;

    @Autowired
    public DefaultDomainValidationService(
            Validator validator,
            DomainValidator<Player> playerValidation,
            DomainValidator<Contract> contractValidation,
            DomainValidator<Injury> injuryValidation,
            DomainValidator<SeasonBudget> budgetValidation,
            DomainValidator<Transaction> transactionValidation,
            DomainValidator<User> userValidation) {
        this.validator = validator;
        this.playerValidation = playerValidation;
        this.contractValidation = contractValidation;
        this.injuryValidation = injuryValidation;
        this.budgetValidation = budgetValidation;
        this.transactionValidation = transactionValidation;
        this.userValidation = userValidation;
    }

    public DefaultDomainValidationService(Validator validator) {
        this(validator, new PlayerValidationService(), new ContractValidationService(),
                new InjuryValidationService(), new SeasonBudgetValidationService(),
                new TransactionValidationService(), new UserValidationService());
    }

    @Override
    public void validate(Object target) {
        if (target == null) {
            throw new BusinessRuleViolationException("Validated object cannot be null.");
        }

        Set<ConstraintViolation<Object>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .sorted()
                    .collect(Collectors.joining("; "));
            throw new BusinessRuleViolationException("Validation failed: " + details);
        }

        if (target instanceof Player player) {
            playerValidation.validate(player);
        } else if (target instanceof Contract contract) {
            contractValidation.validate(contract);
        } else if (target instanceof Injury injury) {
            injuryValidation.validate(injury);
        } else if (target instanceof SeasonBudget budget) {
            budgetValidation.validate(budget);
        } else if (target instanceof Transaction transaction) {
            transactionValidation.validate(transaction);
        } else if (target instanceof User user) {
            userValidation.validate(user);
        }
    }
}
