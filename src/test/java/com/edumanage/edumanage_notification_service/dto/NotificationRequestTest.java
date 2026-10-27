
package com.edumanage.edumanage_notification_service.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationRequestTest {

    private final Validator validator;

    NotificationRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldRejectInvalidEmail() {
        NotificationRequest request = new NotificationRequest(
                "not-an-email",
                "Enrollment confirmed"
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectBlankMessage() {
        NotificationRequest request = new NotificationRequest(
                "student@example.com",
                " "
        );

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptValidRequest() {
        NotificationRequest request = new NotificationRequest(
                "student@example.com",
                "Enrollment confirmed"
        );

        assertTrue(validator.validate(request).isEmpty());
    }
}
