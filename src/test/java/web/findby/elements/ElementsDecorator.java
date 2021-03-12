package web.findby.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ElementsDecorator extends DefaultFieldDecorator {
    private WebDriver webDriver;

    public ElementsDecorator(ElementLocatorFactory factory, WebDriver driver) {
        super(factory);
        this.webDriver = driver;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if(isCustomElement(field)){
            ElementLocator locator = factory.createLocator(field);
            return createElement(loader, locator, field.getType());
        }
        return super.decorate(loader, field);
    }

    private boolean isCustomElement(Field field){
        return CustomElement.class.isAssignableFrom(field.getType());
    }

    protected <T> T createElement(ClassLoader loader, ElementLocator locator, Class<T> clazz){
        WebElement proxy = proxyForLocator(loader, locator);

        try {
            return clazz.getConstructor(WebElement.class, WebDriver.class).newInstance(proxy, webDriver);
        } catch (NoSuchMethodException|SecurityException|IllegalAccessException|InstantiationException| InvocationTargetException e){
            throw new AssertionError("Webelement can't be represented as " + clazz);
        }
    }
}
