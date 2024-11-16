package com.example.onlineshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.onlineshop.Model.CategoryModel
import com.example.onlineshop.Model.SliderModel
import com.example.onlineshop.ViewModel.MainViewModel
import com.example.onlineshop.ui.theme.OnLineShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
MainActivityScreen()
        }
    }
}

@Composable
@Preview
fun MainActivityScreen(){
    val viewModel = MainViewModel()
    val banners = remember { mutableStateListOf<SliderModel>() }
    val categories = remember { mutableStateListOf<CategoryModel>() }

    var showBannerLoading by remember { mutableStateOf(true) }
    var showCategoryLoading by remember { mutableStateOf(true) }

    // Banner
    LaunchedEffect(Unit) {
        viewModel.loadBanners()
        viewModel.banners.observeForever {
            banners.clear()
            banners.addAll(it)
            showBannerLoading = false
        }

    }

    // Category
    LaunchedEffect(Unit) {
        viewModel.loadCategory()
        viewModel.categories.observeForever {
            categories.clear()
            categories.addAll(it)
            showCategoryLoading = false
        }
    }

    ConstraintLayout(modifier = Modifier.background(Color.White)){
        val (scrollList, bottomMenu)=createRefs()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(scrollList){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Welcome Back", color = Color.Black)

                        Text(
                            "Jackie",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row {
                        Image(
                            painter = painterResource(R.drawable.fav_icon),
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "",
                        )
                    }

                }
            }
            //Banner
            item{
                if (showBannerLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center

                    ){
                        CircularProgressIndicator()

                    }
                }else{
                    Banners(banners)

                }

            }
            item{
                SectionTitle(title = "Categories", actionText = "See All")

            }
            item{
                if(showCategoryLoading){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator()

                    }

                }else{
                    CartegoryList(categories)

                }

            }
        }
    }

}

@Composable
fun CartegoryList(categories: SnapshotStateList<CategoryModel>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 8.dp)
    ) {
        items(categories.size) { index ->
            CategoryItem(item = categories[index],
                isSelected = index == selectedIndex,
                onItemClick = {
                    selectedIndex = index
                }

                )
        }

    }

}

@Composable
fun CategoryItem(item: CategoryModel, isSelected: Boolean, onItemClick : ()-> Unit) {
    Row(modifier = Modifier
        .clickable (onClick= onItemClick )
        .background(color = if(isSelected) colorResource(R.color.purple)else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        ),
        verticalAlignment = Alignment.CenterVertically


    ){
        AsyncImage(
            model = (item.picUrl),

            contentDescription = item.title,

            modifier = Modifier
                .size(45.dp)
                .background(
                    color = if (isSelected) Color.Transparent else colorResource(R.color.lightGrey),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Inside,
            colorFilter = if(isSelected){
                ColorFilter.tint(Color.White)
                }else{
                ColorFilter.tint(Color.Black)

            }

        )

if(isSelected){
    Text(text = item.title,
        color = Color.White,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(end = 8.dp)
    )
}

    }

}


@Composable
fun Banners(banners: List<SliderModel>) {
    AutoSlidingCarousel(banners = banners)
    

}

@Composable
fun AutoSlidingCarousel(modifier:Modifier=Modifier,
                        banners: List<SliderModel>,
                        pagerState: PagerState = rememberPagerState(initialPage = 0, pageCount = {banners.size}),
                        ) {

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Column( modifier = modifier.fillMaxWidth()) {
        HorizontalPager(  state = pagerState) {
            page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(banners[page].url)
                    //.crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top=16.dp, bottom = 8.dp)
                    .height(150.dp)

            )

            }
         DotIndicator(
             modifier = Modifier
                 .align(Alignment.CenterHorizontally)
                 .padding(top = 16.dp),
             totalDots = banners.size,
             selectedIndex = if(isDragged) pagerState.currentPage else pagerState.currentPage,
             dotSize = 8.dp
         )
        }

}
@Composable
fun DotIndicator(
    modifier: Modifier=Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = colorResource(R.color.purple),
    unSelectedColor: Color = colorResource(R.color.grey),
    dotSize: Dp

){
    LazyRow (
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ){
        items(totalDots){
            index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }

        }
    }

}

@Composable
fun IndicatorDot(modifier: Modifier = Modifier,
                 size :Dp,
                 color: Color
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color))
}
@Composable
fun SectionTitle(title:String, actionText: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start= 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Text(text = title,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = actionText,
            color = colorResource(R.color.purple),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

    }

}
