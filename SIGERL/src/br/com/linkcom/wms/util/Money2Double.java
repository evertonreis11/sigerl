package br.com.linkcom.wms.util;

import java.lang.reflect.Type;

import org.granite.messaging.amf.io.convert.Converter;
import org.granite.messaging.amf.io.convert.Converters;
import org.granite.messaging.amf.io.convert.Reverter;
import org.granite.util.ClassUtil;

import br.com.linkcom.neo.types.Money;

public class Money2Double extends Converter implements Reverter {
	
    public Money2Double(Converters converters) {
        super(converters);
    }

    // AMF3Deserialization (Converter)...

    public boolean canConvert(Object value, Type targetType) {
        Class<?> targetClass = ClassUtil.classOfType(targetType);
        return (
            targetClass.isAssignableFrom(Money.class) &&
            (value == null || value instanceof Double || value instanceof Integer)
        );
    }

    public Object convert(Object value, Type targetType) {
    	if (value instanceof Double) {
    		return new Money((Double) value);
    	}
    	if (value instanceof Integer) {
    		return new Money((Integer) value);
    	}
    	return null;
    }

    // AMF3Serialization (Reverter)...

    public boolean canRevert(Object value) {
        return value instanceof Money;
    }

    public Object revert(Object value) {
        return new Double(((Money)value).getValue().doubleValue());
    }
	
}
