package main.data;

import java.io.Serializable;
import java.util.Objects;

public class PhoneBookRecord implements Serializable {
    private String name;
    private String phoneNumber;

    public PhoneBookRecord(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneBookRecord record)) return false;
        return name.equals(record.name) && phoneNumber.equals(record.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber);
    }

    public String serialize() {
        return name + "," + phoneNumber;
    }

    public static PhoneBookRecord deserialize(String serializedObj) {
        String[] arr = serializedObj.split(",");
        if(arr.length != 2) {
            throw new RuntimeException("Wrong object");
        }
        return new PhoneBookRecord(arr[0], arr[1]);
    }
}
