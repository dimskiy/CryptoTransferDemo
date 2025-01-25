package `in`.windrunner.cryptotransferdemo.domain.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepository
import `in`.windrunner.cryptotransferdemo.domain.repository.CalculatorRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    @Singleton
    abstract fun bindCalculatorRepository(impl: CalculatorRepositoryImpl): CalculatorRepository
}