package ir.protech21.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*


class Utils {
    companion object {

//        AtomicInteger leftButton = new AtomicInteger();
//        myButton.getViewTreeObserver().addOnGlobalLayoutListener(
//        () -> {
//            if (leftButton.get() == 0) {
//                leftSubtitle.set(myButton.getLeft());
//
//                //do something once you get the value..
//            }
//        }
//        );

        fun isJson(str: String?): Boolean = try {
            JSONArray(str)
            true
        } catch (e: JSONException) {
            try {
                JSONObject(str)
                true
            } catch (e: JSONException) {
                false
            }
        }

        fun isJsonArray(str: String): Boolean = try {
            JSONArray(str)
            true
        } catch (e: JSONException) {
           false
        }

        fun isJsonObject(str: String): Boolean = try {
            JSONObject(str)
            true
        } catch (e: JSONException) {
            false
        }


        fun AddFragment(fragmentManager: FragmentManager, fragment: Fragment, id: Int, backStack: String? = null): FragmentManager? {
            fragmentManager.executePendingTransactions();
            if (backStack == null) {
//                R.id.orderFrameLayout
                fragmentManager.beginTransaction()
                        .add(id, fragment).commit()
            } else {
                if (fragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null) {
                    return null
                }
                fragmentManager.beginTransaction()
                        .addToBackStack(backStack)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(id, fragment, fragment.javaClass.simpleName).commit()
            }
            return fragmentManager
        }

        fun LoadFragment(fragmentManager: FragmentManager, fragment: Fragment, id: Int, backStack: String? = null): FragmentManager? {
            fragmentManager.executePendingTransactions();
            if (backStack == null) {
                try {
                    fragmentManager.beginTransaction()
                            .replace(id, fragment, fragment.javaClass.simpleName).commit()
                } catch (e: IllegalStateException) {
                }

            } else {
                if (fragmentManager.findFragmentByTag(fragment.javaClass.simpleName) != null) {
                    return null
                }
                fragmentManager.beginTransaction()
                        .addToBackStack(backStack)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(id, fragment, fragment.javaClass.simpleName).commit()
            }
            return fragmentManager
        }

        fun getRelativeLeft(myView: View): Int {
            return if (myView.parent === myView.rootView) myView.left else myView.left + getRelativeLeft(myView.parent as View)
        }

        fun getRelativeTop(myView: View): Int {
            return if (myView.parent === myView.rootView) myView.top else myView.top + getRelativeTop(myView.parent as View)
        }

        fun disableEnableControl(enable: Boolean, viewGroup: ViewGroup) {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)
                child.isEnabled = enable
                if (child is ViewGroup) {
                    disableEnableControl(enable, child)
                }
            }
        }

        fun Print(vararg data: Any?) {
            val stringBuilder = StringBuilder()
            for (i in data)
                stringBuilder.append(i).append(" ")

            Log.e("print", "$stringBuilder")
        }

        fun getPath(context: Context?, uri: Uri?): String? {
            if (context == null || uri == null) {
                return null
            }
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        isKitKat && DocumentsContract.isDocumentUri(context, uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        DocumentsContract.getDocumentId(uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    };
                    val split = docId.split(":");
                    val type = split[0];

                    if ("primary" == type) {
                        return Environment.getExternalStorageDirectory().absolutePath + "/" + split[1]
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    val id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        DocumentsContract.getDocumentId(uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    };
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            id.toLong());

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    val docId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        DocumentsContract.getDocumentId(uri)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    };
                    val split = docId.split(":");
                    val type = split[0];

                    var contentUri: Uri? = null
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    val selection = "_id=?";
                    val selectionArgs = arrayOf(split[1])

                    return contentUri?.let {
                        getDataColumn(context, it, selection,
                                selectionArgs)
                    };
                }
            } else if ("content" == uri.scheme) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.lastPathSegment;

                return getDataColumn(context, uri, null, null);
            } else if ("file" == uri.scheme) {
                return uri.path;
            }

            return null
        }

        fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
            var cursor: Cursor? = null;
            val column = "_data";
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(uri, projection,
                        selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    var index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } catch (e: SecurityException) {
            } finally {
                cursor?.close();
            }
            return null;
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        }

        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority;
        }

        class StaticFragment(val intent: Intent, val function: (Intent?) -> Unit) : Fragment() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                startActivityForResult(intent, 2585)
            }

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK && requestCode == 2585)
                    function(data)

            }
        }

        fun encodeImage(bm: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        fun encodeImage(path: String): String? {
            val imagefile = File(path)
            var fis: FileInputStream? = null
            try {
                fis = FileInputStream(imagefile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bm = BitmapFactory.decodeStream(fis)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos)
            val b = baos.toByteArray()
            //Base64.de
            return Base64.encodeToString(b, Base64.DEFAULT)
        }


    }
}