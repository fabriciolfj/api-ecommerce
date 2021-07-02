package com.github.fabriciolfj.apiecommerce.facade.converters;

import com.github.fabriciolfj.apiecommerce.entity.AddressEntity;
import com.github.fabriciolfj.apiecommerce.model.AddAddressReq;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

    public AddressEntity toEntity(final AddAddressReq model) {
        return AddressEntity.builder()
                .number(model.getNumber())
                .residency(model.getResidency())
                .street(model.getStreet())
                .city(model.getCity())
                .state(model.getState())
                .country(model.getCountry())
                .pincode(model.getPincode())
                .build();
    }
}
