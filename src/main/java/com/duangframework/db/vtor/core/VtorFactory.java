package com.duangframework.db.vtor.core;


import com.duangframework.db.annotation.Bean;
import com.duangframework.db.annotation.Entity;

import com.duangframework.db.entity.IdEntity;
import com.duangframework.db.utils.DataType;
import com.duangframework.db.utils.ObjectKit;
import com.duangframework.db.utils.ToolsKit;
import com.duangframework.db.vtor.common.ValidatorException;
import com.duangframework.db.vtor.core.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体类参数验证器
 *
 * @author Laotang
 */
public final class VtorFactory {

	private static final Logger logger = LoggerFactory.getLogger(VtorFactory.class);

	/**实体类对应的字段属性集合*/
	private static final Map<String,Field[]> ENTITY_FIELD_MAP = new HashMap<>();
	/**注册的验证器*/
	private static final Map<Class<?>, AbstractValidatorTemplate> VALIDATOR_HANDLE_MAP = new HashMap<>();
	/**需要进行参数验证的实体类*/
	private static final Set<String> ENTITY_SET = new HashSet<>();

	private static class VtorFactoryHolder {
		private static final VtorFactory INSTANCE = new VtorFactory();
	}
	private VtorFactory() {
		init();
	}
	public static final VtorFactory duang() {
		return VtorFactoryHolder.INSTANCE;
	}

	private void init() {
		if (VALIDATOR_HANDLE_MAP.isEmpty()) {
			List<AbstractValidatorTemplate> validatorHandleList = new ArrayList<AbstractValidatorTemplate>(){{
				add(new DuangIdValidator());
				add(new EmailValidator());
				add(new LengthValidator());
				add(new MaxValidator());
				add(new MinValidator());
				add(new NotEmptyValidator());
				add(new PatternValidator());
				add(new PhoneValidator());
				add(new RangeValidator());
				add(new SafeHtmlValidator());
				add(new UrlValidator());
				add(new YmdValidator());
			}};
			for(AbstractValidatorTemplate validatorTemplate : validatorHandleList) {
				VALIDATOR_HANDLE_MAP.put(validatorTemplate.annotationClass(), validatorTemplate);
			}
		}
	}

	/**
	 * 按单个注解进行验证
	 * @param annotationType		注解类型
	 * @param parameterType			参数类型
	 * @param paramName				参数名称
	 * @param paramValue				参数值
	 * @throws ValidatorException
	 */
	public void validator(Annotation annotationType,  Class<?> parameterType,  String paramName, Object paramValue) throws ValidatorException {
		Class<? extends Annotation> annotationClass = annotationType.annotationType();
		if(ToolsKit.isNotEmpty(VALIDATOR_HANDLE_MAP) &&VALIDATOR_HANDLE_MAP.containsKey(annotationClass)) {
			VALIDATOR_HANDLE_MAP.get(annotationClass).vaildator(annotationType, parameterType, paramName, paramValue);
		}
	}

	/**
	 * List, Set集合验证
	 * 集合里的元素必须实现了java.io.Serializable接口 且  设置了@Bean注解
	 * @param beanCollections
	 * @throws ValidatorException
	 */
	public void validator(Collection<Object> beanCollections) throws ValidatorException {
		if(ToolsKit.isEmpty(beanCollections)) {
			logger.warn("collection is null, so exit validator!");
			return;
		}
		boolean isValidator = false;
		for(Object item : beanCollections) {
			if(isValidatorObj(item)) {
				isValidator = true;
				validator(item);
			}
		}
		if(!isValidator) loggerInfo();
	}

	/**
	 * Map集合验证
	 * 集合里的元素value值必须实现了java.io.Serializable接口 且  设置了@VtorBean注解
	 * @param beanMap
	 * @throws ValidatorException
	 */
	public void validator(Map<String, Object> beanMap) throws ValidatorException {
		if(ToolsKit.isEmpty(beanMap)) {
			logger.warn("map is null, so exit validator!");
			return;
		}
		boolean isValidator = false;
		for(Iterator<Map.Entry<String, Object>> iterator = beanMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> entry = iterator.next();
			Object item = entry.getValue();
			if(isValidatorObj(item)) {
				isValidator =true;
				validator(item);
			}
		}
		if(!isValidator) loggerInfo();
	}

	private void loggerInfo() {
		logger.warn("框架并没进行注解验证，请注意对象或集合元素是否实现[ java.io.Serializable ]接口或设置了@Bean或@Entity注解");
	}

	private boolean isValidatorObj(Object item) {
		return item instanceof  Serializable || item.getClass().isAnnotationPresent(Bean.class) || item.getClass().isAnnotationPresent(Entity.class);
	}

    /**
     * 验证bean
     * @param bean
     * @throws ValidatorException
     */
    public void validator(Object bean) throws ValidatorException {
        if (ToolsKit.isEmpty(bean)) {
            logger.info("要检验的bean对象为空，退出检验");
            return;
        }
        String beanName = bean.getClass().getName();
        Field[] fields = ENTITY_FIELD_MAP.get(beanName);
        if( null == fields){
            fields = bean.getClass().getDeclaredFields();
        }

		// 如果实体类没有设置参数验证的注解，则退出验证
		boolean isValidatorBean = ENTITY_SET.contains(beanName) ? true : isNeedValidator(beanName, fields);
		if(!isValidatorBean){
			return;
		}

        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            Class<?> typeClass = field.getType();
			if(DataType.isListType(typeClass) || DataType.isSetType(typeClass)) {
				Collection collection = (Collection)ObjectKit.getFieldValue(bean, field);
				validator(collection);
				isValidatorBean = true;
			}
			else if(DataType.isMapType(typeClass)) {
				Map map = (Map)ObjectKit.getFieldValue(bean, field);
				validator(map);
				isValidatorBean = true;
            } else if (DataType.isBeanType(typeClass)) {
                Object entityObj = ObjectKit.getFieldValue(bean, field);
                validator(entityObj);
            } else {
                Annotation[] annotationArray = field.getAnnotations();
                if (ToolsKit.isEmpty(annotationArray)) {
					continue;
				}
				for (Annotation annotation : annotationArray) {
					// 如果在验证处理器集合包含了该验证注解则进行验证，并将该bena添加到map缓存，以便再次使用时直接取出字段属性进行验证
					if (VALIDATOR_HANDLE_MAP.containsKey(annotation.annotationType())) {
						Object fieldValue = ObjectKit.getFieldValue(bean, field);
						Class<?> fieldType = field.getType();
						String fieldName = field.getName();
						validator(annotation, fieldType, fieldName, fieldValue);
						isValidatorBean = true;
					} else {
						logger.debug("该验证处理器["+annotation.annotationType()+"]不存在，请检查");
					}
				}
			}
        }

        // 添加到集合
        if(isValidatorBean){
            ENTITY_FIELD_MAP.put(beanName, fields);
        } else {
        	loggerInfo();
		}
    }

	/**
	 * 是否需要进行参数验证
	 * @param beanName
	 * @param fields
	 * @return
	 */
	private boolean isNeedValidator(String beanName, Field[] fields) {
    	for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			if(null != annotations && (annotations.length > 0)) {
				for (Annotation annotation : annotations) {
                    if (VALIDATOR_HANDLE_MAP.containsKey(annotation.annotationType())) {
                        ENTITY_SET.add(beanName);
                        return true;
					}
				}
			}
		}
    	return false;
	}
}
