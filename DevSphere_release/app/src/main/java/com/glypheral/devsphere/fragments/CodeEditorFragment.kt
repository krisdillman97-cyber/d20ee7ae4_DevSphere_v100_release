package com.glypheral.devsphere.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.glypheral.devsphere.R
import com.glypheral.devsphere.utils.SessionManager

class CodeEditorFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        inflater.inflate(R.layout.fragment_code_editor, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isDev = SessionManager.getUser()?.role?.canUseCodeEditor() == true
        if (!isDev) { view.findViewById<View>(R.id.upgradeOverlay)?.visibility = View.VISIBLE; return }

        val editor = view.findViewById<EditText>(R.id.codeEditor)
        val tvLang = view.findViewById<TextView>(R.id.tvLanguage)
        val spinnerLang = view.findViewById<Spinner>(R.id.spinnerLanguage)

        val langs = arrayOf("JavaScript", "Python", "Kotlin", "Java", "TypeScript", "Bash", "HTML", "CSS", "JSON")
        spinnerLang?.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, langs)

        // Sample starter code
        editor?.setText("// DevSphere Code Editor\n// Start coding...\n\nfunction hello() {\n    console.log('Hello from DevSphere!');\n}\n")
        editor?.typeface = android.graphics.Typeface.MONOSPACE

        view.findViewById<ImageButton>(R.id.btnRun)?.setOnClickListener {
            val code = editor?.text.toString()
            view.findViewById<TextView>(R.id.tvOutput)?.text = "[Run] Code executed (simulated)\n$code"
        }

        view.findViewById<ImageButton>(R.id.btnSave)?.setOnClickListener {
            Toast.makeText(requireContext(), "Snippet saved", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<ImageButton>(R.id.btnCopy)?.setOnClickListener {
            val clip = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
            clip.setPrimaryClip(android.content.ClipData.newPlainText("code", editor?.text))
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
        }
    }
}
