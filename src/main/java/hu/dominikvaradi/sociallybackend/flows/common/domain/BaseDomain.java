package hu.dominikvaradi.sociallybackend.flows.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseDomain implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "public_id", nullable = false, unique = true)
	private UUID publicId = UUID.randomUUID();

	@CreationTimestamp
	@Column(name = "created", nullable = false)
	private LocalDateTime created;

	@UpdateTimestamp
	@Column(name = "updated", nullable = false)
	private LocalDateTime updated;

	@Version
	@Column(name = "version", nullable = false)
	private Long version;
}
