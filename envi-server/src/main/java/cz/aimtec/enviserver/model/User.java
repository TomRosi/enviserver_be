package cz.aimtec.enviserver.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qrozt
 */

@Entity
@Table(name="users", schema="dbo")
@EnableTransactionManagement
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "password")
	private String password;

	@Column(name = "year")
	private int year;

	@Column(name = "role")
	private int role;

	public User() {}

	public User(String username, String fullName, String password, int year, int role) {
		setUsername(username);
		setFullName(fullName);
		setPassword(password);
		setYear(year);
		setRole(role);
	}

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(
			name = "users_sensors",
			joinColumns = { @JoinColumn(name = "users_id") },
			inverseJoinColumns = { @JoinColumn(name = "sensors_id") }
	)
	Set<SensorTable> sensorsTable = new HashSet<>();

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() { return username.hashCode(); }

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
}