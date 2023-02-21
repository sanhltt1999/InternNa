package com.example.dictionary

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout
    private val PERMISSION_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            setNotIsChecked(navView)
            it.isChecked = true
            when (it.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment(), it.title.toString())
                R.id.nav_favorite -> replaceFragment(FavoriteFragment(), it.title.toString())
                R.id.nav_history -> replaceFragment(HistoryFragment(), it.title.toString())
            }
            true
        }

        navView.menu.getItem(0).isChecked = true
        val title = navView.menu.getItem(0).title.toString()
        replaceFragment(HomeFragment(), title)
    }

    private fun setNotIsChecked(view : NavigationView){
        for (i in 0 until view.menu.size()){

            view.menu.getItem(i).isChecked = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment : Fragment, title : String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }
    private fun checkPermission(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).toString()
            ) == PackageManager.PERMISSION_GRANTED -> {
                Toast.makeText(this, "accept", Toast.LENGTH_LONG).show()
            }
            shouldShowRequestPermissionRationale(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).toString()) -> {
                Toast.makeText(this, "denied", Toast.LENGTH_LONG).show()

            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("ddd", PERMISSION_REQUEST_CODE.toString())
                } else {
                    Toast.makeText(this,"dđ", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {

            }
        }
    }

}