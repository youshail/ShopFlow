package com.youshail.ecommerce.app.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.youshail.ecommerce.app.firebase.FirebaseCommon
import com.youshail.ecommerce.app.util.Constants.INTRODUCTION_SP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseComm(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ) = FirebaseCommon(firestore,auth)

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application) = application.getSharedPreferences(INTRODUCTION_SP, MODE_PRIVATE)
}