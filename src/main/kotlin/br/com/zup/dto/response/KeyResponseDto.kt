package br.com.zup.dto.response

import com.google.protobuf.Timestamp

data class KeyResponseDto(val clientId: String, val pixId: String, val createdAt: String) {
}