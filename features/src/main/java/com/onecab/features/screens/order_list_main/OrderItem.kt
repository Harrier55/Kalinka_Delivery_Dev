package com.onecab.features.screens.order_list_main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onecab.core.R
import com.onecab.core.theme.KalinkaDeliveryTheme
import com.onecab.domain.entity.OrderEntity

@Composable
internal fun OrderItem(
    modifier: Modifier = Modifier,
    orderEntity: OrderEntity,
    onClick: (orderId: Int) -> Unit
) {
    Column(
        modifier = modifier
            .width(320.dp)
            .height(100.dp)
            .clickable {
                onClick.invoke(1)
            }
            .shadow(
                elevation = 2.dp,
                shape = MaterialTheme.shapes.small
            )
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.small
            )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = orderEntity.deliveryDate,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "№ ${orderEntity.orderNumber}",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Text(
            text = orderEntity.purchaserName,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.padding(start = 10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = orderEntity.deliveryAddress,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.width(280.dp)
            )

            // Показываем иконку, если количество товала было изменено
            if (orderEntity.goodHasBeenModified){
                Icon(
                    painter = painterResource(id = R.drawable.icon_edit_pen),
                    contentDescription = "is-edit",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun PreviewOrderItem() {
    KalinkaDeliveryTheme {
        OrderItem(
            orderEntity = OrderEntity(
                orderId = "123888",
                purchaserName = "Cloud company",
                deliveryAddress = "15, Simphony St.",
                deliveryDate = "01.06.2024",
            )
        ) {}
    }
}