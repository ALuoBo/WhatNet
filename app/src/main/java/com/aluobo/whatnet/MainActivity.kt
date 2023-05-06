package com.aluobo.whatnet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aluobo.whatnet.net.ApiService
import com.aluobo.whatnet.net.RetrofitClient
import com.aluobo.whatnet.ui.LoginScreen
import com.aluobo.whatnet.ui.theme.WhatNetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    var username = ""
                    var password = ""
                    var scope = rememberCoroutineScope()
                    Column {
                        LoginScreen(username,
                            password,
                            usernameChange = fun(a: String) { username = a },
                            passwordChange = fun(a: String) { password = a },
                            onClick = {
                                scope.launch {
                                    val result = async {
                                        apiService.login(username, password)
                                    }
                                    Log.d("MainActivity", "onCreate:${result.await().data} ")
                                }
                            }
                        )
                        Button(onClick = {
                            scope.launch {
                                val article =
                                    withContext(Dispatchers.Default) {
                                        apiService.getCollect(0)
                                    }
                                Log.d("MainActivity", "onCreate: $article")
                            }
                        }) {
                            Text(text = "登录后点击，查看cookie 是否携带成功，并返回收藏的文章(查看日志检验)")
                        }
                    }


//                    var text by remember {
//                        mutableStateOf("")
//
//                    }
//                    var scope =  rememberCoroutineScope()
//                    Button(
//                        onClick = {
//                            android.os.Debug.startMethodTracing()
//
//
//
//                            scope.launch {
//                                RequestHelper.request {
//                                    apiService.getBanners()
//                                }.collect {
//                                    when (it) {
//                                        is RequestResult.Success -> {
//                                            text = GsonBuilder().setPrettyPrinting().create().toJson(it.data)
//                                        }
//                                        else -> {
//
//                                        }
//                                    }
//                                    android.os.Debug.stopMethodTracing()
//                                }
//                            }
//
//
//
//                        }
//                    ){
//                        Text(text = "发送请求")
//                    }
//
//                    Greeting(text)
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