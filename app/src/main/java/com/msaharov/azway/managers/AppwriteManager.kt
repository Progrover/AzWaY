package com.msaharov.azway.managers

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.msaharov.azway.models.AppwriteClient
import com.msaharov.azway.models.Cursor
import com.msaharov.azway.models.Mark
import com.msaharov.azway.models.RegUser
import com.yandex.mapkit.geometry.Point
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.DocumentList
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.UUID
import java.util.function.BiConsumer
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext


object AppwriteManager {
    object Databases {
        const val GENERAL_DATABASE_ID = "6571856e20d1ed19c405"
    }

    object Collections {
        const val MARKS_COLLECTION_ID = "65730536c63a096e1522"
        const val USERS_COLLECTION_ID = "65718593dcf902398d89"
    }

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


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun registerAccount(user : RegUser) {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        val password = user.password
        val email = user.email
        val phoneNumber = user.phoneNumber
        val sex = user.sex
        val name = user.name
        val birthday = Date(user.birthday.timeInMillis + 14400000)
        val birthdayDateZone = birthday.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime().toString()
        val emailFromPhone = user.emailFromPhone
        val databases = Databases(client)
        val user = account.create(
                userId = ID.unique(),
                email = emailFromPhone,
                password = password,
                name = name,

                )
        account.createEmailSession(
                email = emailFromPhone,
                password = password
        )
        try {
            databases.createDocument(
                    databaseId = Databases.GENERAL_DATABASE_ID,
                    collectionId = Collections.USERS_COLLECTION_ID,
                    documentId = user.id,
                    data = mapOf(
                            "name" to name,
                            "email" to email,
                            "phone_number" to phoneNumber,
                            "sex" to sex,
                            "birthday" to birthdayDateZone,
                    )
            )
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }
    suspend fun getAccount(): io.appwrite.models.User<Map<String, Any>> {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        return account.get()
    }
    suspend fun getActiveMarksInZone(
            marksLD: MutableLiveData<ArrayList<Mark>>,
            topLeftPoint: Point,
            bottomRightPoint: Point
    ) {
        val client = AppwriteClient.getClient()
        val databases = Databases(client)

        try {
            val marks = ArrayList<Mark>()
            val documents = databases.listDocuments(
                databaseId = Databases.GENERAL_DATABASE_ID,
                collectionId = Collections.MARKS_COLLECTION_ID,
                queries = listOf(
                    Query.limit(100),
                    Query.lessThanEqual("lat", topLeftPoint.latitude),
                    Query.greaterThanEqual("lat", bottomRightPoint.latitude),
                    Query.lessThanEqual("lon", bottomRightPoint.longitude),
                    Query.greaterThanEqual("lon", topLeftPoint.longitude)
                )
            )
            documents.documents.forEach {
                marks.add(Mark(it.data))
            }
            marksLD.value?.clear()
            marksLD.postValue(marks)
        } catch (e: AppwriteException) {
            Log.e("Appwrite", "Error: " + e.message)
        }
    }
    suspend fun login(mail: String, password: String) {
        val client = AppwriteClient.getClient()
        val account = Account(client)
        account.createEmailSession(
                email = mail,
                password = password
        )
    }
    suspend fun getPagedMarks(
        cursor: Cursor?,
        limit: Int
    ): ArrayList<Mark>? {
        val client = AppwriteClient.getClient()
        val databases = Databases(client)

        try {
            val marks = ArrayList<Mark>()
            val documents: DocumentList<Map<String, Any>>
            if (cursor == null) {
                documents = databases.listDocuments(
                    databaseId = Databases.GENERAL_DATABASE_ID,
                    collectionId = Collections.MARKS_COLLECTION_ID,
                    queries = listOf(
                        Query.limit(limit),
                    )
                )

            } else {
                if (cursor.cursorType == Cursor.CursorType.ASC) {
                    documents = databases.listDocuments(
                        databaseId = Databases.GENERAL_DATABASE_ID,
                        collectionId = Collections.MARKS_COLLECTION_ID,
                        queries = listOf(
                            Query.limit(limit),
                            Query.cursorAfter(cursor.uuid.toString()),
                        )
                    )
                } else {
                    documents = databases.listDocuments(
                        databaseId = Databases.GENERAL_DATABASE_ID,
                        collectionId = Collections.MARKS_COLLECTION_ID,
                        queries = listOf(
                            Query.limit(limit),
                            Query.cursorBefore(cursor.uuid.toString()),
                        )
                    )
                }
            }
            documents.documents?.forEach {
                marks.add(Mark(it.data))
            }
            return marks
        } catch (e: AppwriteException) {
            e.printStackTrace()
            return null
        }
    }

}