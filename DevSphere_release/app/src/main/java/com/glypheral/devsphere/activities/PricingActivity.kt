package com.glypheral.devsphere.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.toast

class PricingActivity : AppCompatActivity() {
    private var billingType = "monthly"  // monthly or lifetime
    private var selectedPlan = "developer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Upgrade DevSphere"

        setupBillingToggle()
        setupPlanSelection()
        setupCheckout()
    }

    private fun setupBillingToggle() {
        val toggle = findViewById<ToggleButton>(R.id.toggleBilling)
        val tvDevPrice   = findViewById<TextView>(R.id.tvDevPrice)
        val tvAdminPrice = findViewById<TextView>(R.id.tvAdminPrice)

        toggle?.setOnCheckedChangeListener { _, isLifetime ->
            billingType = if (isLifetime) "lifetime" else "monthly"
            tvDevPrice?.text   = if (isLifetime) "$1,200 one-time" else "$40/mo"
            tvAdminPrice?.text = if (isLifetime) "$2,400 one-time" else "$150/mo"
        }
    }

    private fun setupPlanSelection() {
        val cardDev   = findViewById<com.google.android.material.card.MaterialCardView>(R.id.cardDeveloper)
        val cardAdmin = findViewById<com.google.android.material.card.MaterialCardView>(R.id.cardAdmin)

        cardDev?.setOnClickListener {
            selectedPlan = "developer"
            cardDev.strokeWidth = 4
            cardAdmin?.strokeWidth = 0
        }
        cardAdmin?.setOnClickListener {
            selectedPlan = "admin"
            cardAdmin.strokeWidth = 4
            cardDev?.strokeWidth = 0
        }
    }

    private fun setupCheckout() {
        findViewById<Button>(R.id.btnCheckout)?.setOnClickListener {
            // Stripe Payment Sheet integration
            initiateStripeCheckout(selectedPlan, billingType)
        }
    }

    private fun initiateStripeCheckout(plan: String, billing: String) {
        // TODO: Create PaymentIntent on backend, then launch Stripe Payment Sheet
        toast("Initiating Stripe checkout: $plan ($billing)…")

        // SIMULATION — upgrade role directly
        val newRole = if (plan == "admin") UserRole.ADMIN else UserRole.DEVELOPER
        SessionManager.updateRole(newRole)
        toast("✔ Upgraded to ${newRole.name}! Restart to apply.")
        finish()
    }

    override fun onSupportNavigateUp(): Boolean { onBackPressed(); return true }
}
