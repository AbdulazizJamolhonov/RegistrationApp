package developer.abdulaziz.registrationapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.abdulaziz.registrationapp.databinding.ItemRvBinding

class MyAdapter(private val list: ArrayList<User>, private val myClick: MyClick) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User, position: Int) {
            binding.name.text = user.name
            binding.number.text = user.number
            binding.time.text = user.time
            binding.date.text = user.date
            binding.item.setOnClickListener { myClick.onClick(user, position) }
            binding.item.setOnLongClickListener { myClick.onLongClick(user, position); true }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(hol: ViewHolder, pos: Int) = hol.onBind(list[pos], pos)
    override fun getItemCount(): Int = list.size
    interface MyClick {
        fun onClick(user: User, position: Int)
        fun onLongClick(user: User, position: Int)
    }
}