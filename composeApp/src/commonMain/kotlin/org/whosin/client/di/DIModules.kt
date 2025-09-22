package org.whosin.client.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.whosin.client.core.network.HttpClientFactory
import org.whosin.client.data.remote.DummyDataSource
import org.whosin.client.data.remote.RemoteClubDataSource
import org.whosin.client.data.remote.RemoteMemberDataSource
import org.whosin.client.data.repository.DummyRepository
import org.whosin.client.data.repository.ClubRepository
import org.whosin.client.data.repository.MemberRepository
import org.whosin.client.presentation.dummy.DummyViewModel
import org.whosin.client.presentation.auth.LoginViewModel
import org.whosin.client.presentation.home.HomeViewModel
import org.whosin.client.presentation.mypage.MyPageViewModel

fun appModule() = listOf(
    httpClientModule,
    dataSourceModule,
    repositoryModule,
    viewModelModule,
    platformModule
)

expect val platformModule: Module

val httpClientModule = module {
    single{ HttpClientFactory.create(get()) }
}

val dataSourceModule = module {
    single { RemoteMemberDataSource(get()) }
    single { RemoteClubDataSource(get()) }
    single { DummyDataSource(get()) } // TODO: 이후에 삭제 예정
}

val repositoryModule = module {
    single { MemberRepository(get()) }
    single { ClubRepository(get()) }
    single { DummyRepository(get()) } // TODO: 이후에 삭제 예정
}

// ViewModel을 새로 생성하는 경우에 모듈에 추가하여 사용
val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MyPageViewModel)
    viewModelOf(::DummyViewModel) // TODO: 이후에 삭제 예정
}
