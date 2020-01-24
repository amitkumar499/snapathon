package helpers;

public class DockerConfig {

    final float serverScale;

    String mysqlVersion;

    //-Xms512M
    String javaMinHeapSize;

    //-Xmx1024M
    String javaMaxHeapSize;

    //-XX:MaxPermSize=256M
    String javaMaxPermSize;

    //-XX:PermSize=256M
    String javaInitPermSize;


    public DockerConfig(float scale) {
        serverScale = scale;
    }
}
