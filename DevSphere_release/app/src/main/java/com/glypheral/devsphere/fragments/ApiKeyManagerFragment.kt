package com.glypheral.devsphere.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.ApiKey
import com.glypheral.devsphere.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ApiKeyManagerFragment : Fragment() {
    private val apiKeys = mutableListOf(
        ApiKey(provider = "OpenAI",    label = "GPT-4 Key",  maskedKey = "sk-****...AbcD"),
        ApiKey(provider = "Anthropic", label = "Claude Key", maskedKey = "sk-ant-****...XyZ"),
        ApiKey(provider = "AWS",       label = "AWS Secret", maskedKey = "****kEyS"),
        ApiKey(provider = "GitHub",    label = "PAT Token",  maskedKey = "ghp_****...1234"),
    )

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_api_keys, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (SessionManager.getUser()?.role?.canUseApiKeyManager() != true) {
            view.findViewById<View>(R.id.upgradeOverlay)?.visibility = View.VISIBLE
            return
        }
        val rv = view.findViewById<RecyclerView>(R.id.rvApiKeys)
        val adapter = ApiKeyAdapter(apiKeys)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddKey)
            ?.setOnClickListener { showAddKeyDialog(adapter) }
    }

    private fun showAddKeyDialog(adapter: ApiKeyAdapter) {
        val dv = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_api_key, null)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add API Key")
            .setView(dv)
            .setPositiveButton("Save") { _, _ ->
                val provider = dv.findViewById<TextInputEditText>(R.id.etProvider)?.text.toString()
                val key = dv.findViewById<TextInputEditText>(R.id.etApiKey)?.text.toString()
                val masked = if (key.length > 8) key.take(4) + "****" + key.takeLast(4) else "****"
                apiKeys.add(ApiKey(provider = provider, label = "$provider Key", maskedKey = masked))
                adapter.notifyItemInserted(apiKeys.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    inner class ApiKeyAdapter(private val items: List<ApiKey>) : RecyclerView.Adapter<ApiKeyAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v)
        override fun onCreateViewHolder(p: ViewGroup, t: Int) =
            VH(LayoutInflater.from(p.context).inflate(R.layout.item_api_key, p, false))
        override fun getItemCount() = items.size
        override fun onBindViewHolder(h: VH, pos: Int) {
            val k = items[pos]
            h.itemView.findViewById<TextView>(R.id.tvProvider)?.text = k.provider
            h.itemView.findViewById<TextView>(R.id.tvMaskedKey)?.text = k.maskedKey
            h.itemView.findViewById<ImageButton>(R.id.btnDeleteKey)?.setOnClickListener {
                (items as MutableList).removeAt(pos)
                notifyItemRemoved(pos)
            }
        }
    }
}
