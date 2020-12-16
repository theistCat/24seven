package uz.usoft.a24seven.ui.barcodeScanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.fragment_barcode_scanner.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.di.MockData
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias CornersListener=(luma: Double)->Unit

private var barcodeResult:TextView?=null

private var scanner:BarcodeScanner?=null
class BarcodeScannerFragment : Fragment() {
    private var preview:Preview?=null
    private var imageCapture:ImageCapture?=null
    private var imageAnalysis:ImageAnalysis?=null
    private var camera: Camera?=null

    private lateinit var safeContext: Context

    private lateinit var outputDirectory:File
    private lateinit var cameraExecutor:ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_UPC_A,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_UPC_E)
            .build()

        scanner = BarcodeScanning.getClient(options)
        barcodeResult=barcodeResultRaw
        if(allPermissionsGranted()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),REQIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

    //    outputDirectory=getOutputDirectory()
        cameraExecutor=Executors.newSingleThreadExecutor()

    }

    fun startCamera(){
        val cameraProviderFuture=ProcessCameraProvider.getInstance(safeContext)
        Log.d(TAG, "started camera")
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider:ProcessCameraProvider = cameraProviderFuture.get()
            preview=Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA

            imageCapture=ImageCapture.Builder().build()

            imageAnalysis=ImageAnalysis.Builder().build().apply {
                setAnalyzer(cameraExecutor,CornerAnalyzer{it->
                    Log.d(TAG, "Average luminosity: $it")
                })
            }

            try {
                //Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview,imageCapture,imageAnalysis)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(safeContext))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode== REQUEST_CODE_PERMISSIONS)
        {
            if(allPermissionsGranted()){
                startCamera()
            }
            else{
                Toast.makeText(safeContext,"permission not granted",Toast.LENGTH_SHORT)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        super.onPause()
        isOffline=true
    }

    override fun onResume() {
        super.onResume()
        isOffline=false
    }
    private fun allPermissionsGranted()=REQIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext,it)== PackageManager.PERMISSION_GRANTED
    }

    companion object{
        val TAG="CameraX"
        private const val FILENAME_FORMAT="yyyy"
        internal const val REQUEST_CODE_PERMISSIONS=10
        private val REQIRED_PERMISSIONS= arrayOf(android.Manifest.permission.CAMERA)
        var isOffline=false
    }

    private class CornerAnalyzer(private val listener: CornersListener) : ImageAnalysis.Analyzer{
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            if(!isOffline)
            {
                val buffer = imageProxy.planes[0].buffer
                val data = buffer.toByteArray()
                val pixels = data.map { it.toInt() and 0xFF }
                val luma = pixels.average()

                listener(luma)
                val mediaImage = imageProxy.image


                if (mediaImage != null) {
                    Log.d(TAG,"before succes $mediaImage")
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)


                    val result = scanner?.process(image)
                        ?.addOnSuccessListener { barcodes ->
                            Log.d(TAG,"barcode found $barcodes")
                            for (barcode in barcodes) {

                                val bounds = barcode.boundingBox
                                val corners = barcode.cornerPoints

                                val rawValue = barcode.rawValue

                                val valueType = barcode.valueType

                                barcodeResult?.text=rawValue
                                Log.d("barcode","succes: $rawValue")
                                // See API reference for complete list of supported types
                                when (valueType) {
                                    Barcode.TYPE_WIFI -> {
                                        val ssid = barcode.wifi!!.ssid
                                        val password = barcode.wifi!!.password
                                        val type = barcode.wifi!!.encryptionType
                                    }
                                    Barcode.TYPE_URL -> {
                                        val title = barcode.url!!.title
                                        val url = barcode.url!!.url

                                    }
                                    Barcode.TYPE_PRODUCT->{
                                        var list=MockData.getProductList()
                                        for(products in list)
                                            if (products.code == rawValue)
                                            {
                                                barcodeResult?.text=products.name+"\t"+products.price
                                            }
                                    }
                                }
                            }
                        }
                        ?.addOnFailureListener {
                            // Task failed with an exception
                            // ...

                            Log.d(TAG,it.message.toString())
                        }
                        ?.addOnCompleteListener {

                            Log.d(TAG,"closing")
                            mediaImage.close()
                            imageProxy.close()
                        }
                }
            }
        }
    }
}