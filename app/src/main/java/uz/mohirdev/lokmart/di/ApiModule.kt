package uz.mohirdev.lokmart.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.mohirdev.lokmart.data.api.auth.AuthApi
import uz.mohirdev.lokmart.data.api.order.OrderApi
import uz.mohirdev.lokmart.data.api.product.ProductApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit) = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun  provideOrderApi(retrofit: Retrofit) = retrofit.create(OrderApi::class.java)
}