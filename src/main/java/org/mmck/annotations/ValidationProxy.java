package org.mmck.annotations;

// this file intercepts the call for the validation method defining a proxy

import lombok.RequiredArgsConstructor;
import org.mmck.services.FileService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@RequiredArgsConstructor
public class ValidationProxy implements InvocationHandler {
    // add this invocation-handler stuff pattern I asked Gemini so the proxy works setting a target object
    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(ValidDirectory.class)) {
                ValidDirectory annotation = parameters[i].getAnnotation(ValidDirectory.class);
                DirectoryValidator.validateAndResolve((String) args[i], annotation);
            }
        }
        try {
            Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            return targetMethod.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
