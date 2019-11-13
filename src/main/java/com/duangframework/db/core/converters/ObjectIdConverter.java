package com.duangframework.db.core.converters;

import com.duangframework.db.annotation.Id;
import com.duangframework.db.core.DbException;
import com.duangframework.db.core.TypeConverter;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.List;


/**
 * ObjectId类型转换器
 *
 * @author Laotang
 */
public class ObjectIdConverter extends TypeConverter {

    public ObjectIdConverter() {
        super(Id.class, ObjectId.class);
    }

    @Override
    public Converter decode(Field field, Object value) throws DbException {

        if (null == value) {
            return null;
        }

        return new Converter(field, getName(field), String.valueOf(value));

    }

    @Override
    public Converter encode(Field field, Object value) throws DbException {

        if(null == value) {
            return null;
        }

        String idString = String.valueOf(value);
        return new Converter(field, getName(field), (ObjectId.isValid(idString) ? new ObjectId(idString) : null));
    }

}
