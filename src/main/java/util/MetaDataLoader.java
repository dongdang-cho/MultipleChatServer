package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MetaDataLoader {
    private static Properties metaDataProfile = new Properties();
    static {
        try {
            metaDataProfile.load(new FileInputStream("meta_data.properties"));
            MariaDBConnector.dbLoading(metaDataProfile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.err.println("메타 데이터 파일이 존재하지 않습니다.");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Properties getMetaDataProfile() {
        return metaDataProfile;
    }

    public static int getPort() {
        return Integer.parseInt(metaDataProfile.getProperty("port"));
    }
}
