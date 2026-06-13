package com.glypheral.devsphere.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glypheral.devsphere.R
import com.glypheral.devsphere.activities.PricingActivity
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.show
import com.glypheral.devsphere.utils.hide

class DashboardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = SessionManager.getUser() ?: return

        // Greeting
        view.findViewById<TextView>(R.id.tvGreeting)?.text = "Hey, ${user.name.ifEmpty { "Developer" }} 👋"
        view.findViewById<TextView>(R.id.tvRole)?.text = "Role: ${user.role.name}"

        // Role badge color
        val badge = view.findViewById<TextView>(R.id.tvRoleBadge)
        badge?.text = user.role.name
        badge?.setBackgroundResource(when (user.role) {
            UserRole.ADMIN -> R.drawable.badge_admin
            UserRole.DEVELOPER -> R.drawable.badge_developer
            UserRole.FREE -> R.drawable.badge_user
            else -> R.drawable.badge_guest
        })

        // Upgrade CTA for free users
        val upgradeCta = view.findViewById<View>(R.id.upgradeCtaCard)
        if (user.role == UserRole.FREE || user.role == UserRole.GUEST) {
            upgradeCta?.show()
            view.findViewById<Button>(R.id.btnUpgrade)?.setOnClickListener {
                startActivity(Intent(requireContext(), PricingActivity::class.java))
            }
        } else {
            upgradeCta?.hide()
        }

        // Active servers (Developer+)
        val serversSection = view.findViewById<View>(R.id.serversSection)
        if (user.role.canUseServerManager()) serversSection?.show() else serversSection?.hide()

        // Recent downloads (Developer+)
        val downloadsSection = view.findViewById<View>(R.id.downloadsSection)
        if (user.role.canUseDownloader()) downloadsSection?.show() else downloadsSection?.hide()

        // Activity feed (Admin)
        val activitySection = view.findViewById<View>(R.id.activitySection)
        if (user.role.canUseAdminPanel()) activitySection?.show() else activitySection?.hide()
    }
}
