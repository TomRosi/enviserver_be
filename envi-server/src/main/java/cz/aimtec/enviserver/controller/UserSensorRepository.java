package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.UserSensor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSensorRepository extends CrudRepository<UserSensor, Integer> {

	@Query("SELECT sensorID FROM UserSensor WHERE userID = :teamID")
	List<String> findSensorsIDsByID(@Param("teamID") String teamID);
}
