package com.example.mobiiliprojektir9

//Sisältää navigaatioreitit

sealed class Screens(val route : String)
{
    object Login : Screens(route = "login_screen")
    object RegisterAs : Screens(route = "register_as_screen")
    object RegisterDriver : Screens(route = "register_driver")
    object RegisterOrganizer : Screens(route = "register_organizer")
    object DriverSite : Screens(route = "driver_site")
    object CreateJob : Screens(route = "create_job_site")
    object OpenOrders : Screens(route = "open_orders")
    object JobDelivered : Screens(route = "job_delivered")
    object JobHistory : Screens(route = "job_history")
    object JobHistoryCompany : Screens(route = "job_history_company")
    object EditCoordInfo : Screens(route = "edit_coord_info")
    object EditUserInfo : Screens(route = "edit_user_info")
}
