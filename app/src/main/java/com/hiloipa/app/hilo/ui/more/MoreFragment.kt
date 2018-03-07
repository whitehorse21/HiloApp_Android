package com.hiloipa.app.hilo.ui.more


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.LogoutRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.Account
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.auth.AuthActivity
import com.hiloipa.app.hilo.ui.more.account.AccountActivity
import com.hiloipa.app.hilo.ui.more.email.EmailTemplatesActivity
import com.hiloipa.app.hilo.ui.more.notes.NotepadActivity
import com.hiloipa.app.hilo.ui.more.products.ProductsActivity
import com.hiloipa.app.hilo.ui.more.scripts.ScriptsActivity
import com.hiloipa.app.hilo.ui.widget.CameraActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_more.*
import vn.tungdx.mediapicker.MediaItem
import vn.tungdx.mediapicker.MediaOptions
import vn.tungdx.mediapicker.activities.MediaPickerActivity
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class MoreFragment : Fragment() {

    companion object {
        fun newInstance(): MoreFragment {
            val args = Bundle()
            val fragment = MoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_more, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        templatesBtn.setOnClickListener {
            val templatesIntent = Intent(activity, EmailTemplatesActivity::class.java)
            activity.startActivity(templatesIntent)
        }

        notepadBtn.setOnClickListener {
            val notepadIntent = Intent(activity, NotepadActivity::class.java)
            activity.startActivity(notepadIntent)
        }

        feedbackBtn.setOnClickListener {
            val feedbackIntent = Intent(activity, FeedbackActivity::class.java)
            activity.startActivity(feedbackIntent)
        }

        productsBtn.setOnClickListener {
            val productsIntent = Intent(activity, ProductsActivity::class.java)
            activity.startActivity(productsIntent)
        }

        logoutBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.log_out))
                    .setMessage(getString(R.string.log_out_confirm))
                    .setPositiveButton(getString(R.string.yes), { dialog, which ->
                        dialog.dismiss()

                        val loadingDialog = activity.showLoading()
                        try {
                            HiloApp.api().logout(LogoutRequest()).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({ response: HiloResponse<Any> ->
                                        loadingDialog.dismiss()
                                        HiloApp.instance.setIsLoggedIn(false)
                                        HiloApp.instance.saveAccessToken("")
                                        val logoutIntent = Intent(activity, AuthActivity::class.java)
                                        activity.startActivity(logoutIntent)
                                        activity.finish()
                                    }, { error: Throwable ->
                                        error.printStackTrace()
                                        loadingDialog.dismiss()
                                        val logoutIntent = Intent(activity, AuthActivity::class.java)
                                        activity.startActivity(logoutIntent)
                                        activity.finish()
                                    })
                        } catch (e: Exception) {
                            e.printStackTrace()
                            loadingDialog.dismiss()
                            val logoutIntent = Intent(activity, AuthActivity::class.java)
                            activity.startActivity(logoutIntent)
                            activity.finish()
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .create()
            dialog.show()
        }

        accountBtn.setOnClickListener {
            val accountIntent = Intent(activity, AccountActivity::class.java)
            activity.startActivityForResult(accountIntent, 1553)
        }

        scriptsBtn.setOnClickListener {
            val scriptsIntent = Intent(activity, ScriptsActivity::class.java)
            activity.startActivity(scriptsIntent)
        }

        Picasso.with(activity)
                .load(HiloApp.userData.userImage)
                .into(userAvatar)

        userAvatar.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.alert_user_image, null)
            val image: ImageView = dialogView.findViewById(R.id.imageView)
            val button: RalewayButton = dialogView.findViewById(R.id.updateBtn)
            Picasso.with(activity).load(HiloApp.userData.userImage).into(image)
            val dialog = AlertDialog.Builder(activity)
                    .setView(dialogView)
                    .create()

            button.setOnClickListener {
                dialog.dismiss()
                showUpdateImageAlert()
            }

            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        getAccount()
    }

    private fun getAccount() {
        val loading = activity.showLoading()
        HiloApp.api().getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<Account> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        userNameLabel.text = "${response.data!!.firstName} ${response.data!!.lastName}"
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    private fun showUpdateImageAlert() {
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.change_avatar))
                .setMessage(getString(R.string.change_avatar_message))
                .setPositiveButton(getString(R.string.from_gallery), { dialog, which ->
                    val options = MediaOptions.Builder()
                            .selectPhoto()
                            .build()
                    MediaPickerActivity.open(this, 1257, options)
                })
                .setNegativeButton(getString(R.string.from_camera), { dialog, which ->
                    val pictureIntent = Intent(activity, CameraActivity::class.java)
                    activity.startActivityForResult(pictureIntent, 1343)
                })
                .setNeutralButton(getString(R.string.cancel), null)
                .create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var image: Pair<String, String>? = null
            when (requestCode) {
                1343 -> {
                    if (data != null) {
                        val file = File(data.extras.getString(CameraActivity.pathKey))
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                        image = Pair(first = file.absolutePath, second = encodedImage)
                    }
                }

                1257 -> {
                    val items: ArrayList<MediaItem>? = MediaPickerActivity.getMediaItemSelected(data);
                    if (items != null && items.size > 0) {
                        val mediaItem: MediaItem = items.get(0)
                        val path: String? = mediaItem.getPathOrigin(activity)
                        if (path != null) {
                            val file = File(path)
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                            image = Pair(first = file.absolutePath, second = encodedImage)
                        }
                    }
                }

                1553 -> getAccount()
            }

            if (image != null) {
                this.saveUserImage(image)
            }
        }
    }

    private fun saveUserImage(imageData: Pair<String, String>) {
        val request = StandardRequest()
        request.image = imageData.second

        val loading = activity.showLoading()
        HiloApp.api().updateUserAvatar(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val file = File(imageData.first)
                        Picasso.with(activity)
                                .load(file)
                                .into(userAvatar)
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }
}
