package com.bigyj.utils;

import java.util.Arrays;

import com.bigyj.breaker.manager.BreakerStateManager;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * ProtoStuff序列化与反序列化
 */
public class ProtoStuffUtil {
	/**
	 * 序列化
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public static <T> byte[] serialize(T obj) {
		if (obj == null) {
			throw new RuntimeException("Failed to serializer");
		} else {
			Class<T> clz = (Class<T>) obj.getClass();
			Schema<T> schema = RuntimeSchema.getSchema(clz);
			LinkedBuffer buffer = LinkedBuffer.allocate(1048576);

			byte[] protoStuff;
			try {
				protoStuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
			} catch (Exception var8) {
				throw new RuntimeException("Failed to serializer");
			} finally {
				buffer.clear();
			}

			return protoStuff;
		}
	}
	/**
	 * 反序列化
	 */
	public static <T> T deserialize(byte[] paramArrayOfByte, Class<T> targetClass) {
		if (paramArrayOfByte != null && paramArrayOfByte.length != 0) {
			Schema<T> schema = RuntimeSchema.getSchema(targetClass);
			T t = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(paramArrayOfByte, t, schema);
			return t;
		} else {
			throw new RuntimeException("Failed to deserialize");
		}
	}

	public static void main(String[] args) {
		BreakerStateManager breakerStateManager = new BreakerStateManager(0,0,0,0,0, 0,0);

		//使用ProtostuffUtils序列化
		byte[] data = ProtoStuffUtil.serialize(breakerStateManager);
		System.out.println("序列化后：" + Arrays.toString(data));
		BreakerStateManager result = ProtoStuffUtil.deserialize(data, BreakerStateManager.class);
		System.out.println("反序列化后：" + result.toString());
	}
}
