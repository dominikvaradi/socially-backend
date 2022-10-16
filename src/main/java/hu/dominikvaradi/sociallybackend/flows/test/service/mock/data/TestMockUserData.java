package hu.dominikvaradi.sociallybackend.flows.test.service.mock.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;

@Getter
@AllArgsConstructor
public class TestMockUserData {
	private final String name;
	private final LocalDate birthDate;
	private final String country;
	private final String city;

	public static List<TestMockUserData> getTestMockUserDataList() {
		return asList(
				new TestMockUserData("Rick Sanchez", LocalDate.of(1952, 11, 11), "United States of America", "Unidentified City"),
				new TestMockUserData("Morty Smith", LocalDate.of(2002, 9, 19), "United States of America", "Unidentified City"),
				new TestMockUserData("Eren Yaeger", LocalDate.of(2007, 3, 30), "Eldia", "Shinganshina District"),
				new TestMockUserData("Mikasa Ackerman", LocalDate.of(2007, 2, 10), "Eldia", "Shinganshina District"),
				new TestMockUserData("Levi Ackerman", LocalDate.of(1991, 12, 25), "Eldia", "Underground City"),
				new TestMockUserData("Naruto Uzumaki", LocalDate.of(2004, 10, 10), "Land of Fire", "Konoha"),
				new TestMockUserData("Hinata Hyuga", LocalDate.of(2004, 12, 27), "Land of Fire", "Konoha"),
				new TestMockUserData("Sakura Haruno", LocalDate.of(2004, 3, 28), "Land of Fire", "Konoha"),
				new TestMockUserData("Itachi Uchiha", LocalDate.of(1998, 6, 9), "Land of Fire", "Konoha"),
				new TestMockUserData("Sasuke Uchiha", LocalDate.of(2004, 7, 23), "Land of Fire", "Konoha"),
				new TestMockUserData("Tanjiro Kamado", LocalDate.of(2006, 7, 14), "Japan", "Okutama"),
				new TestMockUserData("Nezuko Kamado", LocalDate.of(2007, 12, 28), "Japan", "Okutama"),
				new TestMockUserData("Kanao Tsuyuri", LocalDate.of(2006, 6, 19), "Japan", "Mukojima"),
				new TestMockUserData("Zenitsu Agatsuma", LocalDate.of(2006, 9, 3), "Japan", "Shinjuku"),
				new TestMockUserData("Takumi Fujiwara", LocalDate.of(2002, 12, 25), "Japan", "Mountain Akina"),
				new TestMockUserData("Ryosuke Takahashi", LocalDate.of(1999, 12, 25), "Japan", "Mountain Akagi"),
				new TestMockUserData("Mako Sato", LocalDate.of(2000, 12, 25), "Japan", "Mountain Usui")
		);
	}
}
