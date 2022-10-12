package uz.mohirdev.lokmart.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

fun Fragment.toast(message: Int) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.setLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        @Suppress("DEPRECATION")
        requireActivity().window.decorView.systemUiVisibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
    }
}

fun Fragment.clearLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        requireActivity().window.insetsController?.setSystemBarsAppearance(
            0,
            APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        requireActivity().window.decorView.systemUiVisibility = 0
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

val Int.dp: Int get() = (this * getSystem().displayMetrics.density).toInt()
val Int.dpF: Float get() = (this * getSystem().displayMetrics.density)

fun String.toLatLng(): LatLng {
    val coordinates = split(", ")
    return LatLng(
        coordinates[0].toDouble(),
        coordinates[1].toDouble()
    )
}

fun GoogleMap.addMarker(context: Context, drawable: Int, dimen: Int, location: LatLng) : Marker? {
    val bitmap = Bitmap.createBitmap(dimen, dimen, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val shape = ContextCompat.getDrawable(context, drawable) ?: return null
    shape.setBounds(0, 0, bitmap.width, bitmap.height)
    shape.draw(canvas)

    return addMarker(
        MarkerOptions().position(location).anchor(.5f, .5f)
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
    )
}