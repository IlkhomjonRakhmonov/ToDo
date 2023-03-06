package uz.rakhmonov.restapiretrofit.models

data class MyToDoRequest(
    var sarlavha: String,
    var matn: String,
    var holat: String,
    var oxirgi_muddat: String
)