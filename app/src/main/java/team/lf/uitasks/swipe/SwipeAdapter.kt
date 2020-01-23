package team.lf.uitasks.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import team.lf.uitasks.R
import team.lf.uitasks.swipe.model.User
import java.net.UnknownServiceException

class SwipeAdapter : RecyclerView.Adapter<SwipeAdapter.SwipeItemViewHolder>() {

    private val items = ArrayList<User>()

    class SwipeItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            view.findViewById<TextView>(R.id.id).text = user.id.toString()
            view.findViewById<TextView>(R.id.name).text = user.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false)
        return SwipeItemViewHolder(view)
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: SwipeItemViewHolder, position: Int) {
        holder.bind(items[position])

    }

    fun addItems(items:List<User>){
        this.items.addAll(items)
    }

    companion object{
        fun populateItems():List<User> =
            listOf(User(1,"Donald"), User(2,"Tom"), User(3, "Jerry"), User(4, "Mickey"))
    }

}