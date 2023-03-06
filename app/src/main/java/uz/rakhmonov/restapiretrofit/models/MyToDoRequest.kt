package uz.rakhmonov.restapiretrofit.models

data class MyToDoRequest(
    val sarlavha: String,
    val matn: String,
    val holat: String,
    val oxirgi_muddat: String
)