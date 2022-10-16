package hu.dominikvaradi.sociallybackend.flows.common.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Setter
public class PageResponseDto<T> {
	@NotNull
	private int totalPages;

	@NotNull
	private long totalElements;

	@NotNull
	private int size;

	@NotNull
	private int numberOfElements;

	@NotNull
	private List<T> elements;

	public static <T> PageResponseDto<T> buildFromPage(Page<T> page) {
		return PageResponseDto.<T>builder()
				.totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements())
				.size(page.getSize())
				.numberOfElements(page.getNumberOfElements())
				.elements(page.getContent())
				.build();
	}
}
