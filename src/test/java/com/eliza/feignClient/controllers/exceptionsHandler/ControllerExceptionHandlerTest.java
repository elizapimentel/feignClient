package com.eliza.feignClient.controllers.exceptionsHandler;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.InvalidStateException;
import com.eliza.feignClient.services.exceptions.TaxaJurosNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void quandoTaxaJurosNotFoundExceptionRetorneResponseEntityNotFound() {
        ResponseEntity<StandardError> response = handler
                .taxaJurosNotFoundException(new TaxaJurosNotFoundException("Item não encontrado"),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals("Item não encontrado", response.getBody().getMessage());
        assertEquals(404, response.getBody().getCode());
        assertNotEquals(LocalDateTime.now(), response.getBody().getTimestamp());
    }

    @Test
    void quandoArgumentoNotValidResponseEntityBadResquest() {
        BindingResult bindingResult = mock(BindingResult.class);
        ResponseEntity<StandardError> response = handler
                .argumentoNotValid(new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(ValidationError.class, response.getBody().getClass());
        assertEquals("Erro na validação dos campos!", response.getBody().getMessage());
        assertEquals(400, response.getBody().getCode());
        //falta incluir parte do forEach - FieldError

    }

    @Test
    void quandoCnpjForemRepetidosResponseEntityBadResquest() {

        ResponseEntity<StandardError> response = handler
                .cnpjNotValid(new InvalidStateException(mock(ValidationMessage.class))
                        , new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(400, response.getBody().getCode());

    }

    @Test
    void quandoQueryNotValidResponseEntityBadResquest() {

        ResponseEntity<StandardError> response = handler
                .queryNotValid(null, new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(400, response.getBody().getCode());
    }

    @Test
    void quandoSizeIgualA0QueryNotValidResponseEntityBadResquest() {
        ResponseEntity<StandardError> response = handler
                .sizeNotValid(new IllegalArgumentException("Tamanho da página tem que ser maior que 0"),
                        new MockHttpServletRequest());
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals("Tamanho da página tem que ser maior que 0", response.getBody().getMessage());
        assertEquals(400, response.getBody().getCode());
    }

}
