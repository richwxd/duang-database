package com.duangframework.db.core.converters;

import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.ToolsKit;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Double类型转换器
 *
 * @author Laotang
 */
public class DateConverter extends TypeConverter {

    public DateConverter() {
        super(Date.class);
    }

    protected DateConverter(Class clazz) {
        super(clazz);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {
        if (value == null) {
            return null;
        }

        Class<?> type = field.getType();

        if(DataType.isDate(type)) {
            return new Converter(field, getName(field), (Date)value);
        } else {
            return converterDecodeDataType(field, value);
        }

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        Class<?> type = field.getType();
        Date date = null;
        if(DataType.isDate(type)) {
            date = (Date) value;
        }
        else if (value instanceof String) {
            String stringDate = (String)value;
            try{
                date = ToolsKit.parseDate(stringDate, "yyyy-MM-dd HH:mm:ss");
            } catch(Exception e) {
                try{
                    date = ToolsKit.parseDate(stringDate, "yyyy-MM-dd HH:mm:ss.SSS");
                } catch(Exception e2) {
                    logger.error(e2.getMessage(), e2);
                }
            }
        } else if (value instanceof  Long) {
            date = new Date((Long)value);
        }

        return new Converter(field, getName(field), date);
    }


}
