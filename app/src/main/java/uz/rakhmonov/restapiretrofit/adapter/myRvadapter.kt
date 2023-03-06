package uz.rakhmonov.restapiretrofit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.rakhmonov.restapiretrofit.databinding.RvItemBinding
import uz.rakhmonov.restapiretrofit.models.MyToDoX

class RV_adapter (val list:ArrayList<MyToDoX> = ArrayList()):RecyclerView.Adapter<RV_adapter.VH>(){

    inner class VH (val rvItemBinding: RvItemBinding): RecyclerView.ViewHolder(rvItemBinding.root){
        fun onHolder(myToDoX: MyToDoX, position:Int){
            rvItemBinding.name.text=myToDoX.sarlavha
            rvItemBinding.status.text=myToDoX.holat
            rvItemBinding.deadline.text=myToDoX.oxirgi_muddat
            rvItemBinding.tvMatn.text=myToDoX.matn



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onHolder(list[position],position)

    }

    override fun getItemCount(): Int=list.size




}