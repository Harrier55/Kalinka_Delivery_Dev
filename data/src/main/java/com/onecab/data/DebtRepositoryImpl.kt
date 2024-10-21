package com.onecab.data

import com.onecab.domain.entity.DebtEntity
import com.onecab.domain.repository.DebtRepository
import com.onecab.domain.repository.RegisterRepository
import com.onecab.source_web.api.KalinkaApiService
import com.onecab.source_web.models_dto.OrderDebtDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepositoryImpl @Inject constructor(
    private val kalinkaApiService: KalinkaApiService,
    private val registerRepository: RegisterRepository,
): DebtRepository {

    // Кэшированый список
    private var cashOrderDebtList = mutableListOf<DebtEntity>()

    // Получим список по долгам от сервера и закэшировать
    override suspend fun getDebtListFromServer(orderId: String): Result<List<DebtEntity>> {
        cashOrderDebtList.clear()
        try {
            val debtList = kalinkaApiService.getDebt(
                token = registerRepository.getAuthToken().token ?: "",
                orderId = orderId
            ).map { item ->
                item.toOrderDebtEntity()
            }
            cashOrderDebtList = debtList.toMutableList()
            return Result.success(debtList)

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    // Получить список по долгам контрагента ( из кэша )
    override fun getCashDebtList(): List<DebtEntity> {
        return cashOrderDebtList
    }

    private fun OrderDebtDTO.toOrderDebtEntity(): DebtEntity {
        return DebtEntity(
            accountDocumentNumber = this.accountDocumentNumber ?: "",
            amount = this.amount ?: 0.0,
            status = this.status ?: ""
        )
    }
}