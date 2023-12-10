package com.msaharov.azway.managers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.msaharov.azway.models.AppwriteClient
import com.msaharov.azway.models.Cursor
import com.msaharov.azway.models.Mark
import com.yandex.mapkit.geometry.Point
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.DocumentList
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.Calendar
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
                    databaseId = Databases.GENERAL_DATABASE_ID,
                    collectionId = Collections.USERS_COLLECTION_ID,
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