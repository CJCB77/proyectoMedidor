package com.example.proyectomedidor

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.proyectomedidor.Models.Registro
import com.example.proyectomedidor.Utils.SessionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NuevoRegistroActivity : AppCompatActivity() {

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude:String = "2.2153088"
    private var longitude:String ="-79.8903867"

    private lateinit var codigoVivienda: TextView
    private var imageBitmap: Bitmap? = null
    private lateinit var photoPath: String
    private lateinit var photoUri: Uri

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_registro)
        codigoVivienda = findViewById(R.id.inputCodigoViv)


        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //getCurrentLocation()


    }

    //Agarrar ubicacion
    fun getCurrentLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                //Get the latitude and longitude
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location:Location? = task.result
                    if(location == null){
                        Log.i("Location","Location is NULL")
                    }else{
                        Log.i("Location","Location get success")
                        Log.i("Location","Latitude ${location.latitude}")
                        Log.i("Location","Longitude ${location.longitude}")
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }

            }else{
                //Enable location manually
                Toast.makeText(this, "Turn on the location", Toast.LENGTH_SHORT).show()
                val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(locationIntent)
            }

        }else{
            //Request permission
            requestPermission()

        }
    }

    //Permisos
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean
    {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }

    //Mas permisos
    fun checkPermissionAndOpenCamera(view: View) {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 5)
        } else {
            takePicture()
        }
    }

    //Muchos permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    //Guardar registro
    fun saveRegister(view: View){
        Log.i("Nuevo", "Registro Creado")
        Log.i("Nuevo", "Codigo: ${codigoVivienda.text}")
        Log.i("Nuevo", "GPS: 198.11")
        Log.i("Nuevo", "Image Path: $photoPath")

        CoroutineScope(Dispatchers.IO).launch {
            createRegister(photoPath)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE &&  resultCode == RESULT_OK){
            imageBitmap = data?.extras?.get("data") as? Bitmap
            galleryAddPic()
            Toast.makeText(applicationContext, "Foto guardada", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture(){
        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //Create the file where photo should go
        val photoFile: File? = try {
            createImageFile()
        }catch (ex: IOException){
            Log.d("DebugImage", "$ex")
            null
        }
        Log.i("DebugImage", "Imagen creada")
        //Continue if file was succesfully created
        if (photoFile != null) {
            Log.i("DebugImage", "$photoFile")
            photoUri = FileProvider.getUriForFile(this,
                "com.example.android.fileprovider",
                photoFile
            )
            takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePhoto,REQUEST_IMAGE_CAPTURE)
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoPath = absolutePath
        }
    }
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(photoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun createRegister(filepath:String){
        val file: File = File(filepath)
        var objRegistro: Registro? = null

        // Parse the data into multiform parts
        val idUsuario =  sessionManager.fetchUser()
        val idUsuarioPart = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
             idUsuario.toString()
            )

        val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData("imagen",
                file.name,
                RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    file
                ))

        val codigoPart = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
                codigoVivienda.text.toString()
            )

        val gpsPart = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "$latitude $longitude"
        )

        apiClient.getApiService(this).createRegistro(imagePart,idUsuarioPart,codigoPart,gpsPart).enqueue(object: Callback<Registro> {
            override fun onResponse(call: Call<Registro>, response: Response<Registro>) {
                objRegistro = response?.body()
                Toast.makeText(applicationContext,"Registro creado", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Registro>, t: Throwable) {
                t?.printStackTrace()
            }
        })

    }


    /*
    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(photoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }
*/



}