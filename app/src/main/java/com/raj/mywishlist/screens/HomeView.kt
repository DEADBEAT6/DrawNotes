package com.raj.mywishlist.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.raj.mywishlist.R
import com.raj.mywishlist.WishViewModel
import com.raj.mywishlist.data.Wish
import com.raj.mywishlist.navigation.Screen
import com.raj.mywishlist.screens.components.AppBarView
import com.raj.mywishlist.screens.components.TwoColumnGridScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val wishList by viewModel.getAllWishes.collectAsState(initial = emptyList())
    val filteredWishList = if (searchText.isEmpty()) {
        wishList
    } else {
        wishList.filter { it.title.contains(searchText, ignoreCase = true) || it.description.contains(searchText,ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            AppBarView(

                title = "Doodle Notes",
                searchText = searchText,
                onSearchTextChanged = {
                    searchText = it
                },

                onActiveChange = { active = it },
                onBackNavClicked = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                backgroundColor = Color.Black,
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sharp_heart_broken_24),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    ) {
        TwoColumnGridScreen(
            wishList = filteredWishList,
            navController = navController,
            viewModel = viewModel,
            padding = it
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishItem(wish: Wish, onClick: () -> Unit) {
    val state = rememberRichTextState()
    state.setHtml(wish.description)
    val colors = listOf(
        colorResource(id = R.color.card_color1),
        colorResource(id = R.color.card_color2),
        colorResource(id = R.color.card_color3),
        colorResource(id = R.color.card_color4),
        colorResource(id = R.color.card_color5),

        )
    val personalColor = wish.id.toInt() % colors.size
    // Select a random color
    val color = colors[personalColor]
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            // .height(100.dp)
            // .width(100.dp)

            .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
            .clickable {
                onClick()
            }, elevation = 10.dp, backgroundColor = color
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .padding(start = 5.dp)
        ) {
            Text(text = wish.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            val a = 10
            RichTextEditor(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                textStyle = TextStyle(Color.Black),
                colors = RichTextEditorDefaults.richTextEditorColors(containerColor = Color.Transparent),
                enabled = false,
                readOnly = true,
                contentPadding = RichTextEditorDefaults.richTextEditorWithoutLabelPadding(
                    start = 4.dp, end = a.dp, top = a.dp, bottom = a.dp
                )

            )

        }
    }

}
