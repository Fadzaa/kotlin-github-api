package com.example.github_api.repository


import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.github_api.model.local.User
import com.example.github_api.model.local.UserDao
import com.example.github_api.model.local.UserRoomDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

import java.io.IOException

class UserRepositoryTest {

    private lateinit var userDao: UserDao
    private lateinit var db: UserRoomDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserRoomDatabase::class.java).build()
        userDao = db.userDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertUserTest() = runBlocking {
        val user = User(1, "username", "avatar")
        userDao.insert(user)
        val result = userDao.getAllUser().value
        assertEquals(1, result?.size)
    }

}