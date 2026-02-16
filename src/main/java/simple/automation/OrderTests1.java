package simple.automation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Smoke1
@Tag("pseudo")
public class OrderTests1 {

    @BeforeEach
    public void createOrder() {
        System.out.println("create order");
    }

    @Test1
    @Tag("create")
    public void testCreateOrder() {
        System.out.println("check that order was created");
    }

    @ParameterizedTest
    @MethodSource
    public void testListOrdersAsManager(Roles1 role, String orderType) {
        System.out.println("");
        System.out.println("list orders as " + role.getDescription());
        System.out.println("check that is displayed orders for " + orderType);
    }

    static Stream<Arguments> testListOrdersAsManager() {
        return Stream.of(
                arguments(Roles1.MANAGER, "orders for manager"),
                arguments(Roles1.SENIOR_MANAGER, "orders for senior")
        );
    }


    @AfterEach
    public void deleteOrder() {
        System.out.println("delete order");
    }

    public void newStubMethod() {
        // TODO: implement
    }

}

