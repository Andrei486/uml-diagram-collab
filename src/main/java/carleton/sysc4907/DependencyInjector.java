package carleton.sysc4907;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
public class DependencyInjector {

    private final Map<Class<?>, Callable<?>> injectionMethods = new HashMap<>();

    public Parent load(String location) throws IOException {
        FXMLLoader loader = getLoader(location);
        return loader.load();
    }
    public FXMLLoader getLoader(String location) {
        return new FXMLLoader(
                DependencyInjector.class.getResource(location),
                null,
                new JavaFXBuilderFactory(),
                this::constructController);
    }

    private Object constructController(Class<?> controllerClass) {
        if(injectionMethods.containsKey(controllerClass)) {
            return loadControllerWithSavedMethod(controllerClass);
        } else {
            return loadControllerWithDefaultConstructor(controllerClass);
        }
    }

    private Object loadControllerWithSavedMethod(Class<?> controller){
        try {
            return injectionMethods.get(controller).call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    private Object loadControllerWithDefaultConstructor(Class<?> controller){
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
    public void addInjectionMethod(Class<?> controller, Callable<?> method){
        injectionMethods.put(controller, method);
    }
    public void removeInjectionMethod(Class<?> controller){
        injectionMethods.remove(controller);
    }
}