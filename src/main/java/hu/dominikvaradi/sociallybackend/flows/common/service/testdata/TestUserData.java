package hu.dominikvaradi.sociallybackend.flows.common.service.testdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class TestUserData {
	private final String name;
	private final LocalDate birthDate;
	private final String country;
	private final String city;

	public static List<TestUserData> getTestUserDataList() {
		return Arrays.asList(
				new TestUserData("Rick Sanchez", LocalDate.of(1952, 11, 11), "United States of America", "Unidentified City"),
				new TestUserData("Morty Smith", LocalDate.of(2002, 9, 19), "United States of America", "Unidentified City"),
				new TestUserData("Eren Yaeger", LocalDate.of(2007, 3, 30), "Eldia", "Shinganshina District"),
				new TestUserData("Mikasa Ackerman", LocalDate.of(2007, 2, 10), "Eldia", "Shinganshina District"),
				new TestUserData("Levi Ackerman", LocalDate.of(1991, 12, 25), "Eldia", "Underground City"),
				new TestUserData("Naruto Uzumaki", LocalDate.of(2004, 10, 10), "Land of Fire", "Konoha"),
				new TestUserData("Hinata Hyuga", LocalDate.of(2004, 12, 27), "Land of Fire", "Konoha"),
				new TestUserData("Sakura Haruno", LocalDate.of(2004, 3, 28), "Land of Fire", "Konoha"),
				new TestUserData("Itachi Uchiha", LocalDate.of(1998, 6, 9), "Land of Fire", "Konoha"),
				new TestUserData("Sasuke Uchiha", LocalDate.of(2004, 7, 23), "Land of Fire", "Konoha"),
				new TestUserData("Tanjiro Kamado", LocalDate.of(2006, 7, 14), "Japan", "Okutama"),
				new TestUserData("Nezuko Kamado", LocalDate.of(2007, 12, 28), "Japan", "Okutama"),
				new TestUserData("Kanao Tsuyuri", LocalDate.of(2006, 6, 19), "Japan", "Mukojima"),
				new TestUserData("Zenitsu Agatsuma", LocalDate.of(2006, 9, 3), "Japan", "Shinjuku"),
				new TestUserData("Takumi Fujiwara", LocalDate.of(2002, 12, 25), "Japan", "Mountain Akina"),
				new TestUserData("Ryosuke Takahashi", LocalDate.of(1999, 12, 25), "Japan", "Mountain Akagi"),
				new TestUserData("Mako Sato", LocalDate.of(2000, 12, 25), "Japan", "Mountain Usui")
		);
	}
}
