package hu.dominikvaradi.sociallybackend.flows.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@SuperBuilder
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

	@Builder.Default
	@Column(name = "public_id", nullable = false, unique = true)
	private UUID publicId = UUID.randomUUID();

	@CreationTimestamp
	@Column(name = "created", nullable = false)
	private Instant created;

	@UpdateTimestamp
	@Column(name = "updated", nullable = false)
	private Instant updated;

	@Version
	@Column(name = "version", nullable = false)
	private Long version;
}
