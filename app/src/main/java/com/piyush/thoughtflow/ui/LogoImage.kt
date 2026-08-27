package com.piyush.thoughtflow.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.piyush.thoughtflow.R

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(R.drawable.ic_main),
        contentDescription = "ThoughtFlow Logo",
        modifier = Modifier.size(180.dp)
    )
}
