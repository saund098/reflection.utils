package saund098.reflection.utils;
public class TestClass {

	public TestClass(int pIntPrimitive, long pLongPrimitive,
			double pDoublePrimitive, float pFloatPrimitive,
			boolean pBooleanPrimitive, char pCharPrimitive,
			byte pBytePrimitive, short pShortPrimitive, Integer pInteger,
			Long pLong, Double pDouble, Float pFloat, Boolean pBoolean,
			Character pCharacter, Byte pByte, Short pShort, String pString) {
		mIntPrimitive = pIntPrimitive;
		mLongPrimitive = pLongPrimitive;
		mDoublePrimitive = pDoublePrimitive;
		mFloatPrimitive = pFloatPrimitive;
		mBooleanPrimitive = pBooleanPrimitive;
		mCharPrimitive = pCharPrimitive;
		mBytePrimitive = pBytePrimitive;
		mShortPrimitive = pShortPrimitive;
		mInteger = pInteger;
		mLong = pLong;
		mDouble = pDouble;
		mFloat = pFloat;
		mBoolean = pBoolean;
		mCharacter = pCharacter;
		mByte = pByte;
		mShort = pShort;
		mString = pString;
	}

	public int getIntPrimitive() {
		return mIntPrimitive;
	}

	public long getLongPrimitive() {
		return mLongPrimitive;
	}

	public double getDoublePrimitive() {
		return mDoublePrimitive;
	}

	public float getFloatPrimitive() {
		return mFloatPrimitive;
	}

	public boolean isBooleanPrimitive() {
		return mBooleanPrimitive;
	}

	public char getCharPrimitive() {
		return mCharPrimitive;
	}

	public byte getBytePrimitive() {
		return mBytePrimitive;
	}

	public short getShortPrimitive() {
		return mShortPrimitive;
	}

	public Integer getInteger() {
		return mInteger;
	}

	public Long getLong() {
		return mLong;
	}

	public Double getDouble() {
		return mDouble;
	}

	public Float getFloat() {
		return mFloat;
	}

	public Boolean getBoolean() {
		return mBoolean;
	}

	public Character getCharacter() {
		return mCharacter;
	}

	public Byte getByte() {
		return mByte;
	}

	public Short getShort() {
		return mShort;
	}

	public String getString() {
		return mString;
	}

	private final int mIntPrimitive;
	private final long mLongPrimitive;
	private final double mDoublePrimitive;
	private final float mFloatPrimitive;
	private final boolean mBooleanPrimitive;
	private final char mCharPrimitive;
	private final byte mBytePrimitive;
	private final short mShortPrimitive;
	private final Integer mInteger;
	private final Long mLong;
	private final Double mDouble;
	private final Float mFloat;
	private final Boolean mBoolean;
	private final Character mCharacter;
	private final Byte mByte;
	private final Short mShort;
	private final String mString;
}
