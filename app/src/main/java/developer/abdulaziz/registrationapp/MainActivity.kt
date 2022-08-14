package developer.abdulaziz.registrationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import developer.abdulaziz.registrationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: MyAdapter
    private lateinit var list: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Menu"
        firebaseFirestore = FirebaseFirestore.getInstance()
        list = ArrayList()
        binding.add.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("key", "add")
            startActivity(intent)
            finish()
        }
        adapter = MyAdapter(list, object : MyAdapter.MyClick {
            override fun onClick(user: User, position: Int) {
                val intent = Intent(this@MainActivity, MainActivity2::class.java)
                intent.putExtra("key", "edit")
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }

            override fun onLongClick(user: User, position: Int) {
                val a = AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("Delete")
                    setMessage("Do you want to delete this Information ?")
                    setPositiveButton("Yes") { _, _ ->
                        firebaseFirestore.collection("Users").document(user.uid!!).delete()
                            .addOnSuccessListener {
                                Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                    setNegativeButton("No") { _, _ -> }
                }
                a.show()
            }
        })
        binding.rv.adapter = adapter
        firebaseFirestore.collection("Users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.forEach {
                        val value = it.toObject(User::class.java)
                        list.add(value)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
}