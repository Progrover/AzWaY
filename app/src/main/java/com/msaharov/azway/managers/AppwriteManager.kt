package com.msaharov.azway.managers

import android.util.Log
import com.msaharov.azway.AppwriteClient
import io.appwrite.services.Account
import io.appwrite.services.Databases
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
        val userID = UUID.randomUUID().toString()
        val databases = Databases(client)
        val user = account.create(
                userId = userID,
                email = login,
                password = password,
                name = name,

        )
        Log.e("user", user.email + user.id)
        account.createEmailSession(
                email = login,
                password = password
        )
        try {
            databases.createDocument(
                    databaseId = "6571856e20d1ed19c405",
                    collectionId = "64525c2351930e2d5573",
                    documentId = userID,
                    data = mapOf(
                            "accountId" to userID,
                            "userName" to user.name
                    )
            )
        } catch (e: Exception) {
            Log.e("Appwrite", "Error: " + e.message)

        }
    }
    suspend fun getAccount(): io.appwrite.models.User<Map<String, Any>> {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        return account.get()
    }
    suspend fun login(mail: String, password: String) {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        account.createEmailSession(
                email = mail,
                password = password
        )
    }

}