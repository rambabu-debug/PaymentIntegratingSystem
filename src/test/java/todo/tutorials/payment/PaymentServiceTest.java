package todo.tutorials.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import todo.tutorials.common.ApiException;
import todo.tutorials.model.PaymentStatus;
import todo.tutorials.model.PaymentTransaction;
import todo.tutorials.model.Role;
import todo.tutorials.model.User;
import todo.tutorials.payment.dto.CreatePaymentIntentRequest;
import todo.tutorials.payment.dto.PaymentIntentResponse;
import todo.tutorials.repository.PaymentTransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private StripeGateway stripeGateway;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPaymentIntentSucceedsForMerchant() {
        User merchant = new User();
        merchant.setEmail("merchant@test.com");
        merchant.setRole(Role.MERCHANT);
        merchant.setPassword("x");

        CreatePaymentIntentRequest request = new CreatePaymentIntentRequest();
        request.setAmount(5000L);
        request.setCurrency("inr");
        request.setOrderReference("ORDER-1001");
        request.setDescription("Test order");

        when(paymentTransactionRepository.findByOrderReferenceAndMerchant("ORDER-1001", merchant)).thenReturn(Optional.empty());
        when(stripeGateway.createPaymentIntent(5000L, "inr", "Test order", "ORDER-1001"))
                .thenReturn(new StripePaymentIntent("pi_123", "secret_123", "requires_payment_method"));
        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PaymentIntentResponse response = paymentService.createPaymentIntent(merchant, request);
        assertEquals("pi_123", response.getPaymentIntentId());
        assertEquals(PaymentStatus.REQUIRES_ACTION, response.getStatus());
    }

    @Test
    void createPaymentIntentFailsForClientRole() {
        User client = new User();
        client.setEmail("client@test.com");
        client.setRole(Role.CLIENT);
        client.setPassword("x");

        CreatePaymentIntentRequest request = new CreatePaymentIntentRequest();
        request.setAmount(1000L);
        request.setCurrency("usd");
        request.setOrderReference("ORDER-FAIL");

        assertThrows(ApiException.class, () -> paymentService.createPaymentIntent(client, request));
    }

    @Test
    void updatePaymentStatusUsesIntentIdLookup() {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setStatus(PaymentStatus.CREATED);
        tx.setStripePaymentIntentId("pi_123");

        when(paymentTransactionRepository.findByStripePaymentIntentId("pi_123")).thenReturn(Optional.of(tx));
        when(paymentTransactionRepository.save(any(PaymentTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.updatePaymentStatusByIntentId("pi_123", PaymentStatus.SUCCEEDED);

        ArgumentCaptor<PaymentTransaction> captor = ArgumentCaptor.forClass(PaymentTransaction.class);
        org.mockito.Mockito.verify(paymentTransactionRepository).save(captor.capture());
        assertEquals(PaymentStatus.SUCCEEDED, captor.getValue().getStatus());
    }
}

