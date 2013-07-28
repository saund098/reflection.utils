package saund098.reflection.utils;

import java.lang.reflect.Array;
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
	 * @param pArrayLength
	 *            length to use for arrays, a null value or negative length
	 *            results in null arrays, 0 results in empty arrays, 1 results
	 *            in a single element array, and so on.
	 * @param pUseRandomConstructor
	 *            flag specifying the condition for using random constructors or
	 *            not. When this is false, any classes without specified
	 *            constructors will be assigned a null value. When this is true,
	 *            a random constructor will be selected and populated through
	 *            reflection, possibly leading to exceptions.
	 */
	public ReflectionHelper(Map<Class<?>, Object> pDefaultValues,
			Map<Class<?>, Constructor<?>> pDefaultConstructors,
			Integer pArrayLength, boolean pUseRandomConstructor) {
		mDefaultValues.putAll(DEFAULT_PRIMITIVE_VALUES);

		mArrayLength = pArrayLength;

		mUseRandomConstructor = pUseRandomConstructor;
		if (mUseRandomConstructor) {
			mDefaultConstructors.putAll(DEFAULT_WRAPPER_CONSTRUCTORS);
		} else {
			mDefaultValues.putAll(DEFAULT_WRAPPER_VALUES);
		}

		if (pDefaultValues != null) {
			mDefaultValues.putAll(pDefaultValues);
		}
		if (pDefaultConstructors != null) {
			mDefaultConstructors.putAll(pDefaultConstructors);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T reflectClass(Class<T> pClass) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		T obj = null;
		if (pClass != null) {
			if (mDefaultValues.containsKey(pClass)) {
				obj = (T) mDefaultValues.get(pClass);
			} else if (mDefaultConstructors.containsKey(pClass)) {
				obj = (T) reflectFromConstructor(mDefaultConstructors
						.get(pClass));
			} else if (pClass.isArray()) {
				Class<?> component = pClass.getComponentType();
				if (mArrayLength == null || mArrayLength.intValue() < 0) {
					obj = null;
				} else {
					obj = (T) Array.newInstance(component,
							mArrayLength.intValue());
					for (int i = 0; i < Array.getLength(obj); i++) {
						Array.set(obj, i, reflectClass(component));
					}
				}
			} else if (mUseRandomConstructor) {
				Constructor<?>[] constructors = pClass.getConstructors();
				if (constructors.length >= 1) {
					obj = (T) reflectFromConstructor(constructors[RAND
							.nextInt(constructors.length)]);
				}
			}
		}
		return obj;
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
						ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
			}
			args[i] = reflectClass(parameters[i]);
		}
		return pConstructor.newInstance(args);
	}

	private final Map<Class<?>, Object> mDefaultValues = new HashMap<Class<?>, Object>();
	private final Map<Class<?>, Constructor<?>> mDefaultConstructors = new HashMap<Class<?>, Constructor<?>>();
	private final Integer mArrayLength;
	private final boolean mUseRandomConstructor;

	public static final boolean DEFAULT_BOOLEAN = false;
	public static final byte DEFAULT_BYTE = (byte) 0;
	public static final char DEFAULT_CHAR = '\0';
	public static final double DEFAULT_DOUBLE = (double) 0;
	public static final float DEFAULT_FLOAT = (float) 0;
	public static final int DEFAULT_INT = (int) 0;
	public static final long DEFAULT_LONG = (long) 0;
	public static final short DEFAULT_SHORT = (short) 0;

	private static final Random RAND = new Random();
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Provided class constructor requires parameter of the same type which would result in an infinite loop condition";
	private static final Map<Class<?>, Object> DEFAULT_PRIMITIVE_VALUES = new HashMap<Class<?>, Object>();
	private static final Map<Class<?>, Object> DEFAULT_WRAPPER_VALUES = new HashMap<Class<?>, Object>();
	private static final Map<Class<?>, Constructor<?>> DEFAULT_WRAPPER_CONSTRUCTORS = new HashMap<Class<?>, Constructor<?>>();

	static {
		DEFAULT_PRIMITIVE_VALUES.put(Boolean.TYPE, DEFAULT_BOOLEAN);
		DEFAULT_PRIMITIVE_VALUES.put(Byte.TYPE, DEFAULT_BYTE);
		DEFAULT_PRIMITIVE_VALUES.put(Character.TYPE, DEFAULT_CHAR);
		DEFAULT_PRIMITIVE_VALUES.put(Double.TYPE, DEFAULT_DOUBLE);
		DEFAULT_PRIMITIVE_VALUES.put(Float.TYPE, DEFAULT_FLOAT);
		DEFAULT_PRIMITIVE_VALUES.put(Integer.TYPE, DEFAULT_INT);
		DEFAULT_PRIMITIVE_VALUES.put(Long.TYPE, DEFAULT_LONG);
		DEFAULT_PRIMITIVE_VALUES.put(Short.TYPE, DEFAULT_SHORT);

		/**
		 * In the event String is not defined as a number/boolean and random
		 * constructors are not used, these are set to prevent
		 * NumberFormatException and consistent with non-wrapper classes.
		 * 
		 * These default values can be overridden by providing a default value
		 * mapping in the constructor.
		 */
		DEFAULT_WRAPPER_VALUES.put(Boolean.class, null);
		DEFAULT_WRAPPER_VALUES.put(Byte.class, null);
		DEFAULT_WRAPPER_VALUES.put(Character.class, null);
		DEFAULT_WRAPPER_VALUES.put(Double.class, null);
		DEFAULT_WRAPPER_VALUES.put(Float.class, null);
		DEFAULT_WRAPPER_VALUES.put(Integer.class, null);
		DEFAULT_WRAPPER_VALUES.put(Long.class, null);
		DEFAULT_WRAPPER_VALUES.put(Short.class, null);
		DEFAULT_WRAPPER_VALUES.put(Void.class, null);

		try {
			/**
			 * In the event String is not defined as a number/boolean and random
			 * constructors are used, these are set to prevent
			 * NumberFormatExceptions.
			 * 
			 * These default constructors can be overridden by providing a
			 * default value mapping in the constructor.
			 */
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Boolean.class,
					Boolean.class.getConstructor(Boolean.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Byte.class,
					Byte.class.getConstructor(Byte.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Character.class,
					Character.class.getConstructor(Character.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Double.class,
					Double.class.getConstructor(Double.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Float.class,
					Float.class.getConstructor(Float.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Integer.class,
					Integer.class.getConstructor(Integer.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Long.class,
					Long.class.getConstructor(Long.TYPE));
			DEFAULT_WRAPPER_CONSTRUCTORS.put(Short.class,
					Short.class.getConstructor(Short.TYPE));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private static boolean testIllegalTestClass()
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		IllegalTestClass test = new ReflectionHelper(null, null, null, false)
				.<IllegalTestClass> reflectClass(IllegalTestClass.class);
		assert (test == null);
		try {
			new ReflectionHelper(null, null, null, true)
					.<IllegalTestClass> reflectClass(IllegalTestClass.class);
		} catch (IllegalArgumentException e) {
			return e.getMessage().equals(
					ReflectionHelper.ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
		}
		return false;
	}

	/**
	 * @param args
	 * @throws NoSuchMethodException
	 */
	public static void main(String[] args) throws NoSuchMethodException {
		try {
			TestClass test;

			// Test the case with no default values and only a default
			// constructor for the TestClass
			final Map<Class<?>, Constructor<?>> defaultConstructors = new HashMap<Class<?>, Constructor<?>>();
			defaultConstructors.put(TestClass.class,
					TestClass.class.getConstructor(Boolean.class,
							Boolean[].class, Boolean.TYPE, boolean[].class,
							Byte.class, Byte.TYPE, Character.class,
							Character.TYPE, Double.class, Double.TYPE,
							Float.class, Float.TYPE, Integer.class,
							Integer.TYPE, Long.class, Long.TYPE, Short.class,
							Short.TYPE, String.class, Object.class));
			test = new ReflectionHelper(null, defaultConstructors, null, false)
					.<TestClass> reflectClass(TestClass.class);
			assert (test.getBoolean() == null);
			assert (test.getBooleanArray() == null);
			assert (test.getBooleanPrimitive() == ReflectionHelper.DEFAULT_BOOLEAN);
			assert (test.getBooleanPrimitiveArray() == null);
			assert (test.getByte() == null);
			assert (test.getBytePrimitive() == ReflectionHelper.DEFAULT_BYTE);
			assert (test.getCharacter() == null);
			assert (test.getCharPrimitive() == ReflectionHelper.DEFAULT_CHAR);
			assert (test.getDouble() == null);
			assert (test.getDoublePrimitive() == ReflectionHelper.DEFAULT_DOUBLE);
			assert (test.getFloat() == null);
			assert (test.getFloatPrimitive() == ReflectionHelper.DEFAULT_FLOAT);
			assert (test.getInteger() == null);
			assert (test.getIntPrimitive() == ReflectionHelper.DEFAULT_INT);
			assert (test.getLong() == null);
			assert (test.getLongPrimitive() == ReflectionHelper.DEFAULT_LONG);
			assert (test.getShort() == null);
			assert (test.getShortPrimitive() == ReflectionHelper.DEFAULT_SHORT);
			assert (test.getString() == null);
			assert (test.getObject() == null);

			String testString = "Hello World!";
			final Map<Class<?>, Object> defaultValues = new HashMap<Class<?>, Object>();
			defaultValues.put(String.class, testString);

			// Test the case with default string value, default constructor for
			// the TestClass, various sized arrays, and random
			// constructors.
			for (int arrLen = -5; arrLen < 10; arrLen++) {
				test = new ReflectionHelper(defaultValues, defaultConstructors,
						arrLen, true).<TestClass> reflectClass(TestClass.class);
				assert (test.getBoolean().equals(Boolean
						.valueOf(ReflectionHelper.DEFAULT_BOOLEAN)));
				if (arrLen < 0) {
					assert (test.getBooleanArray() == null);
				} else {
					assert (test.getBooleanArray().length == arrLen);
					assert (test.getBooleanArray().getClass()
							.getComponentType() == Boolean.class);
				}
				assert (test.getBooleanPrimitive() == ReflectionHelper.DEFAULT_BOOLEAN);
				if (arrLen < 0) {
					assert (test.getBooleanPrimitiveArray() == null);
				} else {
					assert (test.getBooleanPrimitiveArray().length == arrLen);
					assert (test.getBooleanPrimitiveArray().getClass()
							.getComponentType() == Boolean.TYPE);
				}
				assert (test.getByte().equals(Byte
						.valueOf(ReflectionHelper.DEFAULT_BYTE)));
				assert (test.getBytePrimitive() == ReflectionHelper.DEFAULT_BYTE);
				assert (test.getCharacter().equals(Character
						.valueOf(ReflectionHelper.DEFAULT_CHAR)));
				assert (test.getCharPrimitive() == ReflectionHelper.DEFAULT_CHAR);
				assert (test.getDouble().equals(Double
						.valueOf(ReflectionHelper.DEFAULT_DOUBLE)));
				assert (test.getDoublePrimitive() == ReflectionHelper.DEFAULT_DOUBLE);
				assert (test.getFloat().equals(Float
						.valueOf(ReflectionHelper.DEFAULT_FLOAT)));
				assert (test.getFloatPrimitive() == ReflectionHelper.DEFAULT_FLOAT);
				assert (test.getInteger().equals(Integer
						.valueOf(ReflectionHelper.DEFAULT_INT)));
				assert (test.getIntPrimitive() == ReflectionHelper.DEFAULT_INT);
				assert (test.getLong().equals(Long
						.valueOf(ReflectionHelper.DEFAULT_LONG)));
				assert (test.getLongPrimitive() == ReflectionHelper.DEFAULT_LONG);
				assert (test.getShort().equals(Short
						.valueOf(ReflectionHelper.DEFAULT_SHORT)));
				assert (test.getShortPrimitive() == ReflectionHelper.DEFAULT_SHORT);
				assert (test.getString().equals(testString));
				assert (!test.getObject().equals(new Object()));
			}

			// Validate IllegalTestClass behavior
			assert (testIllegalTestClass());
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
