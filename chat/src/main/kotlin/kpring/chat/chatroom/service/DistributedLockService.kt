package kpring.chat.chatroom.service

import kpring.chat.chatroom.model.DistributedLock
import kpring.chat.chatroom.repository.DistributedLockRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DistributedLockService(
  private val lockRepository: DistributedLockRepository,
) {
  fun acquireLock(
    lockId: String,
    owner: String,
    expireInMillis: Long,
  ): Boolean {
    val now = System.currentTimeMillis()
    val expiresAt = now + expireInMillis

    val optionalLock = lockRepository.findById(lockId)
    return if (optionalLock.isPresent) {
      val lock = optionalLock.get()
      if (lock.expiresAt < now) {
        lockRepository.save(DistributedLock(lockId, owner, expiresAt))
        true
      } else {
        false
      }
    } else {
      lockRepository.save(DistributedLock(lockId, owner, expiresAt))
      true
    }
  }
}