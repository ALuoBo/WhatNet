package com.aluobo.whatnet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    username:String,
    password:String,
    usernameChange: (String) -> Unit,
    passwordChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Column {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            value = username,
            onValueChange = {
                usernameChange(it)
            })
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            value = password,
            onValueChange = {
                passwordChange(it)
            })
        Button(modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .align(Alignment.CenterHorizontally),
            onClick = { onClick.invoke() }) {
            Text("Login")
        }
    }
}

