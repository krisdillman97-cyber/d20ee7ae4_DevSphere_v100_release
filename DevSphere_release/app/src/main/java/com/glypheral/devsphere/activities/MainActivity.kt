package com.glypheral.devsphere.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.fragments.*
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private var currentUser = SessionManager.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUser = SessionManager.getUser()
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        bottomNav = findViewById(R.id.bottomNav)
        setupNavigation()

        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }
    }

    private fun setupNavigation() {
        val role = currentUser?.role ?: UserRole.GUEST

        // Show/hide nav items based on role
        bottomNav.menu.findItem(R.id.nav_browser)?.isVisible = true
        bottomNav.menu.findItem(R.id.nav_servers)?.isVisible = role.canUseServerManager()
        bottomNav.menu.findItem(R.id.nav_editor)?.isVisible = role.canUseCodeEditor()

        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_dashboard  -> DashboardFragment()
                R.id.nav_browser    -> BrowserFragment()
                R.id.nav_servers    -> if (role.canUseServerManager()) ServerManagerFragment()
                                       else UpgradeFragment()
                R.id.nav_editor     -> if (role.canUseCodeEditor()) CodeEditorFragment()
                                       else UpgradeFragment()
                R.id.nav_more       -> MoreMenuFragment()
                else -> DashboardFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    // Hidden Dev Menu: Ctrl+Shift+D (volume up + down = D on physical KB)
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.isCtrlPressed == true && event.isShiftPressed &&
            keyCode == KeyEvent.KEYCODE_D) {
            val role = currentUser?.role ?: UserRole.GUEST
            if (role.canUseHiddenDevMenu()) {
                loadFragment(HiddenDevMenuFragment())
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
