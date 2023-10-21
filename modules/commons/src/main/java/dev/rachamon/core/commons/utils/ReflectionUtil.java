package dev.rachamon.core.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A set of utilities to help with Reflection.
 * Based on <a href="https://github.com/dmulloy2/ProtocolLib/blob/gradle/TinyProtocol/src/main/java/com/comphenix/tinyprotocol/Reflection.java">ProtocolLib Reflection</a>.
 * Based on juanmuscaria version of ProtocolLib Reflection
 *
 * @author EverNife
 */
public class ReflectionUtil {

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param target - the target type.
     * @param name   - the name of the field, or NULL to ignore.
     * @return The field accessor.
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String name) {
        return getField(target, name, null, 0);
    }

    // Common method
    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && (fieldType == null || fieldType.isAssignableFrom(field.getType())) && index-- <= 0) {
                field.setAccessible(true);

                // A function for retrieving a specific field value
                return new FieldAccessor<T>() {

                    @Override
                    @SuppressWarnings("unchecked")
                    public T get(Object target) {
                        try {
                            return (T) field.get(target);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(Object target, Object value) {
                        try {
                            field.set(target, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(Object target) {
                        // target instanceof DeclaringClass
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }

                    @Override
                    public Field getTheField() {
                        return field;
                    }
                };
            }
        }

        // Search in parent classes
        if (target.getSuperclass() != null) return getField(target.getSuperclass(), name, fieldType, index);

        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    /**
     * Search for the first publicly and privately defined method of the given name and parameter count.
     *
     * @param clazz      - a class to start with.
     * @param methodName - the method name, or NULL to skip.
     * @param params     - the expected parameters.
     * @return An object that invokes this specific method.
     * @throws IllegalStateException If we cannot find this method.
     */
    public static MethodInvoker<?> getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        return getTypedMethod(clazz, methodName, null, params);
    }

    /**
     * Search for the first publicly and privately defined method of the given name and parameter count.
     *
     * @param clazz      - a class to start with.
     * @param methodName - the method name, or NULL to skip.
     * @param returnType - the expected return type, or NULL to ignore.
     * @param params     - the expected parameters.
     * @return An object that invokes this specific method.
     * @throws IllegalStateException If we cannot find this method.
     */
    public static MethodInvoker<?> getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?>... params) {
        List<Method> sortedMethodsByArgNumber = Arrays.stream(clazz.getDeclaredMethods()).sorted(Comparator.comparingInt(method -> method.getParameterTypes().length)).collect(Collectors.toList());
        for (final Method method : sortedMethodsByArgNumber) {
            if ((methodName == null || method.getName().equals(methodName)) && (returnType == null || method.getReturnType().equals(returnType)) && (params.length == 0 || Arrays.equals(method.getParameterTypes(), params))) {
                method.setAccessible(true);

                return new MethodInvoker<Object>() {

                    @Override
                    public Object invoke(Object target, Object... arguments) {
                        Object[] correctArgs = new Object[arguments.length];
                        for (int i = 0; i < arguments.length; i++) {
                            //Fix this error https://stackoverflow.com/questions/48577435/why-do-i-get-java-lang-illegalargumentexception-wrong-number-of-arguments-while
                            correctArgs[i] = arguments[i] != null && arguments[i].getClass().isArray() ? new Object[]{arguments[i]} : arguments[i];
                        }
                        try {
                            return method.invoke(target, correctArgs);
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot invoke method " + method, e);
                        }
                    }

                    @Override
                    public Method get() {
                        return method;
                    }

                };
            }
        }

        // Search in every superclass
        if (clazz.getSuperclass() != null) return getMethod(clazz.getSuperclass(), methodName, params);

        throw new IllegalStateException(String.format("Unable to find method %s(%s).", methodName, params.length == 0 ? "" : Arrays.asList(params)));
    }

    /**
     * Search for the first publically and privately defined constructor of the given name and parameter count.
     *
     * @param className - lookup name of the class, see {@link #getClass(String)}.
     * @param params    - the expected parameters.
     * @return An object that invokes this constructor.
     * @throws IllegalStateException If we cannot find this method.
     */
    public static ConstructorInvoker<?> getConstructor(String className, Class<?>... params) {
        return getConstructor(getClass(className), params);
    }

    /**
     * Search for the first publically and privately defined constructor of the given name and parameter count.
     *
     * @param clazz  - a class to start with.
     * @param params - the expected parameters.
     * @return An object that invokes this constructor.
     * @throws IllegalStateException If we cannot find this method.
     */
    public static <T> ConstructorInvoker<?> getConstructor(Class<T> clazz, Class<?>... params) {
        for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (arrayEqualsIgnorePrimitives(constructor.getParameterTypes(), params)) {
                constructor.setAccessible(true);

                return arguments -> {
                    try {
                        return constructor.newInstance(arguments);
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot invoke constructor " + constructor, e);
                    }
                };
            }
        }

        StringBuilder possibleConstructors = new StringBuilder();
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            possibleConstructors.append("\n").append(clazz.getSimpleName()).append("(").append(Arrays.toString(declaredConstructor.getParameterTypes())).append(")");
        }

        throw new IllegalStateException(String.format("Unable to find constructor for %s (%s). \nPossible Constructors: %s", clazz, Arrays.asList(params), possibleConstructors));
    }

    /**
     * Retrieve a class from its full name.
     *
     * @param lookupName - the class name.
     * @return The looked up class.
     * @throws IllegalArgumentException If a variable or class could not be found.
     */
    public static Class<?> getClass(String lookupName) {
        try {
            return Class.forName(lookupName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find " + lookupName, e);
        }
    }

    /**
     * Check if two arrays of classes are equal, ignoring the fact of they being primitives
     * <p>
     * Params:
     * array1 – one array to be tested for equality
     * array2 – the other array to be tested for equality
     *
     * @return true if the two arrays are equal
     */
    public static boolean arrayEqualsIgnorePrimitives(Class<?>[] array1, Class<?>[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            Class<?> clazz1 = array1[i];
            Class<?> clazz2 = array2[i];

            if (Objects.equals(clazz1, clazz2)) {
                continue; //If both are the same, there are no further checks needed
            }

            if (clazz1.isPrimitive() && clazz2.isPrimitive()) {
                return false; //If both are primitives, there are no further checks needed as well
            }

            if (!clazz1.isPrimitive() && !clazz2.isPrimitive()) {
                return false; //If both are non-primitives, there are no further checks needed as well
            }

            try {
                Object primitive = clazz1.isPrimitive() ? clazz1 : clazz2;
                Object nonPrimitiveInnerType = clazz1.isPrimitive() ? ReflectionUtil.getField(clazz2, "TYPE").get(null) : ReflectionUtil.getField(clazz1, "TYPE").get(null);

                if (Objects.equals(primitive, nonPrimitiveInnerType)) {
                    continue;
                }
            } catch (Exception ignored) {

            }
            return false; //If Exception or inner-primives comparassion fail, they are really not equal!
        }
        return true;
    }


    /**
     * An interface for invoking a specific method.
     *
     * @param <T> - field type.
     */
    public interface MethodInvoker<T> {
        /**
         * Invoke a method on a specific target object.
         *
         * @param target    - the target object, or NULL for a static method.
         * @param arguments - the arguments to pass to the method.
         * @return The return value, or NULL if is void.
         */
        T invoke(Object target, Object... arguments);

        /**
         * @return The return Method of this invoker.
         */
        Method get();
    }

    public interface ConstructorInvoker<T> {
        /**
         * Invoke a constructor for a specific class.
         *
         * @param arguments - the arguments to pass to the constructor.
         * @return The constructed object.
         */
        T invoke(Object... arguments);
    }

    /**
     * An interface for retrieving the field content.
     *
     * @param <T> - field type.
     */
    public interface FieldAccessor<T> {
        /**
         * Retrieve the content of a field.
         *
         * @param target - the target object, or NULL for a static field.
         * @return The value of the field.
         */
        T get(Object target);

        /**
         * Set the content of a field.
         *
         * @param target - the target object, or NULL for a static field.
         * @param value  - the new value of the field.
         */
        void set(Object target, Object value);

        /**
         * Determine if the given object has this field.
         *
         * @param target - the object to test.
         * @return TRUE if it does, FALSE otherwise.
         */
        boolean hasField(Object target);

        /**
         * Get the target Field
         *
         * @return return the target Field
         */
        Field getTheField();
    }
}



