package simple.automation.satests;

public class ObjectSensitivityTest {
    class MyBox {
        int value;
    }

    public void objectSensitivityTest() {
        MyBox a = new MyBox();
        MyBox b = a;
        b.value = 0;

        int result = 10/a.value;
    }
}
