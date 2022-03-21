package com.waffle.gethopscotch

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.beust.klaxon.Klaxon
import com.waffle.gethopscotch.ui.theme.HopscotchTheme
import okhttp3.*
import java.io.IOException


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //probably temp?
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

class Project(
    val objects: Array<Object>,
    val scenes: Array<Scene>,
    val stageSize: StageSize,
)

class Object(
    val type: Int,
    val objectID: String,
    var text: String?,
    val xPosition: String,
    val yPosition: String,
)

class Scene(
    val objects: Array<String>,
    val name: String,
)

class StageSize(
    val width: Int,
    val height: Int,
)

@Composable
fun Editor() {
    var project: Project? by remember { mutableStateOf(null) }

    val client = OkHttpClient()
    val req = Request.Builder()
        .url("https://c.gethopscotch.com/api/v1/projects/115dag2ang")
        .get()
        .build()
    client.newCall(req).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            project = Klaxon().parse<Project>(response.body!!.string())!!
        }

        override fun onFailure(call: Call, e: IOException) {
            TODO("Not yet implemented")
        }
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.editor_title)) }
            )
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            if (project != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(project!!.stageSize.width.dp)
                            .height(project!!.stageSize.height.dp)
                            .aspectRatio((project!!.stageSize.width.dp / project!!.stageSize.height.dp)),
                    ) {
                        AsyncImage(
                            model = R.drawable.editor_bg,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        project!!.scenes[0].objects.forEach { v ->
                            val obj = project!!.objects.find { it.objectID == v }
                            if (obj?.type == 1) {
                                Text(
                                    obj.text!!,
                                    modifier = Modifier.offset(obj.xPosition.toFloat().dp,
                                        obj.yPosition.toFloat().dp),
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
