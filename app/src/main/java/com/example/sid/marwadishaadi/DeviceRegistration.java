package com.example.sid.marwadishaadi;


/**
 * Created by Sid on 10-Jul-17.
 */

public class DeviceRegistration {

    private String device_id;
    private String customer_id;

    public DeviceRegistration(){
    }

    public DeviceRegistration(String device_id, String customer_id) {
        this.device_id = device_id;
        this.customer_id = customer_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }


}
