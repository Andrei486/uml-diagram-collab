package carleton.sysc4907;

import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.model.SessionModel;

import java.util.HashMap;

public class Startup {

    private static final HashMap<Class<?>, Object> registeredInstances = new HashMap<>();
    public static <T> void registerInstance(Class<T> type, T instance) {
        registeredInstances.put(type, instance);
    }

    public static <T> T getRegisteredInstance(Class<T> type) {
        return (T) registeredInstances.get(type);
    }

    public static void registerControllerInjectionMethods(
            DependencyInjector injector) {
        injector.addInjectionMethod(SessionInfoBarController.class,
                () -> new SessionInfoBarController(getRegisteredInstance(SessionModel.class)));
    }

}
