package com.example.recycleviewwithclicklistener

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class WelcomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)

        Handler().postDelayed({
            val intent = Intent(this@WelcomePage, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}

class MainActivity : AppCompatActivity() {

    lateinit var imageBitmap: Bitmap
    private lateinit var recyclerView: RecyclerView
    private lateinit var mangrovelist: ArrayList<Mangrove>
    private lateinit var mangroveAdapter:MangroveAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    recyclerView.visibility = View.VISIBLE
                    true
                }
                R.id.navigation_gallery -> {
                    val intent =
                        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, 100)
                    true
                }
                R.id.navigation_camera -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 123)
                    true
                }
                else -> false
            }
        }

        init()
    }
    private fun init() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        mangrovelist = ArrayList()
        addDataToList()
        mangroveAdapter = MangroveAdapter(mangrovelist)
        recyclerView.adapter = mangroveAdapter

        mangroveAdapter.onItemClick = {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("mangrove", it)
            startActivity(intent)
        }

    }

    private fun addDataToList(){
        mangrovelist.add(Mangrove(R.drawable.amarina, "Avicennia marina", "Api-api Jambu","Unlike other species, the young branches of Api-api Jambu is distinctly square shaped. The fruit is greyish green with a short beak at the tip.","Salt-tolerant mangrove with multiple ecological benefits."))
        mangrovelist.add(Mangrove(R.drawable.aofficinalis, "Avicennia officinalis", "Api Api Ludat", "It has large orange-yellow flowers that smell rancid. The leaves are oblong shaped and the underside are distinctly yellowish green.", "Medicinal, salt-tolerant mangrove species."))
        mangrovelist.add(Mangrove(R.drawable.bsexangula, "Bruguiera sexangula","Tumu Mata Buaya" , "A mangrove tree species that is commonly found in the coastal regions of Southeast Asia, including India, Bangladesh, and Sri Lanka. It is a salt-tolerant plant that grows in mudflats and tidal creeks and provides important habitats for various species of wildlife, including fish, crustaceans, and birds. B. sexangula is also known for its strong, durable wood and is used for construction, fuel, and other purposes.", "Dense, saltwater-tolerant mangrove species."))
        mangrovelist.add(Mangrove(R.drawable.rapiculata, "Rhizophora apiculata","Bakau Minyak" ,"A species of mangrove tree native to Southeast Asia. It is commonly found in tidal mudflats and intertidal zones, and is known for its remarkable ability to survive in harsh coastal environments. The tree produces a red fruit that is important to wildlife, and provides essential habitat and protection for many species of marine life.", "Mangrove tree with red aerial roots."))
        mangrovelist.add(Mangrove(R.drawable.scaseolaris, "Sonneratia caseolaris", "Berembang", "A mangrove plant species with aerial prop roots, leathery leaves, and fragrant white flowers that bloom at night. It has salt-tolerant adaptations and is used in traditional medicine and as a source of food and fuel in coastal areas.", "Salt-tolerant mangrove plant with edible fruit"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap

            // Create a new fragment and pass the imageBitmap to it
            val fragment = fragment_result.newInstance(imageBitmap)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
            recyclerView.visibility = View.GONE
        }
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data

            // Load the image from the Uri into a Bitmap object
            val imageStream = selectedImageUri?.let { contentResolver.openInputStream(it) }
            val imageBitmap = BitmapFactory.decodeStream(imageStream)

            // Create a new fragment and pass the imageBitmap to it
            val fragment = fragment_result.newInstance(imageBitmap)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
            recyclerView.visibility = View.GONE

        }
    }

}