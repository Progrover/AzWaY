package com.example.azway.managers

import android.util.Log
import com.example.azway.AppwriteClient
import io.appwrite.services.Account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.UUID
import java.util.function.BiConsumer
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

object AppwriteManager {
    @JvmOverloads
    fun <R> getContinuation(
            onFinished: BiConsumer<String?, Throwable?>,
            dispatcher: CoroutineDispatcher = Dispatchers.Default
    ): Continuation<R> {
        return object : Continuation<R> {
            override val context: CoroutineContext
                get() = dispatcher

            override fun resumeWith(result: Result<R>) {
                onFinished.accept(result.getOrNull().toString(), result.exceptionOrNull())
            }
        }
    }

    suspend fun registerAccount(login: String, password: String, name: String) {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        val user = account.create(
                userId = UUID.randomUUID().toString(),
                email = login,
                password = password,
                name = name
        )
        Log.e("user", user.email + user.id)
        account.createEmailSession(
                email = login,
                password = password
        )

    }
    suspend fun getAccount(): io.appwrite.models.Account {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        return account.get()
    }

}