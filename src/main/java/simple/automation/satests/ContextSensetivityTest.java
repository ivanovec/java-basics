package simple.automation.satests;

public class ContextSensetivityTest {
    class ContextSensetivity {
        Payment payment;
        ContextSensetivity(Payment payment){
            this.payment = payment;
        }

        public String calculatePayment(){
            return payment.getPaymentType().toLowerCase();
        }
    }
}

class CreditCard {
    public String getPaymentType() {
        return "credit card";
    }
}
class NullPayment implements Payment {
    @Override
    public String getPaymentType() {
        return null;
    }
}
interface Payment {
    String getPaymentType();
}
