package com.parana.dobleyfalta.adminpantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.parana.dobleyfalta.R

data class Usuario(val name: String, val email: String)

@Composable
fun AdminScreen(navController: NavController) {
    val DarkBlue = colorResource(id = R.color.darkBlue)
    val PrimaryOrange = colorResource(id = R.color.primaryOrange)
    val DarkGrey = Color(0xFF1A375E)
    val LightGrey = Color(0xFFA0B3C4)
    val RedDelete = colorResource(id = R.color.red_delete)

    val usuarios = listOf(
        Usuario("Juan Pérez", "juan@example.com"),
        Usuario("Ana Gómez", "ana@example.com"),
        Usuario("Carlos Ruiz", "carlos@example.com"),
        Usuario("Emanuel Neme", "em@gmail.com"),
        Usuario("Naomi Kakisu", "na@gmail.com"),
        Usuario("Conrado Peloso", "co@gmail.com"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Panel de Admin",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { navController.navigate("crear_usuario") },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Crear Usuario",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Crear Usuario", color = Color.White)
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = DarkGrey),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Lista de Usuarios",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(usuarios) { usuario ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF2C405A), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            //Las propiedades de alineación (horizontalArrangement, verticalAlignment, etc.) afectan solo a los hijos directos del contenedor.
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //Hay solo 2 Rows que son hijos del Row padre
                            //De esta forma el primer row queda a la izquierda y el segundo a la derecha
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFA0B3C4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        //Text() espera un String, no acepta Char
                                        text = usuario.name.first().toString(),
                                        color = DarkBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(
                                        usuario.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        usuario.email,
                                        color = LightGrey,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "Editar",
                                        tint = colorResource(id = R.color.blue_edit),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Eliminar",
                                        tint = RedDelete,
                                        modifier = Modifier.size(18.dp)
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

//GLOSARIO

//LazyColumn
//es un componente que muestra una lista vertical (lazy).
//lazy significa que solo renderiza en pantalla los elementos visibles, no todos al mismo tiempo
//LazyColumn está pensado principalmente para listas largas de elementos que se muestran en forma vertical.
//El nombre "Lazy" viene de que no renderiza todos los ítems al mismo tiempo, sino solo los que entran en
// pantalla, y a medida que hacés scroll va creando / destruyendo los demás.
// Esto ahorra mucha memoria y mejora el rendimiento.
//no acepta composables sueltos. El compilador espera llamadas a item { ... } o items(...) { ... }.

//verticalArrangement = Arrangement.spacedBy(8.dp)
//verticalArrangement → controla cómo se acomodan los elementos en el eje vertical.
//Arrangement.spacedBy(8.dp) → agrega un espaciado fijo de 8dp entre cada item de la lista.

//items(usuarios)
// dibujá un ítem por cada elemento de la lista usuarios.

//{ Usuario -> ... }
//Es una lambda con un parámetro. (las funciones lambda usan -> para separar los parametros del cuerpo)
//Ese parámetro es cada elemento de la lista usuarios que se está procesando en ese momento.

//Box
//Es un contenedor de layout que se usa principalmente para superponer elementos
// (uno arriba del otro), o para tener un solo hijo posicionado dentro.

//first()
//es una función que devuelve el primer elemento de una colección o secuencia.
//Pero en este caso no se una sobre una lista, sino sobre un String.