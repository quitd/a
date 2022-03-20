package com.waffle.gethopscotch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.waffle.gethopscotch.ui.theme.HopscotchTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HopscotchTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { Home(navController) }
                    composable("editor") { Editor() }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(n: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            Column {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    elevation = 10.dp,
                    onClick = fun() { n.navigate("editor") }
                ) {
                    Column {
                        AsyncImage(
                            model = "https://s3.amazonaws.com/hopscotch-cover-images/production/115dag2ang.png",
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(modifier = Modifier.padding(10.dp), text = "Project")
                    }
                }
            }
        }
    }
}

@Composable
fun Editor() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editor_title)) }
            )
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = R.drawable.editor_bg,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}