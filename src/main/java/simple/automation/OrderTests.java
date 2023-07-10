package simple.automation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Smoke
@Tag("pseudo")
public class   OrderTests {

    @BeforeEach
    public void createOrder(){
        System.out.println("create order");
    }

    @Test
    @Tag("create")
    public void testCreateOrder(){
        System.out.println("check that order was created");
    }

    @ParameterizedTest
    @MethodSource
    public void testListOrdersAsManager(Roles role, String orderType){
        System.out.println("list orders as " + role.getDescription());
        System.out.println("check that is displayed orders for " + orderType);
    }

    static Stream<Arguments> testListOrdersAsManager(){
        return Stream.of(
          arguments(Roles.MANAGER, "orders for manager"),
          arguments(Roles.SENIOR_MANAGER, "orders for senior")
        );
    }

    enum Roles{
        MANAGER("manager"),
        SENIOR_MANAGER("manager of managers"),
        LEAD_MANAGER("manager of manager of managers"),
        HARD_WORKER("just a worker");

        Roles(String description){
            this.description = description;
        }

        private String description;
        public String getDescription(){
            return description;
        }
    }

    @AfterEach
    public void deleteOrder(){
        System.out.println("delete order");
    }

}
