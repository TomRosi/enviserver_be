package cz.aimtec.enviserver.model;

import java.sql.Timestamp;

/**
 * @author qhlaj
 */

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import cz.aimtec.enviserver.common.Constants;

@Entity
@Table(name="alerts", schema="dbo")
@EnableTransactionManagement
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "created_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.JSON_TIME_FORMAT)
	private Timestamp createdOn;

	@Column(name = "sensor_uuid")	
	private String sensorUUID;
	
	@Column(name = "temperature")	
	private Float temperature;
	
	@Column(name = "low_temperature")	
	private Float lowTemperature;
		
	@Column(name = "high_temperature")	
	private Float highTemperature;
	
	@Column(name = "modified_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.JSON_TIME_FORMAT)
	private Timestamp modifiedOn;

	protected Alert() {
		setModifiedOn(new Timestamp(System.currentTimeMillis()));
	}

	public Alert(Timestamp timestamp, String sensorUUID, Float temperature, Float highTemperature, Float lowTemperature) {
		this();
		setTimestamp(timestamp);
		setSensorUUID(sensorUUID);
		setTemperature(temperature);
		setLowTemperature(lowTemperature);
		setHighTemperature(highTemperature);
	}
	
	public void updateFrom(Alert measurement, boolean masterKey) {
		if (measurement.getTimestamp() != null) {
			setTimestamp(measurement.getTimestamp());
		}
		if (measurement.getSensorUUID() != null && masterKey == true) { 
			setSensorUUID(measurement.getSensorUUID());
		}
		if (measurement.getTemperature() != null) { 
			setTemperature(measurement.getTemperature());
		}
		if (measurement.getHighTemperature() != null) { 
			setHighTemperature(measurement.getHighTemperature());
		}
		if (measurement.getLowTemperature() != null) {
			setLowTemperature(measurement.getLowTemperature());
		}
		setModifiedOn(new Timestamp(System.currentTimeMillis()));
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alert )) return false;
        return id != null && id.equals(((Alert) o).id);
    }
	
    @Override
    public int hashCode() {
        return 31;
    }

	public Long getId() {
		return id;
	}

	public Timestamp getTimestamp() {
		return createdOn;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.createdOn = timestamp;
	}

	public String getSensorUUID() {
		return sensorUUID;
	}

	public void setSensorUUID(String sensorUUID) {
		this.sensorUUID = sensorUUID;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	
	
	public Float getLowTemperature() {
		return lowTemperature;
	}

	public void setLowTemperature(float lowTemperature) {
		this.lowTemperature = lowTemperature;
	}
	
	
	
	public Float getHighTemperature() {
		return highTemperature;
	}

	public void setHighTemperature(float highTemperature) {
		this.highTemperature = highTemperature;
	}


	
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}