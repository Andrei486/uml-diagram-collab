package carleton.sysc4907;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Contains methods to load FXML with controllers constructed via dependency injection.
 */
public class DependencyInjector {

    //Maps controllers to their instantiation methods
    private final Map<Class<?>, Callable<?>> injectionMethods = new HashMap<>();

    /**
     * Loads an FXML file and returns the root node.
     * @param location The location of the FXML file.
     * @return The root node.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public Parent load(String location) throws IOException {
        FXMLLoader loader = getLoader(location);
        return loader.load();
    }

    /**
     * Creates an FXML loader.
     * @param location The location of the FXML file.
     * @return The FXML loader.
     */
    public FXMLLoader getLoader(String location) {
        return new FXMLLoader(
                DependencyInjector.class.getResource(location),
                null,
                new JavaFXBuilderFactory(),
                this::constructController);
    }

    /**
     * Constructs a controller with a saves method if available, otherwise use the default constructor.
     * @param controllerClass The controller type to construct.
     * @return The requested controller.
     */
    private Object constructController(Class<?> controllerClass) {
        if(injectionMethods.containsKey(controllerClass)) {
            return loadControllerWithSavedMethod(controllerClass);
        } else {
            return loadControllerWithDefaultConstructor(controllerClass);
        }
    }

    /**
     * Creates a controller with a method known by the DependencyInjector.
     * @param controller The controller type to construct.
     * @return The requested controller.
     */
    private Object loadControllerWithSavedMethod(Class<?> controller){
        try {
            return injectionMethods.get(controller).call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Creates a controller with the controller's default constructor.
     * @param controller The controller type to construct.
     * @return The requested controller.
     */
    private Object loadControllerWithDefaultConstructor(Class<?> controller){
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Add an instantiation method for a controller to the DependencyInjector.
     * @param controller The controller type the method applies to.
     * @param method The method to construct the controller.
     */
    public void addInjectionMethod(Class<?> controller, Callable<?> method){
        injectionMethods.put(controller, method);
    }

    /**
     * Remove an instantiation method for a controller from the DependencyInjector.
     * @param controller The controller type the method applies to.
     */
    public void removeInjectionMethod(Class<?> controller){
        injectionMethods.remove(controller);
    }
}