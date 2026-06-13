package com.glypheral.devsphere.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.activities.PricingActivity
import com.glypheral.devsphere.activities.AdminActivity
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager

class MoreMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_more_menu, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = SessionManager.getUser()
        val role = user?.role ?: UserRole.GUEST

        view.findViewById<TextView>(R.id.tvMoreUserName)?.text = user?.name ?: "Guest"
        view.findViewById<TextView>(R.id.tvMoreEmail)?.text = user?.email ?: ""

        view.findViewById<View>(R.id.menuDownloader)?.apply {
            visibility = if (role.canUseDownloader()) View.VISIBLE else View.GONE
            setOnClickListener { loadFragment(DownloaderFragment()) }
        }
        view.findViewById<View>(R.id.menuTeam)?.apply {
            visibility = if (role.canUseTeam()) View.VISIBLE else View.GONE
        }
        view.findViewById<View>(R.id.menuApiKeys)?.apply {
            visibility = if (role.canUseApiKeyManager()) View.VISIBLE else View.GONE
            setOnClickListener { loadFragment(ApiKeyManagerFragment()) }
        }
        view.findViewById<View>(R.id.menuAdmin)?.apply {
            visibility = if (role.canUseAdminPanel()) View.VISIBLE else View.GONE
            setOnClickListener { startActivity(Intent(requireContext(), AdminActivity::class.java)) }
        }
        view.findViewById<View>(R.id.menuUpgrade)?.apply {
            visibility = if (role == UserRole.FREE) View.VISIBLE else View.GONE
            setOnClickListener { startActivity(Intent(requireContext(), PricingActivity::class.java)) }
        }
        view.findViewById<View>(R.id.menuSignOut)?.setOnClickListener {
            SessionManager.clearSession()
            startActivity(Intent(requireContext(), com.glypheral.devsphere.activities.AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
