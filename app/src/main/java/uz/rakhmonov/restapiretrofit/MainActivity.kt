package uz.rakhmonov.restapiretrofit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.rakhmonov.restapiretrofit.adapter.RV_adapter
import uz.rakhmonov.restapiretrofit.databinding.ActivityMainBinding
import uz.rakhmonov.restapiretrofit.databinding.ItemDialogBinding
import uz.rakhmonov.restapiretrofit.models.MyToDoX
import uz.rakhmonov.restapiretrofit.retrofit.APIclient
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), RV_adapter.RvAction {
    
    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var rvAdapter: RV_adapter
    private var myCalendar:Calendar=Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rvAdapter=RV_adapter(this)
        binding.RV.adapter=rvAdapter
        loadData()

        binding.mySwipe.setOnRefreshListener {
            loadData()
        }
        binding.btnAddTodo.setOnClickListener {
            addToDo()

        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun addToDo() {

            val dialog=AlertDialog.Builder(this).create()
            val  dialogBinding=ItemDialogBinding.inflate(layoutInflater)
        dialogBinding.progresBar.visibility=View.GONE
            dialog.setView(dialogBinding.root)

            dialogBinding.calendar.setOnClickListener {
              DatePickerDialog(this,object :OnDateSetListener{
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        myCalendar.set(Calendar.YEAR,year)
                        myCalendar.set(Calendar.MONTH,month)
                        myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                        val myFormat = "dd/MM/yyyy"
                        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                        dialogBinding.tvDeadline.setText(dateFormat.format(myCalendar.time))

                    }
                },2023,3,10)
                    .show()
            }


            dialogBinding.btnSaveDialog .setOnClickListener {
                if (dialogBinding.tvTitle.text.isNotEmpty() && dialogBinding.tvDeadline.text.isNotEmpty()){
                    
                val myToDoX= MyToDoX(
                    dialogBinding.tvTitle.text.toString(),
//                    dialogBinding.tvText.text.toString(),
                    dialogBinding.spinnerStatus.selectedItem.toString(),
                    dialogBinding.tvDeadline.text.toString()


                )
                dialogBinding.progresBar.visibility=View.VISIBLE
                APIclient.getApiService().addToDo(myToDoX)
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
                loadData()

            }else{
                    Toast.makeText(this, "Iltimos, barcha qatorlarni to'ldiring", Toast.LENGTH_SHORT).show()
                }
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

     fun deleteTodo(myToDoX: MyToDoX) {
        APIclient.getApiService().deleteTodo(myToDoX.id)
            .enqueue(object :Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@MainActivity, "${myToDoX.id} id dagi ochirildi", Toast.LENGTH_SHORT).show()
                        loadData()
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "xatolik boldi", Toast.LENGTH_SHORT).show()
                }
            })
    }

    @SuppressLint("SuspiciousIndentation")
     fun updateTodo(myToDoX: MyToDoX) {
        val dialog=AlertDialog.Builder(this).create()
        val dialogBinding=ItemDialogBinding.inflate(layoutInflater)
        dialogBinding.progresBar.visibility=View.GONE
        dialog.setView(dialogBinding.root)

        dialogBinding.tvTitle.setText(myToDoX.sarlavha)
//        dialogBinding.tvText.setText(myToDoX.matn)
        dialogBinding.tvDeadline.setText(myToDoX.oxirgi_muddat)
        when(myToDoX.holat){
            "yangi"->dialogBinding.spinnerStatus.setSelection(0)
            "bajarilmoqda"->dialogBinding.spinnerStatus.setSelection(1)
            "yakunlangan"->dialogBinding.spinnerStatus.setSelection(2)
        }
        dialogBinding.btnSaveDialog.setOnClickListener {
            loadData()
            if (dialogBinding.tvTitle.text.isNotEmpty() && dialogBinding.tvDeadline.text.isNotEmpty() ){

            myToDoX.sarlavha=dialogBinding.tvTitle.text.toString()
//            myToDoX.matn=dialogBinding.tvText.text.toString()
            myToDoX.oxirgi_muddat=dialogBinding.tvDeadline.text.toString()
            myToDoX.holat=dialogBinding.spinnerStatus.selectedItem.toString()
                
        dialogBinding.progresBar.visibility=View.VISIBLE
            APIclient.getApiService().updateToDo(myToDoX.id, MyToDoX(myToDoX.sarlavha,myToDoX.holat,myToDoX.oxirgi_muddat))
                .enqueue(object :Callback<MyToDoX>{
                    override fun onResponse(call: Call<MyToDoX>, response: Response<MyToDoX>) {
                        if (response.isSuccessful){
                            Toast.makeText(this@MainActivity, "${myToDoX.id} id o'zgartirildi", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            dialogBinding.progresBar.visibility=View.GONE
                        }
                    }

                    override fun onFailure(call: Call<MyToDoX>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                        dialogBinding.progresBar.visibility=View.GONE

                    }
                })
        }else{
                Toast.makeText(this, "Iltimos, barcha qatorlarni to'ldiring", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

    }

    override fun onClick(myToDoX: MyToDoX,position:Int,imageView: ImageView) {
        val popupMenu=PopupMenu(this,imageView)
        popupMenu.inflate(R.menu.popup_item)
        popupMenu.setOnMenuItemClickListener {
        when(it.itemId){
            R.id.delete->{
                deleteTodo(myToDoX)

            }
            R.id.update->{
                updateTodo(myToDoX)

            }

        }
true
        }
        popupMenu.show()


    }
}