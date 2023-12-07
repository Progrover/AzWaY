package com.msaharov.azway.managers

import android.util.Log
import com.msaharov.azway.models.AppwriteClient
import com.msaharov.azway.models.RegUser
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

    suspend fun registerAccount(user : RegUser) {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        val password = user.password
        val email = user.email
        val phone_number = user.phone_number
        val sex = user.sex
        val name = user.name
        val birthday = user.birthday
        val email_required = user.email_required
        val userID = UUID.randomUUID().toString()
        val databases = Databases(client)
        val user = account.create(
                userId = userID,
                email = email_required,
                password = password,
                name = name,

        )
        Log.e("user", user.email + user.id)
        account.createEmailSession(
                email = email,
                password = password
        )
        try {
            databases.createDocument(
                    databaseId = "6571856e20d1ed19c405",
                    collectionId = "65718593dcf902398d89",
                    documentId = userID,
                    data = mapOf(
                            "id" to userID,
                            "name" to user.name,
                            "email" to user.email,
                            "phone_number" to phone_number,
                            "email_required" to email_required,
                            "sex" to sex,
                            "birthday" to birthday,
                            "password" to password
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