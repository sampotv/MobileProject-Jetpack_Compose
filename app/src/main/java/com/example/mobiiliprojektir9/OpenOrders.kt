package com.example.mobiiliprojektir9

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun OpenDeliveries(
    orders: List<Order>
){
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Avoimet keikat",
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.width(24.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp, end = 20.dp)
            ){
            items(orders){order ->
                OrderRow(
                    order,
                    Modifier.fillParentMaxWidth()

                )
            }
        }
    }
}
@Composable
fun OrderRow(order: Order, modifier: Modifier){
    Column(
        modifier
            .padding(8.dp, top = 20.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)).padding(10.dp),
    ){
        Column(
           modifier.padding(2.dp)
        ){
            Text(order.locationFrom)
            Text(order.locationTo)
            Text(order.cargo)
        }
    }
}

@Preview
@Composable
fun OpenDeliveryPreview(){
    val orders = listOf(
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:"),
        Order("Lähtöosoite:", "Kohdeosoite:", "Rahti:")
    )
    OpenDeliveries(orders)
}