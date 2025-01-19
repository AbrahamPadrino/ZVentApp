package com.example.z_ventapp.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.z_ventapp.R
import com.example.z_ventapp.databinding.ActivityMainBinding
import com.example.z_ventapp.databinding.NavHeaderMainBinding
import com.example.z_ventapp.domain.model.Usuario
import com.example.z_ventapp.presentation.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_cliente, R.id.nav_producto,
                R.id.nav_reporte_ticket, R.id.nav_reporte_mensual, R.id.nav_reporte_caja,
                R.id.nav_empresa, R.id.nav_impresora, R.id.nav_usuario,
                R.id.nav_cambiar_clave, R.id.nav_acerca_de
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Pasar Nombre y Correo de Usuario a menu_header //
        NavHeaderMainBinding.bind(binding.navView.getHeaderView(0)).apply {
            tvTitulo.text = mUsuario?.nombre
            tvSubtitulo.text = mUsuario?.email
        }

        if (mUsuario == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    // Obtiene el usuario
    companion object {
        var mUsuario: Usuario? = null
    }
}