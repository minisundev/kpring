package kpring.chat.chat.api.v1

import kpring.chat.chat.service.ChatService
import kpring.chat.global.exception.ErrorCode
import kpring.chat.global.exception.GlobalException
import kpring.core.auth.client.AuthClient
import kpring.core.chat.chat.dto.request.CreateChatRequest
import kpring.core.chat.chat.dto.request.UpdateChatRequest
import kpring.core.chat.model.ChatType
import kpring.core.server.client.ServerClient
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated

@Controller
@RequiredArgsConstructor
class WebSocketChatController(
  private val chatService: ChatService,
  private val authClient: AuthClient,
  private val serverClient: ServerClient,
  private val simpMessagingTemplate: SimpMessagingTemplate,
) {
  private val logger: Logger = LoggerFactory.getLogger(WebSocketChatController::class.java)

  @MessageMapping("/chat/create")
  fun createChat(
    @Payload @Validated request: CreateChatRequest,
    headerAccessor: SimpMessageHeaderAccessor,
  ) {
    val token = headerAccessor.getFirstNativeHeader("Authorization") ?: throw GlobalException(ErrorCode.MISSING_TOKEN)
    val userId = authClient.getTokenInfo(token).data!!.userId
    val contextId = request.contextId

    val result =
      when (request.type) {
        ChatType.Room -> chatService.createRoomChat(request, userId)
        ChatType.Server -> chatService.createServerChat(request, userId)
      }
    simpMessagingTemplate.convertAndSend("/topic/chatroom/$contextId", result)
  }

  @MessageMapping("/chat/update")
  fun updateChat(
    @Payload @Validated request: UpdateChatRequest,
    headerAccessor: SimpMessageHeaderAccessor,
  ) {
    val token = headerAccessor.getFirstNativeHeader("Authorization") ?: throw GlobalException(ErrorCode.MISSING_TOKEN)
    val userId = authClient.getTokenInfo(token).data!!.userId
    val contextId = request.contextId

    val result =
      when (request.type) {
        ChatType.Room -> chatService.updateRoomChat(request, userId)
        ChatType.Server -> chatService.updateServerChat(request, userId)
      }
    simpMessagingTemplate.convertAndSend("/topic/chatroom/$contextId", result)
  }
}
