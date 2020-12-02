package co.patrickflynn.blockplanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.get
import androidx.fragment.app.DialogFragment

class NewBlock : DialogFragment() {
    internal lateinit var listener: NoticeDialogListener
    internal lateinit var entry : EditText
    internal lateinit var startEntry : EditText
    internal lateinit var endEntry : EditText
    internal lateinit var colorChooser : Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            var inflater = requireActivity().layoutInflater

            var view = inflater.inflate(R.layout.fragment_new_block, null)
            builder.setView(view)

            entry = view.findViewById(R.id.task_name_entry)
            startEntry = view.findViewById(R.id.startEntry)
            endEntry = view.findViewById(R.id.endEntry)
            colorChooser = view.findViewById(R.id.color_chooser)

            builder.setPositiveButton("Create",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(this)
                    })
             builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: NewBlock)
        fun onDialogNegativeClick(dialog: NewBlock)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    fun getStart() : Int {
        return Integer(startEntry.text.toString()).toInt()
    }

    fun getEnd() : Int {
        return Integer(endEntry.text.toString()).toInt()
    }

    fun getColor() : Int {
        val pos = colorChooser.selectedItemPosition
        val color = colorChooser.getItemAtPosition(pos).toString()

        if (color == "Cyan") return Color.CYAN
        else if (color == "Light Gray") return Color.LTGRAY
        else if (color == "Magenta") return Color.MAGENTA
        else if (color == "Green") return Color.GREEN
        else if (color == "Blue") return Color.BLUE
        else if (color == "Yellow") return Color.YELLOW
        return Color.WHITE
    }

    fun getTask() : String {
        return entry.text.toString()
    }
}
