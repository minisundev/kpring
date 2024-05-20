package kpring.server.adapter.input.rest

import kpring.core.auth.client.AuthClient
import kpring.core.global.dto.response.ApiResponse
import kpring.core.server.dto.ServerInfo
import kpring.core.server.dto.ServerSimpleInfo
import kpring.core.server.dto.request.AddUserAtServerRequest
import kpring.core.server.dto.request.CreateServerRequest
import kpring.server.application.port.input.AddUserAtServerUseCase
import kpring.server.application.port.input.CreateServerUseCase
import kpring.server.application.port.input.DeleteServerUseCase
import kpring.server.application.port.input.GetServerInfoUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/server")
class RestApiServerController(
  val createServerUseCase: CreateServerUseCase,
  val getServerUseCase: GetServerInfoUseCase,
  val addUserAtServerUseCase: AddUserAtServerUseCase,
  val deleteServerUseCase: DeleteServerUseCase,
  val authClient: AuthClient,
) {
  @PostMapping("")
  fun createServer(
    @RequestHeader("Authorization") token: String,
    @RequestBody request: CreateServerRequest,
  ): ResponseEntity<ApiResponse<*>> {
    val userInfo = authClient.getTokenInfo(token).data!!
    val data = createServerUseCase.createServer(request, userInfo.userId)
    return ResponseEntity.ok()
      .body(ApiResponse(data = data))
  }

  @GetMapping()
  fun getServerList(
    @RequestHeader("Authorization") token: String,
  ): ResponseEntity<ApiResponse<List<ServerSimpleInfo>>> {
    val userInfo = authClient.getTokenInfo(token).data!!
    val data = getServerUseCase.getServerList(userInfo.userId)
    return ResponseEntity.ok()
      .body(ApiResponse(data = data))
  }

  @GetMapping("/{serverId}")
  fun getServerInfo(
    @PathVariable serverId: String,
  ): ResponseEntity<ApiResponse<ServerInfo>> {
    val data = getServerUseCase.getServerInfo(serverId)
    return ResponseEntity.ok()
      .body(ApiResponse(data = data))
  }

  @PutMapping("/{serverId}/invitation/{userId}")
  fun inviteUser(
    @PathVariable serverId: String,
    @PathVariable userId: String,
    @RequestHeader("Authorization") token: String,
  ): ResponseEntity<Any> {
    val invitor = authClient.getTokenInfo(token).data!!
    addUserAtServerUseCase.inviteUser(serverId, invitor.userId, userId)
    return ResponseEntity.ok().build()
  }

  @PutMapping("/{serverId}/user")
  fun addUser(
    @PathVariable serverId: String,
    @RequestHeader("Authorization") token: String,
    @RequestBody request: AddUserAtServerRequest,
  ): ResponseEntity<Any> {
    addUserAtServerUseCase.addInvitedUser(serverId, request)
    return ResponseEntity.ok().build()
  }

  @DeleteMapping("/{serverId}")
  fun deleteServer(
    @PathVariable serverId: String,
    @RequestHeader("Authorization") token: String,
  ): ResponseEntity<Any> {
    val userInfo = authClient.getTokenInfo(token).data!!
    deleteServerUseCase.deleteServer(serverId, userInfo.userId)
    return ResponseEntity.ok().build()
  }
}
