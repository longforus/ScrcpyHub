package view.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.entity.Device
import resource.Images
import resource.Strings

@Composable
fun DeviceCard(
    device: Device,
    isRunning: Boolean,
    startScrcpy: ((Device) -> Unit)? = null,
    stopScrcpy: ((Device) -> Unit)? = null,
    goToDetail: ((Device) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(horizontal = 8.dp).height(48.dp)) {
            Image(
                painter = painterResource(Images.DEVICE),
                contentDescription = Images.DEVICE,
                contentScale = ContentScale.Inside,
                modifier = Modifier.width(32.dp).align(Alignment.CenterVertically).padding(end = 4.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(fraction = 0.65f).align(Alignment.CenterVertically)
            ) {
                Text(
                    device.displayName,
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Text(
                    device.id,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = androidx.compose.ui.graphics.Color.Gray,
                )
            }

            Button(
                onClick = { if (!isRunning) startScrcpy?.invoke(device) else stopScrcpy?.invoke(device) },
                modifier = Modifier.wrapContentHeight().width(80.dp).align(Alignment.CenterVertically)
            ) {
                Text(
                    text = if (!isRunning) Strings.DEVICES_PAGE_START else Strings.DEVICES_PAGE_STOP,
                    style = MaterialTheme.typography.button
                )
            }

            Image(
                painter = painterResource(Images.DOTS),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        goToDetail?.invoke(device)
                    }
            )
        }
    }
}

@Preview
@Composable
private fun DeviceCard_Preview() {
    DeviceCard(Device("ID", "NAME"), false)
}

@Preview
@Composable
private fun DeviceCard_Preview_Overflow() {
    val id = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
    val name = "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
    DeviceCard(Device(id, name), false)
}