package dk.diku.pcsd.assignment3.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import dk.diku.pcsd.keyvaluebase.interfaces.ValueSerializer;

public class ValueSerializerImpl implements ValueSerializer<ValueListImpl> {
	
	// type markers
	private static final byte TYPE_BYTE = 0,
			TYPE_BOOLEAN = 1,
			TYPE_CHARACTER = 2,
			TYPE_SHORT = 3,
			TYPE_INTEGER = 4,
			TYPE_LONG = 5,
			TYPE_FLOAT = 6,
			TYPE_DOUBLE = 7,
			TYPE_STRING = 8;

	@Override
	public ValueListImpl fromByteArray(byte[] array) throws IOException {
		ValueListImpl valueList = new ValueListImpl();
		ByteBuffer byteBuffer = ByteBuffer.wrap(array);
		while(byteBuffer.remaining() > 0)
			valueList.add(new ValueImpl(read(byteBuffer)));
		return valueList;
	}

	@Override
	public byte[] toByteArray(ValueListImpl v) throws IOException {
		int capacity = 0;
		List<ValueImpl> values = v.getValueList();
		for(ValueImpl value : values)
			capacity += getSerializedLength(value.getValue());
		ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
		byteBuffer.clear();
		for(ValueImpl value : values)
			write(value.getValue(), byteBuffer);
		return byteBuffer.array();
	}
	
	private int getSerializedLength(Object value) {
		
		/* always add 1 for the byte marker */
		
		// check for the most common first
		if(value instanceof Integer)
			return 4 + 1;
		if(value instanceof String)
			return ((String) value).getBytes().length + 1 + 4; // + length field
		
		// then the rest
		if(value instanceof Byte)
			return 1 + 1;
		if(value instanceof Boolean)
			return 1 + 1;
		if(value instanceof Character)
			return 2 + 1;
		if(value instanceof Short)
			return 2 + 1;
		if(value instanceof Long)
			return 8 + 1;
		if(value instanceof Float)
			return 4 + 1;
		if(value instanceof Double)
			return 8 + 1;
		
		// otherwise use string representation
		return ((String) value).toString().getBytes().length + 1 + 4; // + length field
	}
	
	private void write(Object value, ByteBuffer dst) {
		// check for the most common first
		if(value instanceof Integer)
			dst.put(TYPE_INTEGER).putInt((Integer) value);
		else if(value instanceof String) {
			byte[] bytes = ((String) value).getBytes();
			dst.put(TYPE_STRING).putInt(bytes.length).put(bytes);
		}
		
		// then the rest
		else if(value instanceof Byte)
			dst.put(TYPE_BYTE).put((Byte) value);
		else if(value instanceof Boolean)
			dst.put(TYPE_BOOLEAN).put((byte) (((Boolean) value) ? 1 : 0));
		else if(value instanceof Character)
			dst.put(TYPE_CHARACTER).putChar((Character) value);
		else if(value instanceof Short)
			dst.put(TYPE_SHORT).putShort((Short) value);
		else if(value instanceof Long)
			dst.put(TYPE_LONG).putLong((Long) value);
		else if(value instanceof Float)
			dst.put(TYPE_FLOAT).putFloat((Float) value);
		else if(value instanceof Double)
			dst.put(TYPE_DOUBLE).putDouble((Double) value);
		
		// otherwise use string representation
		else {
			byte[] bytes = value.toString().getBytes();
			dst.put(TYPE_STRING).putInt(bytes.length).put(bytes);
		}
	}
	
	private Object read(ByteBuffer src) {
		byte type = src.get();
		switch(type) {
			// check for the most common first
			case TYPE_INTEGER: return src.getInt();
			case TYPE_STRING:
				byte[] bytes = new byte[src.getInt()];
				src.get(bytes);
				return new String(bytes);

			// then the rest
			case TYPE_BYTE: return src.get();
			case TYPE_BOOLEAN: return src.get() == 0 ? false : true;
			case TYPE_CHARACTER: return src.getChar();
			case TYPE_SHORT: return src.getShort();
			case TYPE_LONG: return src.getLong();
			case TYPE_FLOAT: return src.getFloat();
			case TYPE_DOUBLE: return src.getDouble();
			default: throw new IllegalArgumentException("Invalid type: " + type);
		}
	}

}
