package hu.dominikvaradi.sociallybackend.flows.test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestUser {
	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;
	private String country;
	private String city;
	private String password;
}
