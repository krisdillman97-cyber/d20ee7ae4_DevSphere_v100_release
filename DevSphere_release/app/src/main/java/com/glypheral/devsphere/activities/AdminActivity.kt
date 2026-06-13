package com.glypheral.devsphere.activities

import android.os.Bundle
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.toast

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Admin Panel"

        val user = SessionManager.getUser()
        if (user?.role != UserRole.ADMIN) {
            toast("Admin access required")
            finish()
            return
        }

        setupAdminPanel()
    }

    private fun setupAdminPanel() {
        // User management, role control, billing dashboard, global settings
        setupUserManagement()
        setupBillingDashboard()
        setupGlobalSettings()
        setupHiddenDevMenuAccess()
    }

    private fun setupUserManagement() {
        // Populate user list (would come from API)
        val tvStats = findViewById<TextView>(R.id.tvUserStats)
        tvStats?.text = "Total Users: 1,247 | Developers: 312 | Admins: 8"
    }

    private fun setupBillingDashboard() {
        val tvRevenue = findViewById<TextView>(R.id.tvRevenue)
        tvRevenue?.text = "MRR: $24,840 | LTR: $48,200"
    }

    private fun setupGlobalSettings() {
        val btnToggleMaintenance = findViewById<Switch>(R.id.switchMaintenance)
        btnToggleMaintenance?.setOnCheckedChangeListener { _, on ->
            toast("Maintenance mode: ${if (on) "ON" else "OFF"}")
        }
    }

    private fun setupHiddenDevMenuAccess() {
        // Hidden Dev Menu accessible via Ctrl+Shift+D shortcut or Admin Panel sidebar
        val btnHiddenMenu = findViewById<Button>(R.id.btnOpenHiddenDevMenu)
        btnHiddenMenu?.setOnClickListener {
            toast("Opening Hidden Dev Menu…")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.isCtrlPressed == true && event.isShiftPressed && keyCode == KeyEvent.KEYCODE_D) {
            toast("Hidden Dev Menu: Ctrl+Shift+D activated")
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSupportNavigateUp(): Boolean { onBackPressed(); return true }
}
