package uz.mohirdev.lokmart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.mohirdev.lokmart.data.repo.AuthRepositoryImpl
import uz.mohirdev.lokmart.data.repo.OrderRepositoryImpl
import uz.mohirdev.lokmart.data.repo.ProductRepositoryImpl
import uz.mohirdev.lokmart.domain.repo.AuthRepository
import uz.mohirdev.lokmart.domain.repo.OrderRepository
import uz.mohirdev.lokmart.domain.repo.ProductRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    abstract fun bindProductRepository(
        authRepositoryImpl: ProductRepositoryImpl
    ) : ProductRepository

    @Binds
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ) : OrderRepository
}