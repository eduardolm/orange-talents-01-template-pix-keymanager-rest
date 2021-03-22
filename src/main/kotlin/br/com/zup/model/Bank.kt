package br.com.zup.model

class Bank(val participant: String, val branch: String, val accountNumber: String, val accountType: String) {

    override fun toString(): String {
        return "Bank(participant='$participant', branch='$branch', accountNumber='$accountNumber', accountType='$accountType')"
    }
}
