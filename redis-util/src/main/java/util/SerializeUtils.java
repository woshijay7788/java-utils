package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <p> Description: [对象序列化、反序列化] </p>
 *
 * @author chibei
 */
public class SerializeUtils {

    /**
     * <p> Description: [序列化] </p>
     *
     * @author chibei
     */
    public static byte[] serialize(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outo = new ObjectOutputStream(out);
            outo.writeObject(o);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * <p> Description: [反序列化] </p>
     *
     * @author chibei
     */
    public static Object deserialize(byte[] b) {
        ObjectInputStream oin;
        try {
            if (null == b) {
                return null;
            }
            oin = new ObjectInputStream(new ByteArrayInputStream(b));
            try {
                return oin.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
