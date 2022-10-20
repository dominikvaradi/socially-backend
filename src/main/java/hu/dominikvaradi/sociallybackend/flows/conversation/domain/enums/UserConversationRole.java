package hu.dominikvaradi.sociallybackend.flows.conversation.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum UserConversationRole {
	NORMAL, ADMIN
}
