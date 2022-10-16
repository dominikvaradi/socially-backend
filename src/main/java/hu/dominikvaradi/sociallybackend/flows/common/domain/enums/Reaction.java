package hu.dominikvaradi.sociallybackend.flows.common.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum Reaction {
	LIKE, HEART, FUNNY, ANGRY
}
