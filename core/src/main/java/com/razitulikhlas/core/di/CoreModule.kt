package com.razitulikhlas.core.di

import androidx.room.Room
import com.razitulikhlas.core.BuildConfig
import com.razitulikhlas.core.data.source.LocalDataSource
import com.razitulikhlas.core.data.source.RemoteDataSource
import com.razitulikhlas.core.data.source.local.mapping.DatabaseApps
import com.razitulikhlas.core.data.source.remote.network.ApiService
import com.razitulikhlas.core.domain.dashboard.officer.repository.IOfficerRepository
import com.razitulikhlas.core.domain.dashboard.officer.repository.OfficerRepository
import com.razitulikhlas.core.domain.dashboard.officer.usecase.IOfficerUseCase
import com.razitulikhlas.core.domain.dashboard.officer.usecase.OfficerUseCase
import com.razitulikhlas.core.domain.dashboard.own.repository.IOwnRepository
import com.razitulikhlas.core.domain.dashboard.own.repository.OwnRepository
import com.razitulikhlas.core.domain.dashboard.own.usecase.IOwnUseCase
import com.razitulikhlas.core.domain.dashboard.own.usecase.OwnUseCase
import com.razitulikhlas.core.domain.login.repository.ILoginRepository
import com.razitulikhlas.core.domain.login.repository.LoginRepository
import com.razitulikhlas.core.domain.login.usecase.ILoginUseCase
import com.razitulikhlas.core.domain.login.usecase.LoginUseCase
import com.razitulikhlas.core.domain.mapping.repository.CustomerRepository
import com.razitulikhlas.core.domain.mapping.repository.ICustomerRepository
import com.razitulikhlas.core.domain.mapping.usecase.CustomerUseCase
import com.razitulikhlas.core.domain.mapping.usecase.ICustomerUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val retrofitModule = module {
    single {
//        val trustManagerFactory = SSLCertificateConfigurator.getTrustManager(androidContext())
//        val trustManagers = trustManagerFactory.trustManagers
//        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
//            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
//        }
//        val trustManager = trustManagers[0] as X509TrustManager

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build().create(ApiService::class.java)
    }
}

val remoteDataSourceModule = module {
    factory { RemoteDataSource(get()) }
}

val databaseModule = module {
    factory {
        get<DatabaseApps>().customerDao
    }
    factory {
        get<DatabaseApps>().clientDao
    }
    single {
//        val passphrase: ByteArray = SQLiteDatabase.getBytes("razit".toCharArray())
//        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(androidContext(), DatabaseApps::class.java, "db_ng")
            .fallbackToDestructiveMigration()
//            .openHelperFactory(factory)
            .build()
    }
}

val localDataSourceSourceModule = module {
    factory { LocalDataSource(get(),get()) }
}

val repositoryModule = module {
    single<ICustomerRepository> { CustomerRepository(get()) }
    single<ILoginRepository> { LoginRepository(get()) }
    single<IOwnRepository> { OwnRepository(get()) }
    factory<IOfficerRepository> { OfficerRepository(get(),get()) }

}

val useCaseModule = module {
    factory<ICustomerUseCase> { CustomerUseCase(get()) }
    factory<ILoginUseCase> { LoginUseCase(get()) }
    factory<IOwnUseCase> { OwnUseCase(get()) }
    factory<IOfficerUseCase> { OfficerUseCase(get()) }
}