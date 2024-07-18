package com.raj.mywishlist.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.raj.mywishlist.Drawing
import com.raj.mywishlist.R
import com.raj.mywishlist.WishViewModel
import com.raj.mywishlist.convertJsonStringToList
import com.raj.mywishlist.data.Wish
import com.raj.mywishlist.screens.components.AppBarView

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val snackMessage = remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()
    val isButtonClicked = remember { mutableStateOf(false) }
    if (id != 0L) {
        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", "", ""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
        viewModel.wishDrawingState = wish.value.drawingJson
        viewModel.wishdrawlist = convertJsonStringToList(wish.value.drawingJson)
    } else {
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
        viewModel.wishDrawingState = ""
        viewModel.wishdrawlist = emptyList()
    }
    val state = rememberRichTextState()


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            AppBarView(
                title = if (id != 0L) stringResource(id = R.string.update_wish)
                else stringResource(id = R.string.add_wish),
                onBackNavClicked = { navController.navigateUp() },
                showSearchBar = false,
                searchText = ""
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Spacer(modifier = Modifier.height(10.dp))
            WishTextField(
                modifier = Modifier
                    .padding(3.dp)
                    .padding(start = 10.dp, end = 12.dp),
                label = "Title",
                value = viewModel.wishTitleState,
                onValueChanged = {
                    viewModel.onWishTitleChange(it)
                }
            )

            Drawing(
                viewModel = viewModel, modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), state, id = id
            )


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (!isButtonClicked.value) {

                    if (viewModel.wishTitleState.isNotEmpty()) {
                        isButtonClicked.value = true
                        if (id != 0L) {

                            viewModel.updateWish(
                                Wish(
                                    id = id,
                                    title = viewModel.wishTitleState.trim(),
                                    description = state.toHtml(),
                                    drawingJson = viewModel.wishDrawingState
                                )
                            )
                            snackMessage.value = "Note Has been Updated Complete!! "
                            navController.navigateUp()

                        } else {

                            viewModel.addWish(
                                Wish(
                                    title = viewModel.wishTitleState.trim(),
                                    description = state.toHtml(),
                                    drawingJson = viewModel.wishDrawingState
                                )
                            )
                            snackMessage.value = "Note Has been Created !! "
                            navController.navigateUp()
                        }

                    } else {
                        snackMessage.value = "Title cannot be blank ! "

                    }
                    Toast.makeText(context, snackMessage.value, Toast.LENGTH_SHORT).show()

                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                ),
                shape = RoundedCornerShape(0f)
            ) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = if (id != 0L) stringResource(id = R.string.update_wish)
                        else stringResource(id = R.string.add_wish),
                        style = TextStyle(fontSize = 18.sp)
                    )
                }

            }
        }
    }
}

@Composable
fun WishTextField(
    modifier: Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black),
        )

    )
}

@Preview
@Composable
fun WishTextFieldPrev() {
    WishTextField(label = "label", value = " text", onValueChanged = {}, modifier = Modifier)

}
