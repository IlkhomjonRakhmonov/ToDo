package uz.rakhmonov.restapiretrofit.models

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

class MyToDoX{
    var id: Int=0
    var sarlavha: String=""
//    var matn: String=""
    var holat: String=""
    var oxirgi_muddat: String=""

    constructor(id: Int, sarlavha: String, holat: String, oxirgi_muddat: String) {
        this.id = id
        this.sarlavha = sarlavha
        this.holat = holat
        this.oxirgi_muddat = oxirgi_muddat

    }

    constructor(sarlavha: String, holat: String, oxirgi_muddat: String) {
        this.sarlavha = sarlavha
        this.holat = holat
        this.oxirgi_muddat = oxirgi_muddat
    }

}