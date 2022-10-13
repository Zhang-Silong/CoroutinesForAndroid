package com.example.coroutinestest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var testText: TextView
    private lateinit var button1: Button
    private lateinit var button2: Button

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testText = findViewById(R.id.testText)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button1.setOnClickListener {
            CoroutineScope(Job()).launch {
                val time = measureTimeMillis {
                    val r1 = withContext(Dispatchers.IO) {
                        delay(2000)
                        "Hello"
                    }
                    val r2 = withContext(Dispatchers.IO) {
                        delay(2000)
                        "World"
                    }
                    withContext(Dispatchers.Main) {
                        testText.text = r1 + r2
                    }
                }
                Log.d(TAG, Thread.currentThread().toString())
            }
            Log.d(TAG, Thread.currentThread().toString())
        }

        button2.setOnClickListener {
            /*launch {
                Log.d(TAG, Thread.currentThread().toString())
            }*/
            CoroutineScope(Job()).launch(Dispatchers.Main) {
                val time = measureTimeMillis {
                    coroutineScope {
                        launch {
                            Log.d(TAG, "start1")
                            delay(2000)
                        }
                        launch {
                            Log.d(TAG, "start2")
                            delay(2000)
                        }
                    }
                }
                delay(10000)
            }
        }
    }

    private suspend fun test1(): String {
        return withContext(Dispatchers.IO) {
            delay(2000)
            "Hello"
        }
    }

    private suspend fun test2(): String {
        return withContext(Dispatchers.IO) {
            delay(2000)
            "World"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

