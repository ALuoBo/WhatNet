package com.aluobo.whatnet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aluobo.whatnet.net.ApiService
import com.aluobo.whatnet.net.RequestHelper
import com.aluobo.whatnet.net.RequestResult
import com.aluobo.whatnet.net.RetrofitClient
import com.aluobo.whatnet.ui.theme.WhatNetTheme
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val apiService: ApiService by lazy { RetrofitClient.createService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhatNetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var text by remember {
                        mutableStateOf("")

                    }
                    var scope =  rememberCoroutineScope()
                    Button(
                        onClick = {
                            android.os.Debug.startMethodTracing()
                            scope.launch {
                                RequestHelper.request {
                                    apiService.getBanners()
                                }.collect {
                                    when (it) {
                                        is RequestResult.Success -> {
                                            text = GsonBuilder().setPrettyPrinting().create().toJson(it.data)
                                        }
                                        else -> {

                                        }
                                    }
                                    android.os.Debug.stopMethodTracing()
                                }
                            }
                        }
                    ){
                        Text(text = "发送请求")
                    }

                    Greeting(text)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WhatNetTheme {
        Greeting("Android")
    }
}