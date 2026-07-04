package com.inb.spendly.presentation.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.AirplaneTicket
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Liquor
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.LocalPizza
import androidx.compose.material.icons.outlined.LocalTaxi
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Redeem
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material.icons.outlined.Stroller
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.Toys
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.inb.spendly.R

object CategoryIcons {

    data class CategoryIconItem(
        val key: String,
        val icon: ImageVector
    )

    val icons: List<CategoryIconItem> = listOf(
        // Базовые (Filled)
        CategoryIconItem("card_travel", Icons.Filled.CardTravel),
        CategoryIconItem("event", Icons.Filled.Event),
        CategoryIconItem("games", Icons.Filled.Games),
        CategoryIconItem("hotel", Icons.Filled.Hotel),
        CategoryIconItem("music_note", Icons.Filled.MusicNote),
        CategoryIconItem("smartphone", Icons.Filled.Smartphone),

        // Общие (Outlined)
        CategoryIconItem("account_balance_wallet", Icons.Outlined.AccountBalanceWallet),
        CategoryIconItem("airplane_ticket", Icons.AutoMirrored.Outlined.AirplaneTicket),
        CategoryIconItem("attach_money", Icons.Outlined.AttachMoney),
        CategoryIconItem("book", Icons.AutoMirrored.Outlined.MenuBook),
        CategoryIconItem("build", Icons.Outlined.Build),
        CategoryIconItem("checkroom", Icons.Outlined.Checkroom),
        CategoryIconItem("cleaning", Icons.Outlined.CleaningServices),
        CategoryIconItem("coffee", Icons.Outlined.Coffee),
        CategoryIconItem("computer", Icons.Outlined.Computer),
        CategoryIconItem("credit_card", Icons.Outlined.CreditCard),
        CategoryIconItem("currency_exchange", Icons.Outlined.CurrencyExchange),
        CategoryIconItem("devices", Icons.Outlined.Devices),
        CategoryIconItem("diamond", Icons.Outlined.Diamond),
        CategoryIconItem("digital_wellbeing", Icons.Outlined.SelfImprovement),
        CategoryIconItem("directions_bus", Icons.Outlined.DirectionsBus),
        CategoryIconItem("directions_car", Icons.Outlined.DirectionsCar),
        CategoryIconItem("face", Icons.Outlined.Face),
        CategoryIconItem("family_group", Icons.Outlined.Groups),
        CategoryIconItem("fastfood", Icons.Outlined.Fastfood),
        CategoryIconItem("favorite", Icons.Outlined.Favorite),
        CategoryIconItem("gifts", Icons.Outlined.CardGiftcard),
        CategoryIconItem("fitness_center", Icons.Outlined.FitnessCenter),
        CategoryIconItem("flight", Icons.Outlined.Flight),
        CategoryIconItem("health_and_safety", Icons.Outlined.HealthAndSafety),
        CategoryIconItem("home", Icons.Outlined.Home),
        CategoryIconItem("image", Icons.Outlined.Image),
        CategoryIconItem("local_gas_station", Icons.Outlined.LocalGasStation),
        CategoryIconItem("local_hospital", Icons.Outlined.LocalHospital),
        CategoryIconItem("local_pizza", Icons.Outlined.LocalPizza),
        CategoryIconItem("local_taxi", Icons.Outlined.LocalTaxi),
        CategoryIconItem("medication", Icons.Outlined.Medication),
        CategoryIconItem("movie", Icons.Outlined.Movie),
        CategoryIconItem("payments", Icons.Outlined.Payments),
        CategoryIconItem("pet_supplies", Icons.Outlined.Pets),
        CategoryIconItem("question_mark", Icons.Outlined.QuestionMark),
        CategoryIconItem("restaurant", Icons.Outlined.Restaurant),
        CategoryIconItem("school", Icons.Outlined.School),
        CategoryIconItem("self_care", Icons.Outlined.Spa),
        CategoryIconItem("shopping_bag", Icons.Outlined.ShoppingBag),
        CategoryIconItem("shopping_cart", Icons.Outlined.ShoppingCart),
        CategoryIconItem("spa", Icons.Outlined.Spa),
        CategoryIconItem("sports_esports", Icons.Outlined.SportsEsports),
        CategoryIconItem("stadium", Icons.Outlined.Stadium),
        CategoryIconItem("subscriptions", Icons.Outlined.Subscriptions),
        CategoryIconItem("tools", Icons.Outlined.Handyman),
        CategoryIconItem("train", Icons.Outlined.Train),
        CategoryIconItem("wine_bar", Icons.Outlined.WineBar),
        CategoryIconItem("work", Icons.Outlined.Work),

        // Коммунальные услуги и Дом (Utilities & Home)
        CategoryIconItem("water_drop", Icons.Outlined.WaterDrop),
        CategoryIconItem("bolt", Icons.Outlined.Bolt),
        CategoryIconItem("wifi", Icons.Outlined.Wifi),
        CategoryIconItem("fire_department", Icons.Outlined.LocalFireDepartment),
        CategoryIconItem("key", Icons.Outlined.Key),
        CategoryIconItem("chair", Icons.Outlined.Chair),

        // Финансы и Доходы (Finance & Income)
        CategoryIconItem("savings", Icons.Outlined.Savings),
        CategoryIconItem("trending_up", Icons.AutoMirrored.Outlined.TrendingUp),
        CategoryIconItem("receipt_long", Icons.AutoMirrored.Outlined.ReceiptLong),
        CategoryIconItem("account_balance", Icons.Outlined.AccountBalance),
        CategoryIconItem("security", Icons.Outlined.Security),

        // Дети и Хобби (Kids & Hobbies)
        CategoryIconItem("child_care", Icons.Outlined.ChildCare),
        CategoryIconItem("toys", Icons.Outlined.Toys),
        CategoryIconItem("stroller", Icons.Outlined.Stroller),
        CategoryIconItem("palette", Icons.Outlined.Palette),
        CategoryIconItem("photo_camera", Icons.Outlined.PhotoCamera),
        CategoryIconItem("florist", Icons.Outlined.LocalFlorist),

        // Транспорт (Transport)
        CategoryIconItem("parking", Icons.Outlined.LocalParking),
        CategoryIconItem("car_repair", Icons.Outlined.CarRepair),
        CategoryIconItem("pedal_bike", Icons.Outlined.PedalBike),

        // Еда и Подарки (Food & Gifts)
        CategoryIconItem("grocery_store", Icons.Outlined.LocalGroceryStore),
        CategoryIconItem("bakery_dining", Icons.Outlined.BakeryDining),
        CategoryIconItem("liquor", Icons.Outlined.Liquor),
        CategoryIconItem("redeem", Icons.Outlined.Redeem),
        CategoryIconItem("volunteer_activism", Icons.Outlined.VolunteerActivism)
    )

    const val DEFAULT_ICON_KEY = "image"

    fun getIconByKey(key: String): ImageVector {
        return icons.find { it.key == key }?.icon ?: Icons.Outlined.Image
    }
}