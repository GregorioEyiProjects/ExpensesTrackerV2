package com.example.expensestrackercomposablev2.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensestrackercomposablev2.appComponents.SetBarColor

@Composable
fun Login(
    destinationPage: NavController
) {

    SetBarColor(color = MaterialTheme.colorScheme.background)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SignUpTextComponent()
        Spacer(modifier = Modifier.height(5.dp))
        TextFieldsLogin(destinationPage)

    }

}

@Composable
private fun SignUpTextComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Sign In",
            fontSize = 40.sp,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}


@Composable
fun TextFieldsLogin(destinationPage: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var validData by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {


        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            placeholder = {
                Text(text = "Email")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Color.White
                )
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(bottom = 5.dp)
        )

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            placeholder = {
                Text(text = "Password")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Email",
                    tint = Color.White
                )
            },
            trailingIcon = {
                val icon = if (passwordVisible.value) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                val description = if (passwordVisible.value) {
                    "Hide password"
                } else {
                    "Show password"
                }
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            },
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 5.dp)
        )


        //Remember me
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Remember me",
                fontSize = 15.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot password?",
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }


    }

    //Button sign up
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp, bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        val initialPart = "Don't have an account? "
        val signUpText = "Sign Up"

        val annotatedString = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(initialPart)
            }
            withStyle(style = SpanStyle(
                MaterialTheme.colorScheme.onError,
                fontSize = 16.sp
            )) {
                pushStringAnnotation(
                    tag = signUpText,
                    annotation = signUpText
                )
                append(signUpText)
            }

        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Or Sign in with

            Text(
                text = "Or sign in With",
                color = Color.White //Change later
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Filled.Facebook, contentDescription = "Facebook")
                Icon(imageVector = Icons.Filled.Facebook, contentDescription = "Facebook")
                Icon(imageVector = Icons.Filled.Facebook, contentDescription = "Facebook")
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Sign in button
            Button(
                onClick = {
                    destinationPage.navigate("Home")
                },
                enabled = validData,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Sign In")
            }

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset).firstOrNull()
                        ?.also { spanTextClicked ->
                            if (spanTextClicked.item == signUpText) {
                                destinationPage.navigate("Register")
                            }
                        }
                }
            )
            /*
            * Text(
                text = "Don't have an account? Sign Ip",
                modifier = Modifier.clickable { destinationPage.navigate("Register") }
            )*/
        }
    }


    validData = ! (email.isEmpty() || password.isEmpty() || !isChecked)

    Log.d("TextFieldsLogin", "email = $email")
    Log.d("TextFieldsLogin", "password = $password")
    Log.d("TextFieldsLogin", "isChecked = $isChecked")
    Log.d("TextFieldsLogin", "ValidaData = $validData")



}

