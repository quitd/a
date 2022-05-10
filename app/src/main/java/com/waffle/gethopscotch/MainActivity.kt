package com.waffle.gethopscotch

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
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
    var objects: Array<Object>,
    var scenes: Array<Scene>,
    var stageSize: StageSize,
    var rules: Array<Rule>,
    var customRules: Array<CustomRule>,
    var abilities: Array<Ability>,
    var eventParameters: Array<ep>,
    var variables: Array<Var>
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
    var variable: String?,
    val key: String,
    var params: Array<Param>?,
    val type: Int
)

class Rule(
    val abilityID: String,
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
    var variable: String?,
    var params: Array<Param>?,
    var description: String?,
    /*var HSTraitTypeKey: Int?,
    var HSTraitObjectIDKey: String?,
    var HSTraitIDKey: String?,
    var HSTraitObjectParameterTypeKey: Int?,
    var obj: Obj?,*/
)

class Ability(
    var abilityID: String,
    var blocks: Array<Bloc>
)

class Bloc(
    var type: Int,
    var parameters: Array<Param>?,
    var controlScript: cS,
    var controlFalseScript: cS
)

class ep(
    var blockType: Int,
    var description: String,
    var id: String,
    var objectID: String?
)

class Var(
    var name: String,
    var objectIdString: String,
    var type: Int
)

class cS(var abilityID: String)

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
                    composable(
                        "code/{obj}",
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
                    val empty = obj.text!!.isNotEmpty()
                    Text(
                        if (empty) obj.text!! else "Text",
                        modifier = Modifier
                            .offset(
                                (obj.xPosition.toFloat() / project.stageSize.width).dp,
                                obj.yPosition.toFloat().dp
                            )
                            .clickable { n.navigate("code/" + obj.objectID) },
                        color = if (empty) Color.Black else Color.Gray
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
                    Column(
                        Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        obj.rules.forEach {
                            rule(it, project)
                        }
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
                    if (open) Modifier
                        .padding(bottom = 10.dp)
                        .horizontalScroll(rememberScrollState()) else Modifier.horizontalScroll(
                        rememberScrollState()
                    ),
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
                        color = col
                    )

                    if (p is Rule) {
                        p.parameters!!.forEach {
                            Par(it, project, true)
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
                            } else if (p is Rule) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    project.abilities.find { it.abilityID == p.abilityID }?.blocks?.forEach {
                                        Block(it, project)
                                    }
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
fun Par(it: Param, project: Project, first: Boolean = false) {
    Box(Modifier.padding(start = 6.dp)) {
        var k = ""
        var color: Boolean by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(k, Modifier.padding(end = 4.dp), color = if (first) Color.White else Color.Black)
            Box(
                Modifier
                    .background(if (color) Color(255, 165, 0) else Color.White, CircleShape)
                    .border(2.dp, Color.Black, CircleShape)
                    .padding(12.dp, 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val str: ArrayList<Unit> = arrayListOf()
                    if (it.datum != null) {
                        if (it.datum?.variable != null) {
                            val va =
                                project.variables.find { v -> v.objectIdString == it.datum?.variable }
                            val ei = va?.type != 8000
                            str.add(
                                Text(
                                    "${if (ei) blocks[va?.type.toString()]?.get(0) else ""} ${va?.name!!}",
                                    color = if (ei) Color.White else Color.Black
                                )
                            )
                            color = ei
                        } else if (it.datum?.params != null) {
                            k =
                                if (blocks[it.datum?.type.toString()]?.get(1) != it.datum?.params!![1].key) blocks[it.datum!!.type.toString()]?.get(
                                    1
                                ) ?: it.key
                                else it.key
                            it.datum!!.params?.forEach {
                                str.add(Par(it, project))
                            }
                        } else {
                            val s = blocks[it.datum?.type.toString()]
                            str.add(Text(s?.getOrElse(1) { s[0] } ?: "", color = Color.Black))
                        }
                    } else {
                        if (it.params != null) {
                            k = blocks[it.type.toString()]?.get(1).toString()
                            it.params?.forEach {
                                str.add(Par(it, project))
                            }
                        } else {
                            k = it.key
                            str.add(Text(it.value, color = Color.Black))
                            if (it.variable != null) {
                                val f = project.eventParameters.find { v -> v.id == it.variable }
                                when (f?.blockType) {
                                    8000 -> {
                                        str.add(
                                            Text(
                                                project.objects.find { it.objectID == f.objectID }?.name
                                                    ?: "?"
                                            )
                                        )
                                    }
                                    8003 -> {
                                        val ii =
                                            project.variables.find { it.objectIdString == f.objectID }
                                        str.add(
                                            Text(
                                                ii?.name ?: f.description,
                                                color = Color.Black
                                            )
                                        )
                                    }
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
fun Block(b: Bloc, project: Project) {
    Box(Modifier.clip(RoundedCornerShape(8.dp))) {
        Box(
            Modifier
                .background(
                    when (blocks[b.type.toString()]!![0]) {
                        "move" -> Color(215, 62, 30)
                        "looks" -> Color(99, 174, 30)
                        "draw" -> Color(166, 0, 110)
                        "var" -> Color(231, 182, 1)
                        "ctrl" -> Color(62, 131, 190)
                        "old" -> Color(100, 127, 150)
                        else -> Color(218, 218, 218)
                    }
                )
                .padding(10.dp)
        ) {
            Row(
                Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(blocks[b.type.toString()]!![1], color = Color.White)
                b.parameters?.forEach {
                    Par(it, project, true)
                }
            }
        }
    }
}