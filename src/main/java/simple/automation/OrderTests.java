package simple.automation;

import org.junit.Test;

public class OrderTests extends BaseTestWithOrder{

    public void testCreateOrder(){
        System.out.println("check that order was created");
    }

    @Test
    public void testListOrders(){
        System.out.println("list orders");
        System.out.println("check that order is displayed");
    }

}
