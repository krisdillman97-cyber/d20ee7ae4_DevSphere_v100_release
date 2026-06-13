package com.glypheral.devsphere.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glypheral.devsphere.R
import com.glypheral.devsphere.models.ServerModel
import com.glypheral.devsphere.models.ServerStatus
import com.glypheral.devsphere.models.UserRole
import com.glypheral.devsphere.utils.SessionManager
import com.glypheral.devsphere.utils.show
import com.glypheral.devsphere.utils.hide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ServerManagerFragment : Fragment() {
    private val servers = mutableListOf<ServerModel>()
    private lateinit var adapter: ServerAdapter

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_server_manager, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isDev = SessionManager.getUser()?.role?.canUseServerManager() == true
        if (!isDev) {
            view.findViewById<View>(R.id.upgradeOverlay)?.show()
            return
        }

        val recycler = view.findViewById<RecyclerView>(R.id.rvServers)
        adapter = ServerAdapter(servers) { server, action ->
            when (action) {
                "start"  -> toggleServer(server, ServerStatus.RUNNING)
                "stop"   -> toggleServer(server, ServerStatus.STOPPED)
                "delete" -> deleteServer(server)
                "logs"   -> showLogs(server)
            }
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabNewServer)
            ?.setOnClickListener { showNewServerDialog() }

        // Load demo servers
        servers.addAll(listOf(
            ServerModel("1", "My Node API", "Node.js", 3000, ServerStatus.RUNNING),
            ServerModel("2", "Python Flask App", "Python", 5000, ServerStatus.STOPPED)
        ))
        adapter.notifyDataSetChanged()
    }

    private fun showNewServerDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_new_server, null)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("New Server")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val name = dialogView.findViewById<TextInputEditText>(R.id.etServerName)?.text.toString()
                val runtime = dialogView.findViewById<Spinner>(R.id.spinnerRuntime)?.selectedItem.toString()
                val port = dialogView.findViewById<TextInputEditText>(R.id.etPort)?.text.toString().toIntOrNull() ?: 3000
                val server = ServerModel(name = name, runtime = runtime, port = port)
                servers.add(server)
                adapter.notifyItemInserted(servers.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun toggleServer(server: ServerModel, status: ServerStatus) {
        val idx = servers.indexOf(server)
        if (idx >= 0) {
            servers[idx] = server.copy(status = status)
            adapter.notifyItemChanged(idx)
        }
    }

    private fun deleteServer(server: ServerModel) {
        val idx = servers.indexOf(server)
        if (idx >= 0) { servers.removeAt(idx); adapter.notifyItemRemoved(idx) }
    }

    private fun showLogs(server: ServerModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("${server.name} — Logs")
            .setMessage(server.logs.joinToString("\n").ifEmpty { "[No logs yet]" })
            .setPositiveButton("Close", null)
            .show()
    }

    inner class ServerAdapter(
        private val items: List<ServerModel>,
        private val onAction: (ServerModel, String) -> Unit
    ) : RecyclerView.Adapter<ServerAdapter.VH>() {
        inner class VH(v: View) : RecyclerView.ViewHolder(v)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_server, parent, false))
        override fun getItemCount() = items.size
        override fun onBindViewHolder(holder: VH, pos: Int) {
            val s = items[pos]
            holder.itemView.findViewById<TextView>(R.id.tvServerName)?.text = s.name
            holder.itemView.findViewById<TextView>(R.id.tvRuntime)?.text = "${s.runtime}:${s.port}"
            val statusBadge = holder.itemView.findViewById<TextView>(R.id.tvStatus)
            statusBadge?.text = s.status.name
            statusBadge?.setTextColor(resources.getColor(
                if (s.status == ServerStatus.RUNNING) R.color.status_running else R.color.status_stopped, null))
            holder.itemView.findViewById<ImageButton>(R.id.btnToggle)?.setOnClickListener {
                onAction(s, if (s.status == ServerStatus.RUNNING) "stop" else "start")
            }
            holder.itemView.findViewById<ImageButton>(R.id.btnLogs)?.setOnClickListener { onAction(s, "logs") }
            holder.itemView.findViewById<ImageButton>(R.id.btnDelete)?.setOnClickListener { onAction(s, "delete") }
        }
    }
}
