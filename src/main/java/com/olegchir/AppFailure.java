package com.olegchir;

import java.io.*;
import java.util.function.BiFunction;

public class AppFailure
{
    interface Data extends Serializable {
        public default Integer transform(Integer integer) {
            return integer;
        }
    }

    interface F2<T, U, R> extends Serializable {
        R apply(T t, U u);
    }

    public void run() throws IOException, ClassNotFoundException {
        Data data = new Data(){};

        BiFunction<Data, Integer, Integer> func = Data::transform;
        System.out.println(func.apply(data, 1));

        ByteArrayOutputStream bos = serializeMethodHandle(func);
        BiFunction<Data, Integer, Integer> func2 = deserializeMethodHandle(bos);
        System.out.println(func2.apply(data, 2));
    }

    private BiFunction<Data, Integer, Integer> deserializeMethodHandle(ByteArrayOutputStream bos) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new   ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        return (BiFunction<Data, Integer, Integer>) in.readObject();
    }

    private ByteArrayOutputStream serializeMethodHandle(BiFunction<Data, Integer, Integer> func) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(func);
        return bos;
    }

    public static void main( String[] args ) throws IOException, ClassNotFoundException {
        new AppFailure().run();
    }
}
