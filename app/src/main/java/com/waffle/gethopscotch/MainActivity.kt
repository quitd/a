package com.waffle.gethopscotch

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.gson.Gson
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
    val rules: Array<Rule>,
    val customRules: Array<CustomRule>
)

class Object(
    val type: Int,
    val objectID: String,
    var text: String?,
    val xPosition: String,
    val yPosition: String,
    val rules: Array<String>,
    val name: String
)

class Scene(
    val objects: Array<String>,
    val name: String
)

class StageSize(
    val width: Int,
    val height: Int
)

class Param(
    val value: String,
    var datum: Datum?,
    var name: String?,
    /*val type: Int,
    val key: String,
    var variable: String?,
    var params: Array<Param>?*/
)

class Rule(
    //val abilityID: String,
    val id: String,
    val parameters: Array<Param>?
)

class CustomRule(
    val rules: Array<String>,
    val id: String,
    val name: String,
)

class Datum(
    val type: Int,
    /*var variable: String?,
    var HSTraitTypeKey: Int?,
    var HSTraitObjectIDKey: String?,
    var HSTraitIDKey: String?,
    var HSTraitObjectParameterTypeKey: Int?,
    var obj: Obj?,
    var description: String?,
    var params: Array<Param>?*/
)

@Composable
fun Editor() {
    var project: Project? by remember { mutableStateOf(null) }

    val client = OkHttpClient()
    val req = Request.Builder()
        .url("https://c.gethopscotch.com/api/v1/projects/11r0coc6ew")
        .get()
        .build()
    client.newCall(req).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            project = Gson().fromJson(response.body!!.string(), Project::class.java)
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
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "objects") {
                    composable("objects") { objE(project!!, navController) }
                    composable("code/{obj}",
                        arguments = listOf(navArgument("obj") {
                            type = NavType.StringType
                        })
                    ) { codeE(it, project!!) }
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

@Composable
fun objE(project: Project, n: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(project.stageSize.width.dp)
                .height(project.stageSize.height.dp)
                .aspectRatio((project.stageSize.width.dp / project.stageSize.height.dp)),
        ) {
            AsyncImage(
                model = R.drawable.editor_bg,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            project.scenes[0].objects.forEach { v -> //scene
                val obj = project.objects.find { it.objectID == v }
                if (obj?.type == 1) {
                    Text(
                        obj.text!!,
                        modifier = Modifier
                            .offset(
                                obj.xPosition.toFloat().dp,
                                obj.yPosition.toFloat().dp
                            )
                            .clickable { n.navigate("code/" + obj.objectID) },
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun codeE(ba: NavBackStackEntry, project: Project) {
    val obj = project.objects.find { it.objectID == ba.arguments!!.getString("obj") }!!
    Box(
        Modifier
            .background(Color(1, 203, 203))
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Column {
            Text(obj.name, Modifier.padding(bottom = 10.dp), color = Color.White)
            Box(Modifier.clip(RoundedCornerShape(10.dp))) {
                Box(
                    Modifier
                        .background(Color.White)
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    obj.rules.forEach {
                        rule(it, project)
                    }
                }
            }
        }
    }
}

@Composable
fun rule(id: String, project: Project) {
    val p =
        project.customRules.find { it.id == id } ?: project.rules.find { it.id == id }!!
    var open: Boolean by remember { mutableStateOf(false) }
    Box(Modifier.clip(RoundedCornerShape(10.dp))) {
        Box(
            Modifier
                .background(if (p is CustomRule) Color(218, 218, 218) else Color(203, 1, 68))
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row(
                    if (open) Modifier.padding(bottom = 10.dp) else Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val col = if (p is CustomRule) Color.Black else Color.White
                    Box(Modifier.clickable { open = !open }) {
                        if (open) Icon(
                            painterResource(R.drawable.open),
                            null,
                            tint = col
                        ) else Icon(
                            painterResource(R.drawable.closed), null, tint = col
                        )
                    }

                    Text(
                        if (p is CustomRule) p.name else "When",
                        Modifier.padding(end = 8.dp),
                        color = col
                    )

                    if (p is Rule) {
                        p.parameters!!.forEach {
                            Par(it)
                        }
                    }
                }
                if (open) {
                    Box(
                        Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Box(
                            Modifier
                                .background(Color.White)
                                .padding(10.dp)
                                .fillMaxWidth()
                        ) {
                            if (p is CustomRule) {
                                p.rules.forEach {
                                    rule(it, project)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Par(it: Param) {
    Box(
        Modifier
            .border(2.dp, Color.White, CircleShape)
            .padding(4.dp)) {
        if (it.datum != null)
            Text(blocks[it.datum?.type.toString()]?.get(1) ?: "", color = Color.White)
    }
}