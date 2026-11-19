package dto;

import lombok.Data;

@Data
public class CommsDTO {
    private String wlan;
    private String blueTooth;
    private String positioning;
    private String nfc;
    private String radio;
    private String usb;
}
