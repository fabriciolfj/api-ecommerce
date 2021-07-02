package com.github.fabriciolfj.apiecommerce.controllers;

import com.github.fabriciolfj.apiecommerce.api.CustomerApi;
import com.github.fabriciolfj.apiecommerce.hateoas.AddressRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.hateoas.CardRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.hateoas.UserRepresentationModelAssembler;
import com.github.fabriciolfj.apiecommerce.model.Address;
import com.github.fabriciolfj.apiecommerce.model.Card;
import com.github.fabriciolfj.apiecommerce.model.User;
import com.github.fabriciolfj.apiecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final UserRepresentationModelAssembler assembler;
    private final AddressRepresentationModelAssembler addrAssembler;
    private final CardRepresentationModelAssembler cardAssembler;
    private final UserService service;

    @Override
    public ResponseEntity<Void> deleteCustomerById(String id) {
        service.deleteCustomerById(id);
        return accepted().build();
    }

    @Override
    public ResponseEntity<List<Address>> getAddressesByCustomerId(String id) {
        return service.getAddressesByCustomerId(id).map(addrAssembler::toListModel)
                .map(ResponseEntity::ok).orElse(notFound().build());
    }

    @Override
    public ResponseEntity<List<User>> getAllCustomers() {
        return ok(assembler.toListModel(service.getAllCustomers()));
    }

    @Override
    public ResponseEntity<Card> getCardByCustomerId(String id) {
        return service.getCardByCustomerId(id).map(cardAssembler::toModel).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }

    @Override
    public ResponseEntity<User> getCustomerById(String id) {
        return service.getCustomerById(id).map(assembler::toModel).map(ResponseEntity::ok)
                .orElse(notFound().build());
    }
}
