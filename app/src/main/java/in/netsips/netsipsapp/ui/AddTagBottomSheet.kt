package `in`.netsips.netsipsapp.ui

import `in`.netsips.netsipsapp.R
import `in`.netsips.netsipsapp.databinding.AddTagBottomSheetBinding
import `in`.netsips.netsipsapp.helper.FirestoreArticle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddTagBottomSheet(private val article: FirestoreArticle) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: AddTagBottomSheetBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_tag_bottom_sheet, container, false)

        binding.addTagArticleTitle.text = article.title
        binding.addTagEdit.setText(article.tags)

        binding.submitButton.setOnClickListener {
            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().currentUser!!.uid)
                .document(article.docID).update("tags", binding.addTagEdit.text.toString())
            dismiss()
        }

        return binding.root
    }
}