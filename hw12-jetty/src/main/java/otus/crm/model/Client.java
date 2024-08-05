package otus.crm.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Phone> phoneList;

    public Client(String name) {
        this.name = name;
    }

    public Client(String name, Address address, List<Phone> phoneList) {
        this.name = name;
        this.address = address;
        this.phoneList = phoneList;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phoneList) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneList = phoneList;

        if (this.address != null) {
            this.address.setClient(this);
        }

        if (this.phoneList != null) {
            this.phoneList.forEach(phone -> phone.setClient(this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(name, client.name) && Objects.equals(address, client.address) && Objects.equals(phoneList, client.phoneList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Client cloneWithoutRelations() {
        return new Client(this.id, this.name);
    }

    @Override
    public Client clone() {
        List<Phone> clonedPhoneList = this.phoneList != null ?
                this.phoneList.stream().map(Phone::clone).collect(Collectors.toList()) : Collections.emptyList();
        Address clonedAddress = this.address != null ? this.address.clone() : null;
        Client clonedClient = new Client(this.id, this.name, clonedAddress, clonedPhoneList);
        clonedPhoneList.forEach(phone -> phone.setClient(clonedClient));
        if (clonedAddress != null) {
            clonedAddress.setClient(clonedClient);
        }
        return clonedClient;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
