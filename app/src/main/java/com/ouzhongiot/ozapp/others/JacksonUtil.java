package com.ouzhongiot.ozapp.others;

/**
 * Created by liu on 2016/5/10.
 */
import java.io.File;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonUtil {



    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将对象序列化成JSON字符串
     * @param obj
     * @return
     */
    public static String serializeObjectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象序列化到文件
     * @param obj					要序列化的对象
     * @param file					要写入的文件
     */
    public static void serializeObjectToFile(Object obj, File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, obj);
        } catch (Exception e) {
        }
    }

    /**
     * 从文件读取JSON
     * @param file					来源文件
     * @param clazz					反序列化成的类
     * @return
     */
    public static <T> T deserializeFormFile(File file, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, clazz);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将JSON字符串反序列化成对象
     * @param json					要反序列化JSON字符串
     * @param typeReference			类型帮助类(带泛型类T为List,Map等泛型类)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeJsonToObject(String json, TypeReference<T> typeReference) {
        try {
            return (T) mapper.readValue(json, typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化成对象
     * @param json					要反序列化JSON字符串
     * @param clazz					普通对象类型
     * @return
     */
    public static <T> T deserializeJsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化成对象
     * @param json					要反序列化JSON字符串
     * @param javaType				JavaType表示的对象
     * @return
     */
    public static Object deserializeJsonToObject(String json, JavaType javaType) {
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取List类型的JavaType对象
     * @param clazz					List内部对象类型
     * @return
     */
    public static <T> JavaType getListJavaType(Class<T> clazz) {
        TypeFactory typeFactory = mapper.getTypeFactory();

		/*JavaType[] pt = new JavaType[] { typeFactory.constructType(clazz) };
		JavaType subtype = typeFactory.constructSimpleType(List.class, List.class, pt);
        JavaType[] collectionParams = typeFactory.findTypeParameters(subtype, Collection.class);
        if (collectionParams.length != 1) {
            throw new IllegalArgumentException(
            		"Could not find 1 type parameter for Collection class list (found " + collectionParams.length + ")");
        }
        JavaType javaType = typeFactory.constructCollectionType(List.class, collectionParams[0]);*/

        JavaType javaType = typeFactory.constructCollectionType(List.class, typeFactory.constructType(clazz));
        return javaType;
    }

    /**
     * 将JSON字符串反序列化成List
     * @param json					JSON字符串
     * @param clazz					List内部类型
     * @return
     */
    public static <T> List<T> deserializeJsonToList(String json, Class<T> clazz) {
        JavaType javaType = getListJavaType(clazz);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取Map类型的JavaType对象
     * @param clazzKey				Map key Type
     * @param clazzValue			Map value Type
     * @return						Map类型的JavaType对象
     */
    public static <K, V> JavaType getMapJavaType(Class<K> clazzKey, Class<V> clazzValue) {
        TypeFactory typeFactory = mapper.getTypeFactory();

		/*JavaType[] pt = new JavaType[] { typeFactory.constructType(clazzKey), typeFactory.constructType(clazzValue) };
		JavaType subtype = typeFactory.constructSimpleType(Map.class, Map.class, pt);
		JavaType[] mapParams = typeFactory.findTypeParameters(subtype, Map.class);
        if (mapParams.length != 2) {
            throw new IllegalArgumentException(
            		"Could not find 2 type parameter for Map class map (found " + mapParams.length + ")");
        }
        JavaType mapType = typeFactory.constructMapType(Map.class, mapParams[0], mapParams[1]);*/

        JavaType mapType = typeFactory.constructMapType(Map.class,
                typeFactory.constructType(clazzKey), typeFactory.constructType(clazzValue));
        return mapType;
    }

    /**
     * 将JSON字符串反序列化成Map
     * @param <K>
     * @param <V>
     * @param json					JSON字符串
     * @param clazzKey				Map key Type
     * @param clazzValue			Map value Type
     * @return						Map<K,V>对象
     */
    public static <K, V> Map<K, V> deserializeJsonToMap(String json, Class<K> clazzKey, Class<V> clazzValue) {
        JavaType javaType = getMapJavaType(clazzKey, clazzValue);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将JSON字符串反序列化成List<Map>
     * @param json					JSON字符串
     * @param clazzKey				key Type
     * @param clazzValue			value Type
     * @return
     */
    public static <K, V> List<Map<K, V>> deserializeJsonToListMap(String json, Class<K> clazzKey, Class<V> clazzValue) {
        JavaType mapType = getMapJavaType(clazzKey, clazzValue);
        TypeFactory typeFactory = mapper.getTypeFactory();

		/*JavaType[] javaTypes = new JavaType[] { mapType };
		JavaType subType = typeFactory.constructSimpleType(List.class, List.class, javaTypes);
        JavaType[] collectionParams = typeFactory.findTypeParameters(subType, Collection.class);
        if (collectionParams.length != 1) {
            throw new IllegalArgumentException(
            		"Could not find 1 type parameter for Collection class list (found " + collectionParams.length + ")");
        }
        JavaType javaType = typeFactory.constructCollectionType(List.class, collectionParams[0]);*/

        JavaType javaType = typeFactory.constructCollectionType(List.class, mapType);
        try {
            return mapper.readValue(json, javaType);
        } catch (Exception e) {
            return null;
        }
    }
}