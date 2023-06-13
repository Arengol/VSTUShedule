package ru.cchgeu.vorobev.vstuschedule.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.cchgeu.vorobev.vstuschedule.data.ApplicationPreferencesRepo
import ru.cchgeu.vorobev.vstuschedule.network.NetworkRepository
import ru.cchgeu.vorobev.vstuschedule.network.NetworkRepositoryImpl
import ru.cchgeu.vorobev.vstuschedule.network.ServerAPI
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModel
import ru.cchgeu.vorobev.vstuschedule.performance.viewmodel.MainViewModelImpl

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun provideAplicationReferencesRepo(@ApplicationContext context: Context) = ApplicationPreferencesRepo(context)
    @Provides
    fun provideHttpClient (): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()
    @Provides
    fun provideRetrofit (okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .build()

    @Provides
    fun provideServerAPI(retrofit: Retrofit): ServerAPI = retrofit.create(ServerAPI::class.java)

    @Provides
    fun provideNetworkRepository(serverApi: ServerAPI): NetworkRepository = NetworkRepositoryImpl(serverApi)

    @Provides
    fun provideMainViewModel(networkRepository: NetworkRepository, applicationPreferencesRepo: ApplicationPreferencesRepo): MainViewModel = MainViewModelImpl.Builder.create(networkRepository, applicationPreferencesRepo)
}