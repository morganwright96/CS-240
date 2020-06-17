package Service;

import java.util.UUID;

public class UUIDGenerator {
    String uuid;

    public String getUuid() {
        uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
