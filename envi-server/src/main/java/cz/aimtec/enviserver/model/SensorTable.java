package cz.aimtec.enviserver.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="sensors", schema="dbo")
@EnableTransactionManagement
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class SensorTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String sensorUUID;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "min_temperature")
    private Float minTemperature;

    @Column(name = "max_temperature")
    private Float maxTemperature;

    @ManyToMany(mappedBy = "sensorsTable")
    private Set<User> users = new HashSet<>();

    public SensorTable() {
    }

    public SensorTable(String sensorUUID, String name, String location, Float minTemperature, Float maxTemperature) {
        setSensorUUID(sensorUUID);
        setName(name);
        setLocation(location);
        setMinTemperature(minTemperature);
        setMaxTemperature(maxTemperature);
    }

    public void updateFrom(SensorTable measurement, boolean masterKey) {
        if (measurement.getSensorUUID() != null && masterKey == true) {
            setSensorUUID(measurement.getSensorUUID());
        }
        if (measurement.getName() != null) {
            setName(measurement.getName());
        }
        if (measurement.getLocation() != null) {
            setLocation(measurement.getLocation());
        }
        if (measurement.getMinTemperature() != null) {
            setMinTemperature(measurement.getMinTemperature());
        }
        if (measurement.getMaxTemperature() != null) {
            setMaxTemperature(measurement.getMaxTemperature());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorTable)) return false;
        return id != null && id.equals(((SensorTable) o).id);
    }

    @Override
    public int hashCode() {
        return sensorUUID.hashCode();
    }

    public Long getId() {
        return id;
    }

    public String getSensorUUID() {
        return sensorUUID;
    }

    public void setSensorUUID(String sensorUUID) {
        this.sensorUUID = sensorUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
}
