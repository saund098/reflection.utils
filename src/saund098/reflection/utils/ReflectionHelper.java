package saund098.reflection.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class should be used with caution. Providing the wrong mapping in the
 * constructor can result in ClassCastExceptions. Using a constructor that
 * requires an argument of the same class being constructed results in an
 * IllegalArgumentException to prevent an infinite loop condition.
 * 
 * @author saund098
 */
public class ReflectionHelper {

	/**
	 * 
	 * @param pDefaultValues
	 *            mapping of default values to use
	 * @param pDefaultConstructors
	 *            mapping of default class constructors to use
	 * @param pUseRandomConstructor
	 *            flag specifying the condition for using random constructors or
	 *            not. When this is false, any classes without specified
	 *            constructors will be assigned a null value. When this is true,
	 *            a random constructor will be selected and populated through
	 *            reflection, possibly leading to exceptions.
	 */
	public ReflectionHelper(Map<Class<?>, Object> pDefaultValues,
			Map<Class<?>, Constructor<?>> pDefaultConstructors,
			boolean pUseRandomConstructor) {
		mDefaultValues.putAll(DEFAULT_PRIMITIVE_VALUES);

		mDefaultConstructors.putAll(DEFAULT_CONSTRUCTORS);
		if (pDefaultValues != null) {
			mDefaultValues.putAll(pDefaultValues);
		}
		if (pDefaultConstructors != null) {
			mDefaultConstructors.putAll(pDefaultConstructors);
		}
		mUseRandomConstructor = pUseRandomConstructor;
	}

	@SuppressWarnings("unchecked")
	public <T> T reflectClass(Class<T> pClass) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (pClass != null) {
			if (mDefaultValues.containsKey(pClass)) {
				return (T) mDefaultValues.get(pClass);
			} else if (mDefaultConstructors.containsKey(pClass)) {
				return (T) reflectFromConstructor(mDefaultConstructors
						.get(pClass));
			} else if (mUseRandomConstructor) {
				Constructor<?>[] constructors = pClass.getConstructors();
				if (constructors.length >= 1) {
					return (T) reflectFromConstructor(constructors[mRand
							.nextInt(constructors.length)]);
				}
			}
		}
		return null;
	}

	public <T> T reflectFromConstructor(Constructor<T> pConstructor)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Class<?> constructorClass = pConstructor.getDeclaringClass();
		Class<?>[] parameters = (Class<?>[]) pConstructor.getParameterTypes();
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			Class<?> parameter = parameters[i];
			if (parameter.equals(constructorClass)) {
				throw new IllegalArgumentException(
						"Provided class constructor requires parameter of the same type which would result in an infinite loop condition");
			}
			args[i] = reflectClass(parameters[i]);
		}
		return pConstructor.newInstance(args);
	}

	private final Map<Class<?>, Object> mDefaultValues = new HashMap<Class<?>, Object>();
	private final Map<Class<?>, Constructor<?>> mDefaultConstructors = new HashMap<Class<?>, Constructor<?>>();
	private final boolean mUseRandomConstructor;

	private static final Random mRand = new Random();
	private static final Map<Class<?>, Object> DEFAULT_PRIMITIVE_VALUES = new HashMap<Class<?>, Object>();
	private static final Map<Class<?>, Constructor<?>> DEFAULT_CONSTRUCTORS = new HashMap<Class<?>, Constructor<?>>();
	static {
		DEFAULT_PRIMITIVE_VALUES.put(Integer.TYPE, Integer.valueOf(0));
		DEFAULT_PRIMITIVE_VALUES.put(Long.TYPE, Long.valueOf((long) 0));
		DEFAULT_PRIMITIVE_VALUES.put(Double.TYPE, Double.valueOf((double) 0));
		DEFAULT_PRIMITIVE_VALUES.put(Float.TYPE, Float.valueOf((float) 0));
		DEFAULT_PRIMITIVE_VALUES.put(Boolean.TYPE, Boolean.valueOf(false));
		DEFAULT_PRIMITIVE_VALUES.put(Character.TYPE, Character.valueOf('\0'));
		DEFAULT_PRIMITIVE_VALUES.put(Byte.TYPE, Byte.valueOf((byte) 0));
		DEFAULT_PRIMITIVE_VALUES.put(Short.TYPE, Short.valueOf((short) 0));
		DEFAULT_PRIMITIVE_VALUES.put(Void.TYPE, null);

		try {
			/**
			 * The following constructors are used since the behavior of
			 * constructors with String parameter is undefined or exception
			 * causing
			 */
			DEFAULT_CONSTRUCTORS.put(Integer.class,
					Integer.class.getConstructor(Integer.TYPE));
			DEFAULT_CONSTRUCTORS.put(Long.class,
					Long.class.getConstructor(Long.TYPE));
			DEFAULT_CONSTRUCTORS.put(Double.class,
					Double.class.getConstructor(Double.TYPE));
			DEFAULT_CONSTRUCTORS.put(Float.class,
					Float.class.getConstructor(Float.TYPE));
			DEFAULT_CONSTRUCTORS.put(Boolean.class,
					Boolean.class.getConstructor(Boolean.TYPE));
			DEFAULT_CONSTRUCTORS.put(Character.class,
					Character.class.getConstructor(Character.TYPE));
			DEFAULT_CONSTRUCTORS.put(Byte.class,
					Byte.class.getConstructor(Byte.TYPE));
			DEFAULT_CONSTRUCTORS.put(Short.class,
					Short.class.getConstructor(Short.TYPE));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final Map<Class<?>, Object> defaultValues = new HashMap<Class<?>, Object>();
			final Map<Class<?>, Constructor<?>> defaultConstructors = new HashMap<Class<?>, Constructor<?>>();
			defaultValues.put(String.class, "Hello World!");
			ReflectionHelper reflectionHelper = new ReflectionHelper(
					defaultValues, defaultConstructors, true);
			TestClass testClass = reflectionHelper
					.<TestClass> reflectClass(TestClass.class);
			System.out.println(testClass.toString());
			// IllegalTestClass illegalTestClass = reflectionHelper
			// .<IllegalTestClass> reflectClass(IllegalTestClass.class);
			// System.out.println(illegalTestClass.toString());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
