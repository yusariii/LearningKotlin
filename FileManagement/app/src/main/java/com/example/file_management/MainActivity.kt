package com.example.file_management

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.net.toUri

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var tvPath: TextView
    private lateinit var rvFiles: RecyclerView
    private lateinit var adapter: FileAdapter

    private val files = mutableListOf<File>()
    private lateinit var rootDir: File
    private lateinit var currentDir: File

    private var fileToCopy: File? = null

    companion object {
        private const val REQ_STORAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvPath = findViewById(R.id.tvPath)
        rvFiles = findViewById(R.id.rvFiles)

        rvFiles.layoutManager = LinearLayoutManager(this)
        adapter = FileAdapter(files,
            onItemClick = { f -> onFileClicked(f) },
            onItemLongClick = { f, v -> showContextMenu(f, v) }
        )
        rvFiles.adapter = adapter

        rootDir = Environment.getExternalStorageDirectory()
        currentDir = rootDir
        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                loadFiles(currentDir)
            } else {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = "package:$packageName".toUri()
                    startActivityForResult(intent, REQ_STORAGE)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivityForResult(intent, REQ_STORAGE)
                }
            }
        } else {
            val hasRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            val hasWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

            if (hasRead && hasWrite) {
                loadFiles(currentDir)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQ_STORAGE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    loadFiles(currentDir)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                loadFiles(currentDir)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadFiles(dir: File) {
        if (!dir.exists() || !dir.isDirectory) {
            Toast.makeText(this, "Thư mục không tồn tại", Toast.LENGTH_SHORT).show()
            return
        }
        currentDir = dir
        tvPath.text = currentDir.absolutePath

        files.clear()
        val children = dir.listFiles()
        if (children != null) {
            children.sortWith(
                compareBy<File> { !it.isDirectory }.thenBy { it.name.lowercase() }
            )
            files.addAll(children)
        }
        adapter.notifyDataSetChanged()
    }

    private fun onFileClicked(f: File) {
        if (f.isDirectory) {
            loadFiles(f)
        } else {
            openFile(f)
        }
    }

    private fun openFile(f: File) {
        val name = f.name.lowercase()
        when {
            name.endsWith(".txt") -> showTextFile(f)
            name.endsWith(".jpg") || name.endsWith(".jpeg")
                    || name.endsWith(".png") || name.endsWith(".bmp") -> showImageFile(f)
            else -> {
                Toast.makeText(this, "Không hỗ trợ xem file này", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTextFile(f: File) {
        val text = try { f.readText() } catch (e: IOException) { "Lỗi đọc file" }
        val tv = TextView(this).apply {
            this.text = text
            setPadding(32, 32, 32, 32)
        }
        val scroll = ScrollView(this).apply { addView(tv) }
        AlertDialog.Builder(this).setTitle(f.name).setView(scroll).setPositiveButton("Đóng", null).show()
    }

    private fun showImageFile(f: File) {
        val bmp = BitmapFactory.decodeFile(f.absolutePath)
        val img = ImageView(this).apply {
            setImageBitmap(bmp)
            adjustViewBounds = true
        }
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            addView(img)
        }
        AlertDialog.Builder(this).setTitle(f.name).setView(container).setPositiveButton("Đóng", null).show()
    }

    private fun showContextMenu(f: File, anchor: View) {
        val popup = PopupMenu(this, anchor)
        val menu = popup.menu

        if (f.isDirectory) {
            menu.add("Đổi tên").setOnMenuItemClickListener { showRenameDialog(f); true }
            menu.add("Xóa").setOnMenuItemClickListener { confirmDelete(f); true }
        } else {
            menu.add("Đổi tên").setOnMenuItemClickListener { showRenameDialog(f); true }
            menu.add("Sao chép (Copy)").setOnMenuItemClickListener {
                fileToCopy = f
                Toast.makeText(this, "Đã sao chép! Hãy đi đến thư mục đích và nhấn Dán", Toast.LENGTH_SHORT).show()
                invalidateOptionsMenu()
                true
            }
            menu.add("Xóa").setOnMenuItemClickListener { confirmDelete(f); true }
        }
        popup.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val pasteItem = menu?.findItem(R.id.action_paste)
        pasteItem?.isVisible = (fileToCopy != null)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_folder -> {
                showCreateFolderDialog()
                true
            }
            R.id.action_new_file -> {
                showCreateFileDialog()
                true
            }
            R.id.action_paste -> {
                // XỬ LÝ DÁN FILE
                processPasteFile()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun processPasteFile() {
        val src = fileToCopy
        if (src == null || !src.exists()) {
            Toast.makeText(this, "File gốc không còn tồn tại", Toast.LENGTH_SHORT).show()
            fileToCopy = null
            invalidateOptionsMenu()
            return
        }

        val dest = File(currentDir, src.name)

        if (dest.exists()) {
            Toast.makeText(this, "File đã tồn tại ở đây, vui lòng đổi tên file gốc trước", Toast.LENGTH_LONG).show()
            return
        }

        val success = copyFile(src, dest)
        if (success) {
            Toast.makeText(this, "Đã dán file thành công", Toast.LENGTH_SHORT).show()
            loadFiles(currentDir)
            fileToCopy = null
            invalidateOptionsMenu()
        } else {
            Toast.makeText(this, "Lỗi khi dán file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRenameDialog(f: File) {
        val input = EditText(this)
        input.setText(f.name)
        AlertDialog.Builder(this)
            .setTitle("Đổi tên")
            .setView(input)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val newFile = File(f.parentFile, newName)
                    if (f.renameTo(newFile)) loadFiles(currentDir)
                    else Toast.makeText(this, "Đổi tên thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDelete(f: File) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn muốn xóa \"${f.name}\"?")
            .setPositiveButton("Xóa") { _, _ ->
                if (deleteFileOrDir(f)) loadFiles(currentDir)
                else Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteFileOrDir(f: File): Boolean {
        if (f.isDirectory) f.listFiles()?.forEach { deleteFileOrDir(it) }
        return f.delete()
    }

    private fun copyFile(src: File, dest: File): Boolean {
        return try {
            FileInputStream(src).use { inp ->
                FileOutputStream(dest).use { out ->
                    val buffer = ByteArray(4096)
                    var len: Int
                    while (inp.read(buffer).also { len = it } > 0) out.write(buffer, 0, len)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun showCreateFolderDialog() {
        val input = EditText(this)
        input.hint = "Tên thư mục"
        AlertDialog.Builder(this)
            .setTitle("Tạo thư mục mới")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val folder = File(currentDir, name)
                    if (folder.mkdirs()) loadFiles(currentDir)
                    else Toast.makeText(this, "Tạo thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showCreateFileDialog() {
        val input = EditText(this)
        input.hint = "Tên file (vd: note.txt)"
        AlertDialog.Builder(this)
            .setTitle("Tạo file mới")
            .setView(input)
            .setPositiveButton("Tạo") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    try {
                        if (File(currentDir, name).createNewFile()) loadFiles(currentDir)
                        else Toast.makeText(this, "Tạo thất bại", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) { e.printStackTrace() }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        if (currentDir != rootDir) {
            currentDir.parentFile?.let { loadFiles(it) } ?: super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}