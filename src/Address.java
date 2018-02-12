package com.example.android.basicgesturedetect;

/**
 * Created by 462904 on 07/06/2016.
 */
// Simple class to represents addresses in the address book
public class Address {

    public String Address1 = "";
    public String Address2 = "";
    public String Town = "";
    public String County = "";
    public String Postcode = "";
    public String Lat;
    public String Long;

    public Address(String address1, String address2, String town, String county, String postcode, String latitude, String longitude) {
        this.Address1 = address1;
        this.Address2 = address2;
        this.Town = town;
        this.County = county;
        this.Postcode = postcode;
        this.Lat = latitude;
        this.Long = longitude;
    }


}
