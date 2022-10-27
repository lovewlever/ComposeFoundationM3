package com.gq.basicm3.hilt

import com.gq.basicm3.common.SystemUiController
import com.gq.basicm3.common.SystemUiControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun bindsSystemUiController(systemUiControllerImpl: SystemUiControllerImpl): SystemUiController
}