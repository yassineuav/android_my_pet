package tech.siham.papp.ui.mypost

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil
import android.net.Uri
import androidx.core.content.ContextCompat

import android.provider.MediaStore
import tech.siham.papp.data.requests.PostRequest
import android.os.Build
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import tech.siham.papp.R
import tech.siham.papp.adapter.MyPostListAdapter
import tech.siham.papp.databinding.AddPostDialogLayoutBinding
import tech.siham.papp.databinding.DeletePostDialogLayoutBinding

import tech.siham.papp.databinding.FragmentMyPostBinding
import tech.siham.papp.models.MyPost
import tech.siham.papp.models.Post
import tech.siham.papp.utils.SessionManager
import tech.siham.papp.utils.getFileName
import tech.siham.papp.utils.snackbar
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class MyPostFragment : Fragment() {

    private lateinit var myPostViewModel: MyPostViewModel

    private var _binding: FragmentMyPostBinding? = null
    private val binding get() = _binding!!

    val IMAGE_PICK_CODE:Int = 1829
    private var imageUri: Uri? = null
    lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPostViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(MyPostViewModel::class.java)

        _binding = FragmentMyPostBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this

        binding.data = myPostViewModel

        binding.post.adapter = MyPostListAdapter(
            MyPostListAdapter.OnClickListener{
                showDeleteDialog(it)
            },
            // PostListAdapter.OnClickListener{ showEditDialog() }
        )

        // binding.post.adapter?.notifyDataSetChanged()


        myPostViewModel.myPost.observe(viewLifecycleOwner, {
            if(it != null){
                Toast.makeText(requireContext(), "text: "+ it.toString(), Toast.LENGTH_LONG).show()
            }
        })

        binding.addPost.setOnClickListener{
            showAddDialog()
        }

        binding.deleteAllPost.setOnClickListener {
            //showDeleteDialog("Delete All Posts", "Are you Sure! you want to delete all posts", it)
            // pass all
        }

        return binding.root
    }

    private fun showAddDialog() {
        val dialogBinding: AddPostDialogLayoutBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.add_post_dialog_layout,
                null,
                false
            )

        val customDialog = AlertDialog.Builder(requireContext(), 0).create()

        customDialog.apply {
            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()

        if (dialogBinding != null ) {
            imageView = dialogBinding.imageView
            if(imageUri != null)
                imageView.setImageURI(imageUri)
        }

        dialogBinding?.progressBar?.progress = 0

        dialogBinding?.buttonSelectImage?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ){
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            startActivityForResult(pickIntent, IMAGE_PICK_CODE)
            }
        }

        dialogBinding?.buttonAddPost?.setOnClickListener {

            val desc:String = dialogBinding?.postDescription?.text.toString()

            when {
                imageUri == null -> {
                    binding.rootLayout.snackbar("select an image first!!")
                }
                desc.isEmpty() -> {
                    binding.rootLayout.snackbar("Please Enter a Post Description !!")
                }
                else -> {

                    val parcelFileDescriptor =
                        requireContext().contentResolver.openFileDescriptor(imageUri!!, "r", null)
                            ?: return@setOnClickListener
                    val file = File(
                        requireContext().cacheDir, requireContext().contentResolver.getFileName(imageUri!!)
                    )

                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)

                    // progressbar = 0

                    val userId :Int = SessionManager().getUserId()

                    Log.e("SHOW USER ID", "user id use :$userId")

//                    myPostViewModel.setPost(
//                        file,
//                        PostRequest(
//                            "post uploaded by android user id :$userId",
//                            userId,
//                            desc,
//                            "image")
//                    )

//                    customDialog.dismiss()
                }
            }
        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun showEditDialog() {
        val dialogBinding: AddPostDialogLayoutBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.add_post_dialog_layout,
                null,
                false
            )

        val customDialog = AlertDialog.Builder(requireContext(), 0).create()

        customDialog.apply {
            setView(dialogBinding?.root)
            setCancelable(false) }.show()

        dialogBinding?.buttonAddPost?.setOnClickListener {
            //postViewModel.setPost(Post("","test Post", "this is test desc","text/content","post/images/life.jpg"))
            customDialog.dismiss()
        }
    }


    private fun showDeleteDialog(post: MyPost) {
        // val id = post.url.split("/").takeLast(2).first()
        val id:Int? = post.id

        val dialog = Dialog(requireContext())
        val dialogBinding = DeletePostDialogLayoutBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(dialogBinding.root)

        dialogBinding.title.text = "Delete: "+post.title+"\t\t id:"+ id
        dialogBinding.desc.text = "desc: "+ post.description +"\n"+"content: "+ post.content +"\n"+ id

        dialogBinding.buttonDelete.setOnClickListener {
//            Snackbar.make(view, "Open Delete Dialog", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
               myPostViewModel.deletePost(id)

            dialog.dismiss()
        }
        dialog.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}