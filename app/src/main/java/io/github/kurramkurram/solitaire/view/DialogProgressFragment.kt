package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.kurramkurram.solitaire.databinding.DialogProgressBinding

/**
 * ローディングダイアログ.
 */
class DialogProgressFragment : DialogBaseFragment() {

    companion object {
        private const val KEY_MESSAGE = "key_message"

        fun newInstance(message: String): DialogProgressFragment {
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            val fragment = DialogProgressFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: DialogProgressBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogProgressBinding.inflate(layoutInflater)
        return Dialog(requireContext()).apply {
            val message = requireArguments().getString(KEY_MESSAGE)
            setContentView(binding.root)
            binding.progressMessage.text = message
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
