package br.com.zup.model

data class Account(
    val accountType: String,
    val accountInstitution: Organization,
    val accountBranch: String,
    val accountNumber: String,
    val accountOwner: Owner
) {

}
