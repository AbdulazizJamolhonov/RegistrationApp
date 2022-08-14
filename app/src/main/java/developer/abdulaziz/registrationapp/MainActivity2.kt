package developer.abdulaziz.registrationapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import developer.abdulaziz.registrationapp.databinding.ActivityMain2Binding
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var firebaseFirestore: FirebaseFirestore

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()

        when (intent.getStringExtra("key")) {
            "add" -> {
                title = "Add"
                binding.save.setOnClickListener {
                    val name = binding.name.text.toString()
                    val number = binding.number.text.toString()
                    if (name.isNotEmpty() && number.isNotEmpty()) {
                        val time = LocalTime.now().toString().substring(0, 5)
                        val date = SimpleDateFormat("dd/MM").format(Date())
                        val user = User(name, number, time, date)
                        firebaseFirestore.collection("Users")
                            .add(user)
                            .addOnSuccessListener {
                                val uid = firebaseFirestore.collection("Users").document(it.id)
                                val map = mapOf("uid" to it.id)
                                uid.update(map)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                    } else Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                }
            }
            "edit" -> {
                val user = intent.getSerializableExtra("user") as User
                binding.name.setText(user.name)
                binding.number.setText(user.number)
                binding.save.setOnClickListener {
                    val name = binding.name.text.toString()
                    val number = binding.number.text.toString()
                    if (name.isNotEmpty() && number.isNotEmpty()) {
                        val time = LocalTime.now().toString().substring(0, 5)
                        val date = SimpleDateFormat("dd/MM").format(Date())
                        val map = mapOf(
                            "name" to name,
                            "number" to number,
                            "time" to time,
                            "date" to date
                        )
                        firebaseFirestore.collection("Users").document(user.uid!!).update(map)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                    } else Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}