package dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Root {
    private BatteryDTO battery;
    private BodyDTO body;
    private CommsDTO comms;
    private DisplayDTO display;
    private FeaturesDTO features;
    private LaunchDTO launch;
    private MainCameraDTO mainCamera;
    private MemoryDTO memory;
    private MiscDTO misc;
    private NetworkDTO network;
    private PlatformDTO platform;
    private SelfieCameraDTO selfieCamera;
    private SoundDTO sound;


    private String release_date;
    private String androidVersion;
    private String storage;
    private String batteryCapacity;
    private String model;
}
