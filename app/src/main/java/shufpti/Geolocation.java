package shufpti;

import com.google.gson.annotations.SerializedName;

public class Geolocation {

    @SerializedName("host")
    String host;

    @SerializedName("ip")
    String ip;

    @SerializedName("rdns")
    String rdns;

    @SerializedName("asn")
    String asn;

    @SerializedName("isp")
    String isp;

    @SerializedName("country_name")
    String countryName;

    @SerializedName("country_code")
    String countryCode;

    @SerializedName("region_name")
    String regionName;

    @SerializedName("region_code")
    String regionCode;

    @SerializedName("city")
    String city;

    @SerializedName("postal_code")
    String postalCode;

    @SerializedName("continent_name")
    String continentName;

    @SerializedName("continent_code")
    String continentCode;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("metro_code")
    String metroCode;

    @SerializedName("timezone")
    String timezone;

    @SerializedName("ip_type")
    String ipType;

    @SerializedName("capital")
    String capital;

    @SerializedName("currency")
    String currency;


    public void setHost(String host) {
        this.host = host;
    }
    public String getHost() {
        return host;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return ip;
    }

    public void setRdns(String rdns) {
        this.rdns = rdns;
    }
    public String getRdns() {
        return rdns;
    }

    public void setAsn(String asn) {
        this.asn = asn;
    }
    public String getAsn() {
        return asn;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
    public String getIsp() {
        return isp;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getCountryName() {
        return countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCountryCode() {
        return countryCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    public String getRegionName() {
        return regionName;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
    public String getRegionCode() {
        return regionCode;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getPostalCode() {
        return postalCode;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }
    public String getContinentName() {
        return continentName;
    }

    public void setContinentCode(String continentCode) {
        this.continentCode = continentCode;
    }
    public String getContinentCode() {
        return continentCode;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLongitude() {
        return longitude;
    }

    public void setMetroCode(String metroCode) {
        this.metroCode = metroCode;
    }
    public String getMetroCode() {
        return metroCode;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getTimezone() {
        return timezone;
    }

    public void setIpType(String ipType) {
        this.ipType = ipType;
    }
    public String getIpType() {
        return ipType;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
    public String getCapital() {
        return capital;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
    }
}
