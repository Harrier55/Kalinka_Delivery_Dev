package com.onecab.data.di

import com.onecab.data.CheckQrCodeRepositoryImpl
import com.onecab.data.DebtRepositoryImpl
import com.onecab.data.LocalSourceRepositoryImpl
import com.onecab.data.OrderRepositoryImpl
import com.onecab.data.ParamsRepositoryImpl
import com.onecab.data.PaymentRepositoryImpl
import com.onecab.data.RegisterRepositoryImpl
import com.onecab.domain.repository.CheckQrCodeRepository
import com.onecab.domain.repository.DebtRepository
import com.onecab.domain.repository.LocalSourceRepository
import com.onecab.domain.repository.OrderRepository
import com.onecab.domain.repository.ParamsRepository
import com.onecab.domain.repository.PaymentRepository
import com.onecab.domain.repository.RegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Это первая раелизация с интерфейсом, тоже работает. Нужны аннотации у RegisterRepositoryImpl
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class DataModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindRegisterRepository(
//        registerRepositoryImpl: RegisterRepositoryImpl
//    ): RegisterRepository
//}


// Можно переписать так, только надо убрать аннотации у RegisterRepositoryImpl
//@Module
//@InstallIn(SingletonComponent::class)
//class DataModule{
//
//    @Provides
//    @Singleton
//    fun provideRegisterRepository(): RegisterRepository{
//        return RegisterRepositoryImpl()
//    }
//}

// или так, в этом случае в репозитории надо добавить аннотацию @Inject
@Module
@InstallIn(SingletonComponent::class)
interface DataBindModule {

    @Binds
    @Singleton
    fun bindRepositoryImpl(
        repositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface LocalSourceBindModule {

    @Binds
    fun bindLocalSourceRepository(
        localSourceRepositoryImpl: LocalSourceRepositoryImpl
    ): LocalSourceRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface OrderRepositoryBindModule {

    @Binds
    @Singleton
    fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface DebtRepositoryBindModule {

    @Binds
    @Singleton
    fun bindDebtRepository(
        debtRepositoryImpl: DebtRepositoryImpl
    ): DebtRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface PaymentRepositoryBindModule {

    @Binds
    @Singleton
    fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface SettingRepositoryBindModule {

    @Binds
    @Singleton
    fun bindSettingRepository(
        settingRepositoryImpl: ParamsRepositoryImpl
    ): ParamsRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface CheckQrCodeRepositoryBindModule {

    @Binds
    @Singleton
    fun bindCheckQrCodeRepository(
        checkQrCodeRepositoryImpl: CheckQrCodeRepositoryImpl
    ): CheckQrCodeRepository
}




