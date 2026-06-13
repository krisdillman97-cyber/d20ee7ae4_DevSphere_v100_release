package com.glypheral.devsphere.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.utils.SessionManager

class DownloaderFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_downloader, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isDev = SessionManager.getUser()?.role?.canUseDownloader() == true
        if (!isDev) { view.findViewById<View>(R.id.upgradeOverlay)?.visibility = View.VISIBLE; return }

        val etUrl  = view.findViewById<EditText>(R.id.etStreamUrl)
        val btnDl  = view.findViewById<Button>(R.id.btnStartDownload)
        val progress = view.findViewById<ProgressBar>(R.id.downloadProgress)
        val tvStatus = view.findViewById<TextView>(R.id.tvDownloadStatus)

        btnDl?.setOnClickListener {
            val url = etUrl?.text.toString().trim()
            if (url.isEmpty()) { Toast.makeText(requireContext(), "Enter a URL", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            tvStatus?.text = "Analyzing stream…"
            progress?.visibility = View.VISIBLE
            // Simulate download start
            view.postDelayed({
                tvStatus?.text = "Downloading: ${url.takeLast(40)}"
                progress?.progress = 35
            }, 1000)
        }
    }
}
