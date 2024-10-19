package com.example.sahara

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sahara.ui.theme.nunito

@Composable
fun Food(
    viewModel: MainViewModel
) {
    val scrollState = rememberScrollState()
    val food = viewModel.foodState.food
    var ingredients by remember {
        mutableStateOf(false)
    }
    var additives by remember {
        mutableStateOf(false)
    }
    var allergens by remember {
        mutableStateOf(false)
    }
    var certifications by remember {
        mutableStateOf(false)
    }
    var nutrition by remember {
        mutableStateOf(false)
    }
    val comp by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
        R.raw.food
    ))
    val comp2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.search
        ))
    if(food == null && !viewModel.foodState.isUploading){
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sahaara",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = nunito,
                color = Color(0xff0080c7)
            )
            LottieAnimation(
                composition = comp,
                iterations = LottieConstants.IterateForever
            )
        }
    }
    if(food == null && viewModel.foodState.isUploading){
        Column(
            modifier = Modifier
                .fillMaxSize().padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sahaara",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = nunito,
                color = Color(0xff0080c7)
            )
            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = comp2,
                iterations = LottieConstants.IterateForever
            )
            val animatedProgress by animateFloatAsState(
                targetValue = viewModel.foodState.progress,
                animationSpec = tween(durationMillis = 100),
                label = "File upload progress bar"
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }

    }
    food?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            viewModel.foodImageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(250.dp)
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                ) {
                    Text(
                        text = "Name: ${food.name.toString()}",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Brand: ${food.brand.toString()}",
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "Origin : ${food.origin_country.toString()}",
                        fontSize = 20.sp,
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (food.vegan_friendly == "yes") {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Vegan\nFriendly",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (food.keto_friendly == "yes") {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Keto\nFriendly",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (food.diabetic_friendly == "yes") {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Diabetic\nFriendly",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = food.description.toString(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (!food.ingredients.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { ingredients = !ingredients }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ingredients",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(15.dp)
                            )
                            Icon(
                                imageVector = if (!ingredients) {
                                    Icons.Rounded.ArrowDropDown
                                } else {
                                    Icons.Rounded.ArrowDropUp
                                },
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = ingredients
                        ) {
                            Column {
                                food.ingredients.forEach { ingred ->
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth().padding(horizontal = 5.dp)
                                            .padding(vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = ingred,
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.size(5.dp))
                            }
                        }
                    }
                }
            }
            if (!food.additives.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { additives = !additives }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Additives",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(15.dp)
                            )
                            Icon(
                                imageVector = if (!additives) {
                                    Icons.Rounded.ArrowDropDown
                                } else {
                                    Icons.Rounded.ArrowDropUp
                                },
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = additives
                        ) {
                            Column {
                                food.additives!!.forEach { ingred ->
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth().padding(horizontal = 5.dp)
                                            .padding(vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = ingred,
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.size(5.dp))
                            }
                        }
                    }
                }
            }
            if (!food.allergens.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { allergens = !allergens }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Allergens",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(15.dp)
                            )
                            Icon(
                                imageVector = if (!allergens) {
                                    Icons.Rounded.ArrowDropDown
                                } else {
                                    Icons.Rounded.ArrowDropUp
                                },
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = allergens
                        ) {
                            Column {
                                food.allergens!!.forEach { ingred ->
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth().padding(horizontal = 5.dp)
                                            .padding(vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = ingred,
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.size(5.dp))
                            }
                        }
                    }
                }
            }
            if (!food.certifications.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { certifications = !certifications }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Certifications",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(15.dp)
                            )
                            Icon(
                                imageVector = if (!certifications) {
                                    Icons.Rounded.ArrowDropDown
                                } else {
                                    Icons.Rounded.ArrowDropUp
                                },
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = certifications
                        ) {
                            food.certifications.forEach { ingred ->
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = ingred,
                                            fontSize = 17.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }

                        }
                    }
                }
            }
            if (food.nutritional_info != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { nutrition = !nutrition }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Nutritional Info",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(15.dp)
                            )
                            Icon(
                                imageVector = if (!nutrition) {
                                    Icons.Rounded.ArrowDropDown
                                } else {
                                    Icons.Rounded.ArrowDropUp
                                },
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                        AnimatedVisibility(
                            visible = nutrition
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Calories: ${food.nutritional_info.calories.toString()}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Serving Size: ${food.nutritional_info?.serving_size}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Text(
                                    text = "Macronutrients",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Fat: ${food.nutritional_info?.macronutrients?.fat}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Protein: ${food.nutritional_info?.macronutrients?.protein}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Carbohydrates: ${food.nutritional_info?.macronutrients?.carbohydrates}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Text(
                                    text = "Micronutrients",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Calcium: ${food.nutritional_info?.micronutrients?.calcium}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Vitamin D: ${food.nutritional_info?.micronutrients?.vitamin_d}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth().padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Vitamin E: ${food.nutritional_info?.micronutrients?.vitamin_e}",
                                        fontSize = 17.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(Modifier.size(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}