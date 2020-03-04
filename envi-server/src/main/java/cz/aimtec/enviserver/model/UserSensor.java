package cz.aimtec.enviserver.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;


@Entity
@Table(name="users_sensors", schema="dbo")
@EnableTransactionManagement
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sensors_id")
    private String sensorID;

    @Column(name = "users_id")
    private String userID;


    public UserSensor(String sensorID, String alertID) {
        setSensorID(sensorID);
        setUserID(userID);
    }

    public void updateFrom(UserSensor measurement, boolean masterKey) {
        if (measurement.getSensorID() != null && masterKey == true) {
            setSensorID(measurement.getSensorID());
        }
        if (measurement.getUserID() != null) {
            setUserID(measurement.getUserID());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSensor)) return false;
        return id != null && id.equals(((UserSensor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public Long getId() {
        return id;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String alertID) {
        this.userID = userID;
    }
}
