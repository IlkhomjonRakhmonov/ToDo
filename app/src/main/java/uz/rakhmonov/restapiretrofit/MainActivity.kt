package uz.rakhmonov.restapiretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.rakhmonov.restapiretrofit.adapter.RV_adapter
import uz.rakhmonov.restapiretrofit.databinding.ActivityMainBinding
import uz.rakhmonov.restapiretrofit.databinding.ItemDialogBinding
import uz.rakhmonov.restapiretrofit.models.MyToDoRequest
import uz.rakhmonov.restapiretrofit.models.MyToDoX
import uz.rakhmonov.restapiretrofit.retrofit.APIclient

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    
    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var rvAdapter: RV_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvAdapter=RV_adapter()
        binding.RV.adapter=rvAdapter
        loadData()

        binding.mySwipe.setOnRefreshListener {
            loadData()
        }
        binding.btnAddTodo.setOnClickListener {
            addToDo()

        }

    }

    private fun addToDo() {

            val dialog=AlertDialog.Builder(this).create()
            val  dialogBinding=ItemDialogBinding.inflate(layoutInflater)
        dialogBinding.progresBar.visibility=View.GONE
            dialog.setView(dialogBinding.root)
            dialogBinding.btnSaveDialog .setOnClickListener {
                val mytodoRequest= MyToDoRequest(
                    dialogBinding.tvTitle.text.toString(),
                    dialogBinding.tvText.text.toString(),
                    "yangi",
                    dialogBinding.tvDeadline.text.toString())
                dialogBinding.progresBar.visibility=View.VISIBLE
                APIclient.getApiService().addToDo(mytodoRequest)
                    .enqueue(object :Callback<MyToDoX>{
                        override fun onResponse(call: Call<MyToDoX>, response: Response<MyToDoX>) {
                          
                                if (response.isSuccessful){
                                    dialogBinding.progresBar.visibility=View.GONE
                                    Toast.makeText(this@MainActivity, "${response.body()?.id} id bilan saqlandi", Toast.LENGTH_SHORT).show()
                                    dialog.dismiss()
                                }
                        }
                        override fun onFailure(call: Call<MyToDoX>, t: Throwable) {
                            dialogBinding.progresBar.visibility=View.GONE
                            Toast.makeText(this@MainActivity, "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()



                        }
                    })

            }
            dialog.show()



    }

    fun loadData(){
        APIclient.getApiService().getAllToDo()
            .enqueue(object :Callback<List<MyToDoX>>{
                override fun onResponse(
                    call: Call<List<MyToDoX>>,
                    response: Response<List<MyToDoX>>
                ) {
                    if (response.isSuccessful){
                        binding.progresBar.visibility= View.GONE
                        rvAdapter.list.clear()
                        rvAdapter.list.addAll(response.body()!!)
                        rvAdapter.notifyDataSetChanged()
                        binding.mySwipe.isRefreshing=false

                    }
                }

                override fun onFailure(call: Call<List<MyToDoX>>, t: Throwable) {
                    rvAdapter.list.clear()
                    binding.progresBar.visibility= View.GONE

                    Toast.makeText(this@MainActivity, "Internetga ulanishni tekshiring", Toast.LENGTH_SHORT).show()
                    binding.mySwipe.isRefreshing=false
                }
            })
    }
}