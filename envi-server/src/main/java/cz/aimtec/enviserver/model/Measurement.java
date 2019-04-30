package cz.aimtec.enviserver.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Convert;

/**
 * @author dobj
 */

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
@Table(name="measurements", schema="dbo")
@EnableTransactionManagement
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class, 
		property = "id")
public class Measurement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@Column(name = "status")
	@Convert(converter = MeasurementStatusConverter.class)
	MeasurementStatus status;
	
	@Column(name = "modified_on")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.JSON_TIME_FORMAT)
	private Timestamp modifiedOn;

	protected Measurement() {
		setModifiedOn(new Timestamp(System.currentTimeMillis()));
	}

	public Measurement(Timestamp timestamp, String sensorUUID, Float temperature, MeasurementStatus status) {
		this();
		setTimestamp(timestamp);
		setSensorUUID(sensorUUID);
		setTemperature(temperature);
		setStatus(status);		
	}
	
	public void updateFrom(Measurement measurement) {
		if (measurement.getTimestamp() != null) {
			setTimestamp(measurement.getTimestamp());
		}
		if (measurement.getSensorUUID() != null) { 
			setSensorUUID(measurement.getSensorUUID());
		}
		if (measurement.getTemperature() != null) { 
			setTemperature(measurement.getTemperature());
		}
		if (measurement.getStatus() != null) { 
			setStatus(measurement.getStatus());
		}
		setModifiedOn(new Timestamp(System.currentTimeMillis()));
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Measurement )) return false;
        return id != null && id.equals(((Measurement) o).id);
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

	public MeasurementStatus getStatus() {
		return status;
	}

	public void setStatus(MeasurementStatus status) {
		this.status = status;
	}
	
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}