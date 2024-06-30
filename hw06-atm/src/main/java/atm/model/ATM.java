package atm.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ATM {
    private int id;
    private String location;
    private final Map<Integer, Integer> balance = new HashMap<>();

    public ATM(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Integer> getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATM atm = (ATM) o;
        return Objects.equals(id, atm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
