package com.glypheral.devsphere.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager

class HiddenDevMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_hidden_dev_menu, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = SessionManager.getUser()
        if (user?.role?.canUseHiddenDevMenu() != true) {
            view.findViewById<TextView>(R.id.tvDevMenuTitle)?.text = "⛔ Admin access required"
            return
        }

        view.findViewById<TextView>(R.id.tvDevMenuTitle)?.text = "🔐 Hidden Dev Menu"

        // Module toggles
        setupToggle(view, R.id.toggleHIT,  "HIT Module")
        setupToggle(view, R.id.toggleCrew, "Crew Orchestrator")
        setupToggle(view, R.id.toggleADR,  "ADR Matrix")
        setupToggle(view, R.id.toggleDeep, "Deep Matrix")

        // Termux Full Bridge
        view.findViewById<Button>(R.id.btnTermuxBridge)?.setOnClickListener {
            Toast.makeText(requireContext(), "Termux Full Bridge — activating…", Toast.LENGTH_SHORT).show()
        }

        // Device API Panel
        view.findViewById<Button>(R.id.btnDeviceApi)?.setOnClickListener {
            showDeviceApiPanel(view)
        }
    }

    private fun setupToggle(view: View, id: Int, label: String) {
        val toggle = view.findViewById<Switch>(id)
        toggle?.text = label
        toggle?.setOnCheckedChangeListener { _, checked ->
            Toast.makeText(requireContext(), "$label: ${if (checked) "ON" else "OFF"}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeviceApiPanel(view: View) {
        val panel = view.findViewById<View>(R.id.deviceApiPanel)
        panel?.visibility = if (panel?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}
