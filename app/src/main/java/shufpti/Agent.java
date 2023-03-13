package shufpti;

import com.google.gson.annotations.SerializedName;

public class Agent {

    @SerializedName("is_desktop")
    boolean isDesktop;

    @SerializedName("is_phone")
    boolean isPhone;

    @SerializedName("useragent")
    String useragent;

    @SerializedName("device_name")
    String deviceName;

    @SerializedName("browser_name")
    String browserName;

    @SerializedName("platform_name")
    String platformName;


    public void setIsDesktop(boolean isDesktop) {
        this.isDesktop = isDesktop;
    }
    public boolean getIsDesktop() {
        return isDesktop;
    }

    public void setIsPhone(boolean isPhone) {
        this.isPhone = isPhone;
    }
    public boolean getIsPhone() {
        return isPhone;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }
    public String getUseragent() {
        return useragent;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }
    public String getBrowserName() {
        return browserName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
    public String getPlatformName() {
        return platformName;
    }
}
