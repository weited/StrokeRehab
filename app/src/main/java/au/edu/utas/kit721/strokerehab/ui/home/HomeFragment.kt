package au.edu.utas.kit721.strokerehab.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import au.edu.utas.kit721.strokerehab.databinding.FragmentHomeBinding
import android.content.Context.MODE_PRIVATE
import au.edu.utas.kit721.strokerehab.PREFERENCE_FILE
import au.edu.utas.kit721.strokerehab.USERNAME_KEY

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val ui get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = ui.root
        val settings = context!!.getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE)

        ui.textUsername.text = settings.getString(USERNAME_KEY, "Explorer")

        ui.inputUsername.setOnEditorActionListener { v, actionId, event ->
            with (settings.edit()) {
                putString(USERNAME_KEY, ui.inputUsername.text.toString())
                apply()
            }
            ui.textUsername.text = settings.getString(USERNAME_KEY, "Explorer")
            true
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}