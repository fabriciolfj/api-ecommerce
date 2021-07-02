package com.github.fabriciolfj.apiecommerce.facade.validation;

import com.github.fabriciolfj.apiecommerce.exceptions.ResourceNotFoundException;
import com.github.fabriciolfj.apiecommerce.model.NewOrder;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ValidationNewOrderFacade {

    public void execute(final NewOrder newOrder) {
        if (Strings.isEmpty(newOrder.getCustomerId())) {
            throw new ResourceNotFoundException("Invalid customer id.");
        }

        if (Objects.isNull(newOrder.getAddress()) || Strings.isEmpty(newOrder.getAddress().getId())) {
            throw new ResourceNotFoundException("Invalid address id.");
        }

        if (Objects.isNull(newOrder.getCard()) || Strings.isEmpty(newOrder.getCard().getId())) {
            throw new ResourceNotFoundException("Invalid card id.");
        }
    }
}
