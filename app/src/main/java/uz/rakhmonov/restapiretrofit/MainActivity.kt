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
import uz.rakhmonov.restapiretrofit.models.MyReqDelete
import uz.rakhmonov.restapiretrofit.models.MyToDoRequest
import uz.rakhmonov.restapiretrofit.models.MyToDoX
import uz.rakhmonov.restapiretrofit.retrofit.APIclient

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), RV_adapter.RvAction {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var rvAdapter: RV_adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvAdapter = RV_adapter(this)
        binding.RV.adapter = rvAdapter
        loadData()

        binding.mySwipe.setOnRefreshListener {
            loadData()
        }
        binding.btnAddTodo.setOnClickListener {
            addToDo()

        }

    }

    private fun addToDo() {

        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = ItemDialogBinding.inflate(layoutInflater)
        dialogBinding.progresBar.visibility = View.GONE
        dialog.setView(dialogBinding.root)
        dialogBinding.btnSaveDialog.setOnClickListener {
            val mytodoRequest = MyToDoRequest(
                dialogBinding.tvTitle.text.toString(),
                dialogBinding.tvText.text.toString(),
                "yangi",
                dialogBinding.tvDeadline.text.toString()
            )
            dialogBinding.progresBar.visibility = View.VISIBLE
            APIclient.getApiService().addToDo(mytodoRequest)
                .enqueue(object : Callback<MyToDoX> {
                    override fun onResponse(call: Call<MyToDoX>, response: Response<MyToDoX>) {

                        if (response.isSuccessful) {
                            dialogBinding.progresBar.visibility = View.GONE
                            Toast.makeText(
                                this@MainActivity,
                                "${response.body()?.id} id bilan saqlandi",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    }

                    override fun onFailure(call: Call<MyToDoX>, t: Throwable) {
                        dialogBinding.progresBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Xatolik yuz berdi", Toast.LENGTH_SHORT)
                            .show()


                    }
                })

        }
        dialog.show()


    }

    fun loadData() {
        APIclient.getApiService().getAllToDo()
            .enqueue(object : Callback<List<MyToDoX>> {
                override fun onResponse(
                    call: Call<List<MyToDoX>>,
                    response: Response<List<MyToDoX>>,
                ) {
                    if (response.isSuccessful) {
                        binding.progresBar.visibility = View.GONE
                        rvAdapter.list.clear()
                        rvAdapter.list.addAll(response.body()!!)
                        rvAdapter.notifyDataSetChanged()
                        binding.mySwipe.isRefreshing = false

                    }
                }

                override fun onFailure(call: Call<List<MyToDoX>>, t: Throwable) {
                    rvAdapter.list.clear()
                    binding.progresBar.visibility = View.GONE

                    Toast.makeText(
                        this@MainActivity,
                        "Internetga ulanishni tekshiring",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.mySwipe.isRefreshing = false
                }
            })
    }

    override fun deleteTodo(myToDoX: MyToDoX) {
        APIclient.getApiService().deleteTodo(myToDoX.id)
            .enqueue(object : Callback<MyReqDelete> {
                override fun onResponse(call: Call<MyReqDelete>, response: Response<MyReqDelete>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity,
                            "Delete ${response.code()} : ${myToDoX.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadData()
                    } else {
                        Toast.makeText(this@MainActivity, "${response.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<MyReqDelete>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "O'chirishda Internet bilan muammo",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    override fun updateTodo(myToDoX: MyToDoX) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = ItemDialogBinding.inflate(layoutInflater)
        dialogBinding.spinnerStatus.visibility = View.VISIBLE
        dialogBinding.progresBar.visibility = View.GONE

        dialogBinding.apply {
            tvTitle.setText(myToDoX.sarlavha)
            tvDeadline.setText(myToDoX.oxirgi_muddat)
            tvText.setText(myToDoX.matn)
            when(myToDoX.holat){
                "yangi" -> spinnerStatus.setSelection(0)
                "bajarilmoqda" -> spinnerStatus.setSelection(1)
                "yakunlangan" -> spinnerStatus.setSelection(2)
            }
        }

        dialog.setView(dialogBinding.root)
        dialogBinding.btnSaveDialog.setOnClickListener {

            myToDoX.sarlavha = dialogBinding.tvTitle.text.toString()
            myToDoX.matn = dialogBinding.tvText.text.toString()
            myToDoX.holat = dialogBinding.spinnerStatus.selectedItem.toString()
            myToDoX.oxirgi_muddat = dialogBinding.tvDeadline.text.toString()

            APIclient.getApiService().updateTodo(
                myToDoX.id,
                MyToDoRequest(myToDoX.sarlavha, myToDoX.matn, myToDoX.holat, myToDoX.oxirgi_muddat)
            ).enqueue(object : Callback<MyToDoX>{
                override fun onResponse(call: Call<MyToDoX>, response: Response<MyToDoX>) {
                    Toast.makeText(
                        this@MainActivity,
                        "${response.body()} o'zgartirildi",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<MyToDoX>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "O'zgartirishda muammo", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
        dialog.show()
    }
}